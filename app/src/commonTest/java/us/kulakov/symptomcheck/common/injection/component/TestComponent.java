package us.kulakov.symptomcheck.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import us.kulakov.symptomcheck.common.injection.module.ApplicationTestModule;
import us.kulakov.symptomcheck.injection.component.AppComponent;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends AppComponent {
}
