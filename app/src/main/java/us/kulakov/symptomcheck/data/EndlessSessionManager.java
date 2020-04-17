package us.kulakov.symptomcheck.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;
import us.kulakov.symptomcheck.data.entities.Disease;
import us.kulakov.symptomcheck.data.local.supporting.SupportingDataProvider;
import us.kulakov.symptomcheck.data.model.response.endless.FeatureDetails;
import us.kulakov.symptomcheck.data.remote.EndlessService;
import us.kulakov.symptomcheck.data.entities.Feature;
import us.kulakov.symptomcheck.util.rx.scheduler.SchedulerUtils;

@Singleton
public class EndlessSessionManager {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String PASSPHRASE = "I have read, understood and I accept and agree to comply with the Terms of Use of EndlessMedicalAPI and Endless Medical services. The Terms of Use are available on endlessmedical.com";

    @NonNull
    private EndlessService service;
    private SupportingDataProvider supportingDataProvider;
    @Nullable
    private String currentSessionID = "CPGvYypNitCmJykJ";

    @Inject
    public EndlessSessionManager(@NonNull EndlessService service, SupportingDataProvider supportingDataProvider) {
        this.service = service;
        this.supportingDataProvider = supportingDataProvider;
    }

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    private void onSessionIDChanged(@NonNull String newSessionID) {
        currentSessionID = newSessionID;
    }

    private void onSessionError() {
        currentSessionID = null;
    }

    public Single<Boolean> newSession() {
        return Single.create(emitter -> {
            addDisposable(service.initSession()
                    .compose(SchedulerUtils.ioToMain())
                    .subscribe(response -> {
                                if ("ok".equals(response.status)) {
                                    onSessionIDChanged(response.sessionID);
                                    emitter.onSuccess(true);
                                } else {
                                    onSessionError();
                                    emitter.onSuccess(false);
                                }
                            },
                            throwable -> {
                                Timber.e(throwable, "Error starting session");
                                onSessionError();
                                emitter.onSuccess(false);
                            }));

        });
    }

    public Single<Boolean> acceptTerms() {
        return Single.create(emitter -> {
            if (currentSessionID == null) {
                emitter.onError(new Exception("Invalid session ID"));
                return;
            }

            addDisposable(service.acceptTerms(currentSessionID, PASSPHRASE)
                    .compose(SchedulerUtils.ioToMain())
                    .subscribe(response -> {
                                if ("ok".equals(response.status)) {
                                    emitter.onSuccess(true);
                                } else {
                                    emitter.onSuccess(false);
                                }
                            },
                            throwable -> {
                                Timber.e(throwable, "Error accepting terms");
                                emitter.onSuccess(false);
                            }));

        });
    }

    public Single<List<Disease>> analyze() {
        if (currentSessionID == null) return Single.just(new ArrayList<>());

        return service.analyze(currentSessionID, 10)
                .map(analysisResponse -> {
                    if (!"ok".equals(analysisResponse.status) || analysisResponse.diseases == null) {
                        return new ArrayList<Disease>();
                    }

                    int resultCount = analysisResponse.diseases.length;

                    List<Disease> diseases = new ArrayList<>(resultCount);

                    for (Map<String, String> disease: analysisResponse.diseases) {
                        if (disease.size() > 0) {
                            Map.Entry<String,String> first = disease.entrySet().iterator().next();
                            diseases.add(new Disease(first.getKey(), Double.parseDouble(first.getValue())));
                        }
                    }
                    return diseases;
                })
                .onErrorReturnItem(new ArrayList<>());
    }

    public Single<Feature[]> getFeatures() {
        Map<String, FeatureDetails> featureDetails = supportingDataProvider.getFeatureNames();

        return service.getFeatures()
                .map(getFeaturesResponse -> {
                    String[] data = getFeaturesResponse.data;
                    if (!"ok".equals(getFeaturesResponse.status) || data == null) {
                        return new Feature[0];
                    }

                    List<Feature> res = new ArrayList<>(data.length);
                    for (String datum : data) {
                        FeatureDetails details = featureDetails.get(datum);

                        if (details == null) continue;

                        res.add(new Feature(details));
                    }

                    return res.toArray(new Feature[0]);
                })
                .onErrorReturnItem(new Feature[0]);
    }

    public Single<Boolean> updateFeature(Feature feature, double value) {
        return updateFeature(feature, String.format(Locale.US, "%f", value));
    }

    public Single<Boolean> updateFeature(Feature feature, int value) {
        return updateFeature(feature, String.format(Locale.US, "%d", value));
    }

    private Single<Boolean> updateFeature(Feature feature, String value) {

        if (currentSessionID == null) return Single.just(false);

        return service.updateFeature(currentSessionID, feature.code, value)
                .map(updateResponse -> "ok".equals(updateResponse.status))
                .onErrorReturnItem(false);
    }

    public Single<Boolean> deleteFeature(Feature feature) {

        if (currentSessionID == null) return Single.just(false);

        return service.deleteFeature(currentSessionID, feature.code)
                .map(updateResponse -> "ok".equals(updateResponse.status))
                .onErrorReturnItem(false);
    }
}
