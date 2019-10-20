package request;

public class Response {
    private ResponseType type = null;
    private String output = null;

    public Response(ResponseType type, String output) {
        this.type = type;
        this.output = output;
    }

    public ResponseType getType() {
        return type;
    }

    public String getOutput() {
        return output;
    }
}
