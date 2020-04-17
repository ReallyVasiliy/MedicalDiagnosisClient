package us.kulakov.symptomcheck.data.remote;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import us.kulakov.symptomcheck.data.model.response.endless.AnalysisResponse;
import us.kulakov.symptomcheck.data.model.response.endless.BasicSuccessResponse;
import us.kulakov.symptomcheck.data.model.response.endless.GetFeaturesResponse;
import us.kulakov.symptomcheck.data.model.response.endless.InitSessionResponse;

public interface EndlessService {

    @GET("InitSession")
    Single<InitSessionResponse> initSession();

    @POST("AcceptTermsOfUse")
    Single<BasicSuccessResponse> acceptTerms(@Query("SessionID") String sessionID,
                                             @Query("passphrase") String passphrase);

    @GET("GetFeatures")
    Single<GetFeaturesResponse> getFeatures();

    @POST("UpdateFeature")
    Single<BasicSuccessResponse> updateFeature(@Query("SessionID") String sessionID,
                                               @Query("name") String name,
                                               @Query("value") String value);
    @POST("DeleteFeature")
    Single<BasicSuccessResponse> deleteFeature(@Query("SessionID") String sessionID,
                                               @Query("name") String name);

    @GET("Analyze")
    Single<AnalysisResponse> analyze(@Query("SessionID") String sessionID,
                                     @Query("NumberOfResults") int numberOfResults);
}
