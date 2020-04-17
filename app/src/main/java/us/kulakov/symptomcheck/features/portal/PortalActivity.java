package us.kulakov.symptomcheck.features.portal;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import us.kulakov.symptomcheck.R;
import us.kulakov.symptomcheck.features.base.BaseActivity;
import us.kulakov.symptomcheck.features.portal.analysis.AnalysisFragment;
import us.kulakov.symptomcheck.features.portal.features.FeaturesFragment;
import us.kulakov.symptomcheck.injection.component.ActivityComponent;

public class PortalActivity extends BaseActivity {

    private PortalPagerAdapter adapter;

    @BindView(R.id.portal_pager)
    ViewPager pager;

    @BindView(R.id.portal_tab_layout)
    TabLayout tabLayout;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, PortalActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        List<Pair<Fragment, String>> children = new ArrayList<>();
        children.add(Pair.create(FeaturesFragment.newInstance(), getString(R.string.page_title_features)));
        children.add(Pair.create(AnalysisFragment.newInstance(), getString(R.string.page_title_analysis)));
        adapter = new PortalPagerAdapter(fm, children);
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);
    }


    @Override
    public int getLayout() {
        return R.layout.activity_portal;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {

    }

    @Override
    protected void detachPresenter() {

    }
}
