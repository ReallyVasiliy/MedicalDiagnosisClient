package us.kulakov.symptomcheck.injection.component;

import dagger.Subcomponent;
import us.kulakov.symptomcheck.features.portal.PortalActivity;
import us.kulakov.symptomcheck.features.terms.TermsActivity;
import us.kulakov.symptomcheck.injection.PerActivity;
import us.kulakov.symptomcheck.injection.module.ActivityModule;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(TermsActivity termsActivity);

    void inject(PortalActivity portalActivity);
}
