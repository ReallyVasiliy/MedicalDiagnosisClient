package us.kulakov.symptomcheck.injection.component;

import dagger.Subcomponent;
import us.kulakov.symptomcheck.features.portal.analysis.AnalysisFragment;
import us.kulakov.symptomcheck.features.portal.features.FeaturesFragment;
import us.kulakov.symptomcheck.injection.PerFragment;
import us.kulakov.symptomcheck.injection.module.FragmentModule;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(FeaturesFragment fragment);
    void inject(AnalysisFragment fragment);
}
