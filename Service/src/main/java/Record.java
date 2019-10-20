public class Record {
    private String title = null;
    private String overview = null;
    private String relased = null;
    private double rating = 0.0;

    public Record(String title, String overview, String relased, double rating) {
        this.title = title;
        this.overview = overview;
        this.relased = relased;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelased() {
        return relased;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return String.format("title: %s overview: %s relased: %s rating: %.2f",
                getTitle(), getOverview(), getRelased(), getRating());
    }
}
