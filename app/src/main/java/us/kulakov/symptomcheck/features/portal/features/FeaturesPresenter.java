package us.kulakov.symptomcheck.features.portal.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import us.kulakov.symptomcheck.data.EndlessSessionManager;
import us.kulakov.symptomcheck.data.entities.Feature;
import us.kulakov.symptomcheck.data.model.response.endless.FeatureDetails;
import us.kulakov.symptomcheck.features.base.BasePresenter;
import us.kulakov.symptomcheck.features.portal.SavedFeature;
import us.kulakov.symptomcheck.injection.ConfigPersistent;
import us.kulakov.symptomcheck.util.rx.scheduler.SchedulerUtils;

@ConfigPersistent
public class FeaturesPresenter extends BasePresenter<FeaturesMvpView> {

    private final EndlessSessionManager endlessSessionManager;
    private List<SavedFeature> savedFeatures = new ArrayList<>(5);

    @Inject
    public FeaturesPresenter(EndlessSessionManager endlessSessionManager) {
        this.endlessSessionManager = endlessSessionManager;
    }

    @Override
    public void attachView(FeaturesMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void getFeatures() {
        addDisposable(endlessSessionManager.getFeatures()
                .compose(SchedulerUtils.ioToMain())
                .subscribe(features -> {
                    if (getView() == null) return;

                    getView().updateFeatures(features);
                })
        );
    }

    public void selectFeature(Feature feature) {
        if (getView() == null) return;

        FeatureDetails details = feature.details;

        if ("integer".equals(details.type)) {
            getView().showIntegerDialog(feature);
        } else if ("double".equals(details.type)) {
            getView().showDoubleDialog(feature);
        } else if ("categorical".equals(details.type) && details.choices != null && details.choices.length > 0) {

            int selected = (int)details.defaultValue - 1; // -1 for 0-indexed

            if (selected < 0) return;

            String[] choices = new String[details.choices.length];

            for (int i = 0; i < details.choices.length; i++) {
                choices[i] = details.choices[i].text;
            }

            getView().showMultiDialog(feature, selected, choices);
        }
    }

    private void addToList(Feature feature, String value) {
        SavedFeature sf = new SavedFeature(feature, value);
        savedFeatures.add(sf);
        getView().updateSavedFeatures();
    }

    public void addDoubleFeature(Feature feature, double val) {
        addDisposable(endlessSessionManager.updateFeature(feature, val)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(ok -> {
                    if (getView() == null) return;
                    if (ok) addToList(feature, String.format(Locale.US, "%f", val));
                }));
    }

    public void addIntegerFeature(Feature feature, int val) {
        addDisposable(endlessSessionManager.updateFeature(feature, val)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(ok -> {
                    if (getView() == null) return;
                    if (ok) addToList(feature, String.format(Locale.US, "%d", val));
                }));
    }

    public void addMultiFeature(Feature feature, int option) {
        addDisposable(endlessSessionManager.updateFeature(feature, option)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(ok -> {
                    if (getView() == null) return;
                    if (ok && option >= 0 && option < feature.details.choices.length) {
                        addToList(feature, feature.details.choices[option].text);
                    }
                }));
    }

    public List<SavedFeature> getSavedFeatures() {
        return savedFeatures;
    }

    public void deleteSavedFeature(int position) {
        if (position < 0 || position >= savedFeatures.size()) return;

        SavedFeature toDelete = savedFeatures.get(position);

        addDisposable(endlessSessionManager.deleteFeature(toDelete.feature)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(ok -> {
                    if (getView() == null) return;
                    if (ok) {
                        int index = savedFeatures.indexOf(toDelete);
                        if (index >= 0) savedFeatures.remove(index);
                        if (getView() != null) {
                            getView().updateSavedFeatures();
                        }
                    }
                }));
    }
}
