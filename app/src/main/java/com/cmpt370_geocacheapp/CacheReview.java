public class CacheReview extends CacheComment {
    private int rating;
    public CacheReview(String commentBody, User author, long reviewID, int rating) {
        super(commentBody, author, reviewID);
        this.rating = rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return this.rating;
    }

    public static void main(String[] args) {
        //TODO: Make testing for CacheReview

    }
}
