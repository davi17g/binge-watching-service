package request;

import com.google.gson.annotations.SerializedName;

public enum RequestType {
    @SerializedName("0")
    SET_USER_ID (0),
    @SerializedName("1")
    SET_OPTION (1),
    @SerializedName("2")
    SET_CONTENT_TYPE (2),
    @SerializedName("3")
    SET_STATUS (3),
    @SerializedName("4")
    SET_RATE (4);



    private final int value;
    public int getValue() {
        return value;
    }

    private RequestType(int value) {
        this.value = value;
    }
}
