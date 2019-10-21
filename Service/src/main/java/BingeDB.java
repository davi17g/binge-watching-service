import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class BingeDB implements DataBase{

    private static final String databaseName = "BingeWatchingDB";
    private MongoClient mongo = null;
    private MongoDatabase database = null;
    public static BingeDB instance = null;

    BingeDB() {
        mongo = MongoClients.create();
        database = mongo.getDatabase(databaseName);
    }


    public static BingeDB getInstance(){

        if (instance == null) {
            instance = new BingeDB();
        }
        return instance;
    }

    @Override
    public void setUser(String uid) {
        Document doc = new Document(
                "userid", uid
        ).append("content", Collections.emptyList());
        database.getCollection("users").insertOne(doc);
        System.out.println("set-user id");
    }

    @Override
    public String getUser(String uid) throws NoSuchElementException {
        Document doc = database.getCollection("users").find(Filters.eq("userid", uid)).first();
        if (doc == null) {
            throw new NoSuchElementException();
        }
        return doc.toJson();

    }
    @Override
    public boolean isExsists(String uid) {
        Document doc = database.getCollection("users").find(Filters.eq("userid", uid)).first();
        if (doc == null) {
            return false;
        }
        return true;
    }

    public ArrayList<Document> getHistory(String uid) {
        return database.getCollection("users").find(Filters.eq("userid", uid))
                .projection(new Document("content", 1)).into(new ArrayList<Document>());
    }

    public void setContent(String uid, Record record) {
        Document doc = new Document()
                .append("title", record.getTitle())
                .append("overview", record.getOverview())
                .append("released", record.getRelased())
                .append("rating", record.getRating());
        database.getCollection("users")
                .findOneAndUpdate(Filters.eq("userid", uid), Updates.addToSet("content", doc));

    }
}
