package us.kulakov.symptomcheck.data.entities;

import us.kulakov.symptomcheck.data.model.response.endless.FeatureDetails;

public class Feature {
    public final String code;
    public final String friendly;
    public final FeatureDetails details;

    public Feature(FeatureDetails details) {
        this.details = details;
        this.code = details.name;
        this.friendly = details.text;
    }

    @Override
    public String toString() {
        return friendly;
    }
}
