package us.kulakov.symptomcheck.data.model.response.endless;

import com.google.gson.annotations.SerializedName;

public class GetFeaturesResponse {
    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public String[] data;

}
