package comp5216.sydney.edu.au.group11.reciplan.entity;

import java.util.List;

public class UsedIngredients {

    private String id;
    private String amount;
    private String unit;
    private String unitLong;
    private String unitShort;
    private String aisle;
    private String name;
    private String original;
    private String originalName;
    private List<String> meta;
    private String image;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getAmount() {
        return amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getUnit() {
        return unit;
    }

    public void setUnitLong(String unitLong) {
        this.unitLong = unitLong;
    }
    public String getUnitLong() {
        return unitLong;
    }

    public void setUnitShort(String unitShort) {
        this.unitShort = unitShort;
    }
    public String getUnitShort() {
        return unitShort;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }
    public String getAisle() {
        return aisle;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
    public String getOriginal() {
        return original;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
    public String getOriginalName() {
        return originalName;
    }

    public void setMeta(List<String> meta) {
        this.meta = meta;
    }
    public List<String> getMeta() {
        return meta;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
    }

}
