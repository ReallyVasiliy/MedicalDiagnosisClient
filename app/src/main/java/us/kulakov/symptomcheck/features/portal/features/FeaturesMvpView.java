package us.kulakov.symptomcheck.features.portal.features;

import us.kulakov.symptomcheck.data.entities.Feature;
import us.kulakov.symptomcheck.features.base.MvpView;

public interface FeaturesMvpView extends MvpView {
    void showError();
    void updateFeatures(Feature[] features);
    void showDoubleDialog(Feature feature);
    void showIntegerDialog(Feature feature);
    void showMultiDialog(Feature feature, int selected, CharSequence[] choices);
    void updateSavedFeatures();
}
