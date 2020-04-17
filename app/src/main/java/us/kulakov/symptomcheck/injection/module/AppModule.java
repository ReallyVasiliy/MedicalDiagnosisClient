package us.kulakov.symptomcheck.injection.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import us.kulakov.symptomcheck.data.local.supporting.AssetsJsonProvider;
import us.kulakov.symptomcheck.data.local.supporting.SupportingDataProvider;
import us.kulakov.symptomcheck.injection.ApplicationContext;

@Module(includes = {ApiModule.class})
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    SupportingDataProvider provideSupportingDataProvider(@ApplicationContext Context context) {
        return new AssetsJsonProvider(context);
    }
}
