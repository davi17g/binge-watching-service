package request;
import com.google.gson.annotations.SerializedName;

public enum ResponseType {
    @SerializedName("0")
    GET_USER_ID (0),
    @SerializedName("1")
    GET_OPTION (1),
    @SerializedName("2")
    GET_CONTENT_TYPE (2),
    @SerializedName("3")
    GET_STATUS (3),
    @SerializedName("4")
    GET_RATE (4),
    @SerializedName("5")
    NEW_CONTENT (5);

    private final int value;
    public int getValue() {
        return value;
    }

    private ResponseType(int value) {
        this.value = value;
    }
}
