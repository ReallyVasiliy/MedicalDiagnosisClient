package us.kulakov.symptomcheck.features.terms;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;
import us.kulakov.symptomcheck.R;
import us.kulakov.symptomcheck.features.base.BaseActivity;
import us.kulakov.symptomcheck.features.common.ErrorView;
import us.kulakov.symptomcheck.features.portal.PortalActivity;
import us.kulakov.symptomcheck.injection.component.ActivityComponent;

public class TermsActivity extends BaseActivity implements TermsMvpView, ErrorView.ErrorListener {

    @Inject
    TermsPresenter presenter;

    @BindView(R.id.view_error)
    ErrorView errorView;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        errorView.setErrorListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_terms;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        presenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        presenter.detachView();
    }


    @Override
    public void onReloadData() {
        errorView.setVisibility(View.GONE);
        presenter.begin();
    }

    @OnClick(R.id.button_begin)
    public void onBeginClick() {
        errorView.setVisibility(View.GONE);
        presenter.begin();
    }


    @Override
    public void showError() {
        errorView.setVisibility(View.VISIBLE);
        Timber.e("There was an error");
    }

    @Override
    public void showTerms() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.terms_dialog_message)
                .setTitle(R.string.terms_dialog_title);

        builder.setNeutralButton(R.string.dialog_action_read_terms, (dialog, id) -> {
            Uri uri = Uri.parse(getString(R.string.terms_url));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        builder.setPositiveButton(R.string.dialog_action_terms_accept, (dialog, id) -> {
            presenter.acceptTerms();
        });
        builder.setNegativeButton(R.string.dialog_action_cancel, (dialog, id) -> {
            presenter.declineTerms();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView message = dialog.findViewById(android.R.id.message);
        if (message != null) {
            message.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }

    @Override
    public void showNext() {
        startActivity(PortalActivity.getStartIntent(this));
    }
}
