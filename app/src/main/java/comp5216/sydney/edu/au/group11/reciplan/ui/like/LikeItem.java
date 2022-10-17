package comp5216.sydney.edu.au.group11.reciplan.ui.like;

public class LikeItem {

    private final String doc;
    private final int id;
    private final String imgURL;
    private final String recipeName;
    private final double calorieVal;

    public LikeItem(String doc, int id, String recipeName, String imgURL, double calorieVal) {
        this.doc = doc;
        this.id = id;
        this.recipeName = recipeName;
        this.imgURL = imgURL;
        this.calorieVal = calorieVal;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getImgURL() {
        return imgURL;
    }

    public double getCalorieVal() {
        return calorieVal;
    }

    public int getId() {
        return id;
    }

    public String getDoc() {
        return doc;
    }
}
