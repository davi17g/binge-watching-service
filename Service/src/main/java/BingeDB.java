import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

public class BingeDB implements DataBase{

    private static final String databaseAddress = "localhost";
    private static final String databaseName = "BingeWatchingDB";
    private static final int databasePort = 27017;
    private static final String databaseUserName = "davi17g";
    private static final String databaseUserPassword = "1234";
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

    public void setContent(String uid, Document doc) {
//        database.getCollection("users").updateOne(Filters.eq("userid", uid));
    }
}
