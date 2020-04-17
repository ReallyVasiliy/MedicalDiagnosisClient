package us.kulakov.symptomcheck.features.portal.analysis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import us.kulakov.symptomcheck.R;
import us.kulakov.symptomcheck.features.base.BaseFragment;
import us.kulakov.symptomcheck.injection.component.FragmentComponent;

public class AnalysisFragment extends BaseFragment implements AnalysisMvpView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.diseases_recycler)
    RecyclerView recycler;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    private RecyclerView.Adapter adapter;

    @Inject
    AnalysisPresenter presenter;

    public static AnalysisFragment newInstance() {
        Bundle args = new Bundle();
//        TODO: Add arguments e.g. args.putParcelable(Constants.ID, id)
        AnalysisFragment fragment = new AnalysisFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_analysis;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void attachView() {
        presenter.attachView(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            presenter.refreshDiseases();
    }

    @Override
    protected void detachPresenter() {
        presenter.detachView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.refreshDiseases();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recycler.setLayoutManager(layoutManager);
        adapter = new DiseasesListAdapter(presenter.getDiseases());
        recycler.setAdapter(adapter);

        swipeLayout.setOnRefreshListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void showError() {

    }

    @Override
    public void setWorking(boolean isWorking) {
        swipeLayout.setRefreshing(isWorking);
    }

    @Override
    public void updateDiseases() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        presenter.refreshDiseases();
    }
}
