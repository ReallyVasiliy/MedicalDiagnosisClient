package us.kulakov.symptomcheck.data.model.response.endless;

import com.google.gson.annotations.SerializedName;

public class FeatureDetails {
    @SerializedName("text")
    public String text;
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
    @SerializedName("default")
    public double defaultValue;
    @SerializedName("min")
    public double min;
    @SerializedName("max")
    public double max;
    @SerializedName("step")
    public double step;
    @SerializedName("choices")
    public FeatureDetailsChoice[] choices;
}
