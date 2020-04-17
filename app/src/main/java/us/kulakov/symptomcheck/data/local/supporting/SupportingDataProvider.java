package us.kulakov.symptomcheck.data.local.supporting;

import java.util.Map;

import us.kulakov.symptomcheck.data.model.response.endless.FeatureDetails;

public interface SupportingDataProvider {
    Map<String, FeatureDetails> getFeatureNames();
}
