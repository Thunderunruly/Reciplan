package comp5216.sydney.edu.au.group11.reciplan.ui.like;

public class LikeItem {

    private final int id;
    private final String imgURL;
    private final String recipeName;
    private final int calorieVal;

    public LikeItem(int id, String recipeName, String imgURL, int calorieVal) {
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

    public int getCalorieVal() {
        return calorieVal;
    }

    public int getId() {
        return id;
    }
}