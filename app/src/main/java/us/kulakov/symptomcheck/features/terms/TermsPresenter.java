package us.kulakov.symptomcheck.features.terms;

import javax.inject.Inject;

import us.kulakov.symptomcheck.data.EndlessSessionManager;
import us.kulakov.symptomcheck.features.base.BasePresenter;
import us.kulakov.symptomcheck.injection.ConfigPersistent;
import us.kulakov.symptomcheck.util.rx.scheduler.SchedulerUtils;

@ConfigPersistent
public class TermsPresenter extends BasePresenter<TermsMvpView> {

    private final EndlessSessionManager endlessSessionManager;

    @Inject
    public TermsPresenter(EndlessSessionManager endlessSessionManager) {
        this.endlessSessionManager = endlessSessionManager;
    }

    @Override
    public void attachView(TermsMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void doSomething() {
//        addDisposable(...);
    }

    public void begin() {
        addDisposable(endlessSessionManager.newSession()
                .compose(SchedulerUtils.ioToMain())
                .subscribe(ok -> {
                    if (ok) {
                        getView().showTerms();
                    } else {
                        getView().showError();
                    }
                })
        );
    }

    public void acceptTerms() {
        addDisposable(endlessSessionManager.acceptTerms()
                .compose(SchedulerUtils.ioToMain())
                .subscribe(ok -> {
                    if (ok) {
                        getView().showNext();
                    } else {
                        getView().showError();
                    }
                })
        );
    }

    public void declineTerms() {
        // TODO: Do something here?
    }
}
