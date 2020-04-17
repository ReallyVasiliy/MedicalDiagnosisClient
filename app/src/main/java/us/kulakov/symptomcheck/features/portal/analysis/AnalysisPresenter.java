package us.kulakov.symptomcheck.features.portal.analysis;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import us.kulakov.symptomcheck.data.EndlessSessionManager;
import us.kulakov.symptomcheck.data.entities.Disease;
import us.kulakov.symptomcheck.features.base.BasePresenter;
import us.kulakov.symptomcheck.injection.ConfigPersistent;
import us.kulakov.symptomcheck.util.rx.scheduler.SchedulerUtils;

@ConfigPersistent
public class AnalysisPresenter extends BasePresenter<AnalysisMvpView> {

    private final EndlessSessionManager endlessSessionManager;
    private List<Disease> diseases = new ArrayList<>(5);

    @Inject
    public AnalysisPresenter(EndlessSessionManager endlessSessionManager) {
        this.endlessSessionManager = endlessSessionManager;
    }

    @Override
    public void attachView(AnalysisMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void refreshDiseases() {
        if (getView() != null) getView().setWorking(true);

        addDisposable(endlessSessionManager.analyze()
                .compose(SchedulerUtils.ioToMain())
                .subscribe(diseases -> {
                    if (getView() == null) return;

                    this.diseases.clear();
                    this.diseases.addAll(diseases);
                    getView().updateDiseases();
                    getView().setWorking(false);
                })
        );
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

}
