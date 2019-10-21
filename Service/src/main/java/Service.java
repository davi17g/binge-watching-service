import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;

public class Service {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8887;
        WebSocketServer server = new Server(new InetSocketAddress(host, port));
        server.run();
    }
    }