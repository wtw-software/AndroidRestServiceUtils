package no.wtw.android.restserviceutils;

import com.google.gson.annotations.SerializedName;

public class RestServiceErrorObject {

    @SerializedName("message")
    private String message = "Unknown error";
    @SerializedName("code")
    private int code = -1;
    @SerializedName("subCode")
    private int subCode = -1;

}
