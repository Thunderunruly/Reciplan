package comp5216.sydney.edu.au.group11.reciplan.ui.like;

public class LikeItem {

    private final int id;
    private final String imgURL;
    private final String recipeName;
    private final double calorieVal;
    private final String unit;

    public LikeItem(int id, String recipeName, String imgURL, double calorieVal,String unit) {
        this.id = id;
        this.recipeName = recipeName;
        this.imgURL = imgURL;
        this.calorieVal = calorieVal;
        this.unit = unit;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getUnit() {
        return unit;
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
}
