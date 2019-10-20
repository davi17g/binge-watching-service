import java.net.URI;
import java.util.concurrent.Semaphore;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import request.Response;
import request.ResponseType;

public class Client extends WebSocketClient{

    private Semaphore semaphore = new Semaphore(0);
    private ResponseType status = null;
    public Client(URI serverURI) {
        super( serverURI );
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println( "opened connection" );
    }

    @Override
    public void onMessage(String message) {
        Gson gson = new Gson();
        Response response = gson.fromJson(message, Response.class);
        status = response.getType();
        semaphore.release();
        System.out.println(response.getOutput());
    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
    }

    @Override
    public void onError( Exception ex ) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }

    public ResponseType getType() throws InterruptedException {
        semaphore.acquire();
        ResponseType type = status;
        return type;
    }

}
