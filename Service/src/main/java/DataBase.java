public interface DataBase {

    void setUser(String uid);
    boolean isExsists(String uid);
    String getUser(String uid);
}
