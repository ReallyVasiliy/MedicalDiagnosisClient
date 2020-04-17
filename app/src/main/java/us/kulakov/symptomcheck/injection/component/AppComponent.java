package us.kulakov.symptomcheck.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import us.kulakov.symptomcheck.data.EndlessSessionManager;
import us.kulakov.symptomcheck.injection.ApplicationContext;
import us.kulakov.symptomcheck.injection.module.AppModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ApplicationContext
    Context context();

    Application application();

    EndlessSessionManager endlessSessionManager();
}
