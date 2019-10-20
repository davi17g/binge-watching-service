package request;

public class Request {
    private RequestType type = null;
    private String input = null;
    public Request(RequestType type, String input) {
        this.type = type;
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public RequestType getType() {
        return type;
    }
}
