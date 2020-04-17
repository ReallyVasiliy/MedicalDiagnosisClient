package us.kulakov.symptomcheck.common.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import us.kulakov.symptomcheck.data.EndlessSessionManager;
import us.kulakov.symptomcheck.data.remote.EndlessService;
import us.kulakov.symptomcheck.injection.ApplicationContext;

import static org.mockito.Mockito.mock;

/**
 * Provides application-level dependencies for an app running on a testing environment This allows
 * injecting mocks if necessary.
 */
@Module
public class ApplicationTestModule {
    private final Application application;

    public ApplicationTestModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    /**
     * ********** MOCKS ***********
     */
    @Provides
    @Singleton
    EndlessSessionManager providesSessionManager() {
        return mock(EndlessSessionManager.class);
    }

    @Provides
    @Singleton
    EndlessService provideEndlessService() {
        return mock(EndlessService.class);
    }
}
