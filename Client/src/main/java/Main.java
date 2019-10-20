import com.google.gson.Gson;
import request.Request;
import request.RequestType;
import request.ResponseType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;


public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Client client = new Client(new URI("ws://localhost:8887"));
        client.connect();
        BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
        boolean stop = false;

        while (!stop) {
            ResponseType restype = null;

            try {
                 restype = client.getType();
            } catch (InterruptedException e) {
                System.out.println(e.toString());
                continue;
            }

            RequestType reqtype = null;
            Request request = null;
            switch (restype) {
                case GET_USER_ID:
                    request = new Request(RequestType.SET_USER_ID, reader.readLine());
                    break;
                case GET_OPTION:
                    request = new Request(RequestType.SET_OPTION, reader.readLine());
                    break;
                case GET_STATUS:
                    request = new Request(RequestType.SET_STATUS, reader.readLine());
                    break;
                case GET_RATE:
                    request = new Request(RequestType.SET_RATE, reader.readLine());
                    break;
                case GET_CONTENT_TYPE:
                    request = new Request(RequestType.SET_CONTENT_TYPE, reader.readLine());
                    break;
                case NEW_CONTENT:
                    client.sendPing();
                    break;
                default:
                    System.out.println("unsupported type");
                    continue;
            }
            if (request != null) {
                Gson gson = new Gson();
                String message = gson.toJson(request);
                System.out.println(message);
                client.send(message);
            }

        }
    }
}
