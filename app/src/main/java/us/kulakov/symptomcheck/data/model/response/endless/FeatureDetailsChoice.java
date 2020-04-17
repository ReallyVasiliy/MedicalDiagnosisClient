package us.kulakov.symptomcheck.data.model.response.endless;

import com.google.gson.annotations.SerializedName;

public class FeatureDetailsChoice {
    @SerializedName("text")
    public String text;
    @SerializedName("value")
    public double value;
}
