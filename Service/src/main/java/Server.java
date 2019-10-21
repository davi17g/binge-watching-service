import java.net.InetSocketAddress;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import request.Request;
import request.RequestType;
import request.Response;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Server extends WebSocketServer {

    private Set<String> connectedUsers = null;
    private HashMap<String, Context> users = null;

    public Server(InetSocketAddress address) {
        super(address);
        connectedUsers = new HashSet<String>();
        users = new HashMap<String, Context>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Context context = new Context();
        users.put(conn.getRemoteSocketAddress().toString(), context);
        doAction(conn, new GetUserIDState());
        System.out.println("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

        Context context = users.get(conn.getRemoteSocketAddress().toString());
        Gson gson = new Gson();
        Request request = gson.fromJson(message, Request.class);
        RequestType reqtype = request.getType();
        if (RequestType.SET_USER_ID == reqtype) {
            String userid = request.getInput();
            connectedUsers.add(userid);
            doAction(conn, new GetUserOptionsState(userid));
        } else if (RequestType.SET_OPTION == reqtype) {
            char ch = request.getInput().charAt(0);
            switch (ch) {
                case 'C':
                    doAction(conn, new UserContentTypeState(context.getState()));
                    break;
                case 'H':
                    doAction(conn, new ShowUserHistory(context.getState()));
                    break;
                case 'E':
                    users.remove(conn.getRemoteSocketAddress().toString());
                    connectedUsers.remove(context.getState().getUser().getUserID());
                    conn.close(1000);
                    break;
                case 'S':
                    connectedUsers.remove(context.getState().getUser().getUserID());
                    doAction(conn, new GetUserIDState());
                    break;
                default:
                    connectedUsers.remove(context.getState().getUser().getUserID());
                    conn.closeConnection(1007, "Unsupported option");
            }
        } else if (RequestType.SET_CONTENT_TYPE == reqtype) {
            doAction(conn, new DeliverNewContentState(context.getState()));
        } else if (RequestType.SET_STATUS == reqtype) {
            char ch = request.getInput().charAt(0);
            switch (ch) {
                case 'Y':
                    doAction(conn, new UserRecommendationState(context.getState()));
                    break;
                case 'N':
                    try {
                        TimeUnit.SECONDS.sleep(10);
                        doAction(conn, context.getState());
                    } catch (InterruptedException e) {
                        System.out.println(e.toString());
                    }
                    break;
                default:
                    connectedUsers.remove(context.getState().getUser().getUserID());
                    conn.closeConnection(1007, "Unsupported option");
            }
        } else if (RequestType.SET_RATE == reqtype) {
            int rate = Integer.parseInt(request.getInput());
            context = users.get(conn.getRemoteSocketAddress().toString());
            doAction(conn, new GetUserIDState());
            User user = context.getState().getUser();
            user.setRate(rate);
            connectedUsers.remove(user.getUserID());


        } else {
            connectedUsers.remove(context.getState().getUser().getUserID());
            conn.closeConnection(1007, "Unsupported option");
        }
    }

    private void doAction(WebSocket conn, State state) {
        Context context = users.get(conn.getRemoteSocketAddress().toString());
        state.doAction(context);
        Gson gson = new Gson();
        String message = gson.toJson(new Response(context.getState().getType(), context.getState().getPrompt()));
        conn.send(message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }

    @Override
    public void onWebsocketPing(WebSocket conn, Framedata f) {
        Context context = users.get(conn.getRemoteSocketAddress().toString());
        doAction(conn, new IsUserFinished(context.getState()));
        super.onWebsocketPing(conn, f);
    }
}