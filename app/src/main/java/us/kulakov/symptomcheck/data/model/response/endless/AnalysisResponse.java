package us.kulakov.symptomcheck.data.model.response.endless;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class AnalysisResponse {
    @SerializedName("status")
    public String status;
    @SerializedName("Diseases")
    public Map<String, String>[] diseases;
}
