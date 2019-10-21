public class Record {
    private String title = null;
    private String overview = null;
    private String released = null;
    private double rating = 0.0;

    public Record(String title, String overview, String released, double rating) {
        this.title = title;
        this.overview = overview;
        this.released = released;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelased() {
        return released;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s \noverview: %s \nrelased: %s \nrating: %.2f\n",
                getTitle(), getOverview(), getRelased(), getRating());
    }
}