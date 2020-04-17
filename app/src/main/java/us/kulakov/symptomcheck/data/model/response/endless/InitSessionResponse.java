package us.kulakov.symptomcheck.data.model.response.endless;

import com.google.gson.annotations.SerializedName;

public class InitSessionResponse {
    @SerializedName("status")
    public String status;
    @SerializedName("SessionID")
    public String sessionID;
}
