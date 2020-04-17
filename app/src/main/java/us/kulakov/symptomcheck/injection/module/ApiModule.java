package us.kulakov.symptomcheck.injection.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import us.kulakov.symptomcheck.data.remote.EndlessService;
import retrofit2.Retrofit;

@Module(includes = {NetworkModule.class})
public class ApiModule {
    @Provides
    @Singleton
    EndlessService endlessApi(@Named("Retrofit_Endless") Retrofit retrofit) {
        return retrofit.create(EndlessService.class);
    }
}
