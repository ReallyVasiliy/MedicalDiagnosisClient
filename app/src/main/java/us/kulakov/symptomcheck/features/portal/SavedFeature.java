package us.kulakov.symptomcheck.features.portal;

import us.kulakov.symptomcheck.data.entities.Feature;

public class SavedFeature {
    public final String value;
    public final Feature feature;

    public SavedFeature(Feature feature, String value) {
        this.value = value;
        this.feature = feature;
    }
}
