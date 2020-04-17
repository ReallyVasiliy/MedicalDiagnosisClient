package us.kulakov.symptomcheck.data.local.supporting;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;
import us.kulakov.symptomcheck.data.model.response.endless.FeatureDetails;
import us.kulakov.symptomcheck.injection.ApplicationContext;

public class AssetsJsonProvider implements SupportingDataProvider {

    private final Context context;

    @Inject
    public AssetsJsonProvider(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Map<String, FeatureDetails> getFeatureNames() {

        Map<String, FeatureDetails> result = new HashMap<>();

        try (Reader reader = read("Features.json")) {

            Gson gson = new Gson();
            FeatureDetails[] details = gson.fromJson(reader, FeatureDetails[].class);

            for (FeatureDetails detail: details) {
                result.put(detail.name, detail);
            }

        } catch (IOException e) {
            Timber.e(e, "Error opening json file");
        }

        return result;
    }

    private Reader read(String file) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream ims = assetManager.open(file);
        return new InputStreamReader(ims);
    }
}
