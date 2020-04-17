package us.kulakov.symptomcheck.features.terms;

import us.kulakov.symptomcheck.features.base.MvpView;

public interface TermsMvpView extends MvpView {
    void showError();
    void showTerms();
    void showNext();
}
