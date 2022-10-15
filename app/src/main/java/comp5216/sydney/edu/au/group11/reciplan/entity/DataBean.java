package comp5216.sydney.edu.au.group11.reciplan.entity;

import java.util.List;

public class DataBean {

    private int id;
    private String title;
    private String image;
    private String imageType;
    private String usedIngredientCount;
    private String missedIngredientCount;
    private List<MissedIngredientsBean> missedIngredients;
    private List<UsedIngredientsBean> usedIngredients;
    private List<UnusedIngredientsBean> unusedIngredients;
    private String likes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getUsedIngredientCount() {
        return usedIngredientCount;
    }

    public void setUsedIngredientCount(String usedIngredientCount) {
        this.usedIngredientCount = usedIngredientCount;
    }

    public String getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public void setMissedIngredientCount(String missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public List<MissedIngredientsBean> getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(List<MissedIngredientsBean> missedIngredients) {
        this.missedIngredients = missedIngredients;
    }

    public List<UsedIngredientsBean> getUsedIngredients() {
        return usedIngredients;
    }

    public void setUsedIngredients(List<UsedIngredientsBean> usedIngredients) {
        this.usedIngredients = usedIngredients;
    }

    public List<UnusedIngredientsBean> getUnusedIngredients() {
        return unusedIngredients;
    }

    public void setUnusedIngredients(List<UnusedIngredientsBean> unusedIngredients) {
        this.unusedIngredients = unusedIngredients;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}