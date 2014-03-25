package no.wtw.android.restserviceutils;

import com.google.gson.annotations.SerializedName;

public class RestServiceErrorObject {

    @SerializedName("message")
    private String message = "Unknown error";
    @SerializedName("code")
    private int code = -1;
    @SerializedName("subCode")
    private int subCode = -1;

    public RestServiceErrorObject(String message) {
        this.message = message;
    }

    public String getMessage() {
        if (message == null)
            return "";
        return message;
    }

    @Override
    public String toString() {
        return code + "/" + code + " " + message != null ? message : "";
    }
}
