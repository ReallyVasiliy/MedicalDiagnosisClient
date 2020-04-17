package us.kulakov.symptomcheck.features.portal.analysis;

import us.kulakov.symptomcheck.features.base.MvpView;

public interface AnalysisMvpView extends MvpView {
    void showError();
    void setWorking(boolean isWorking);
    void updateDiseases();
}
