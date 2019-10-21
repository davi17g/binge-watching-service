import org.bson.Document;
import java.util.ArrayList;

public interface DataBase {

    void setUser(String uid);
    boolean isExsists(String uid);
    String getUser(String uid);
    ArrayList<Document> getHistory(String uid);
    void setContent(String uid, Record record);
}
