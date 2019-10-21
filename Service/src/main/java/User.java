import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.bson.Document;

public class User {
    private static final String address = "https://api.reelgood.com/v1/roulette/netflix?nocache=true&amp;content_kind=both&amp;availability=onAnySource";
    private ArrayList<Record> history = null;
    private Set<String> watched = null;
    private String userID = null;
    private DataBase database = null;

    User(String userID) {
        this.userID = userID;
        this.history = new ArrayList<Record>();
        this.watched = new HashSet<String>();
        database = BingeDB.getInstance();
        if (!database.isExsists(this.userID)) {
            database.setUser(this.userID);
        }
        else {
            ArrayList<Document> docs = database.getHistory(this.userID);
            Gson gson = new Gson();
            for (Document doc : docs) {
                ArrayList<Document> contents = (ArrayList<Document>)doc.get("content");
                for (Document content : contents) {
                    Record record = gson.fromJson(content.toJson(), Record.class);
                    history.add(record);
                    watched.add(record.getTitle());
                }
            }
        }

    }

    String getUserID() {
        return this.userID;
    }

    private Record getNewContentHelper() {
        Record rc = null;
        String body = "";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(address))
                .build();
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.toString());
        }
        if (body.length() != 0) {
            GsonBuilder builder = new GsonBuilder();
            Object o = builder.create().fromJson(body, Object.class);
            LinkedTreeMap m =(LinkedTreeMap) o;
            rc = new Record((String)m.get("title"), (String)m.get("overview"), (String)m.get("released_on"), (double)m.get("imdb_rating"));

        }
        return rc;
    }

    Record getNewContent() {
        Record record = getNewContentHelper();
        while (watched.contains(record.getTitle())) {
            record = getNewContentHelper();
        }
        watched.add(record.getTitle());
        history.add(record);
        database.setContent(this.userID, record);
        return record;
    }
    String getHistory() {
        return history.toString();
    }
    void setRate(int rate) {

    }
}
