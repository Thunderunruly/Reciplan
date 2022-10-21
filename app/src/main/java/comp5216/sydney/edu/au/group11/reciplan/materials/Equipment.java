package comp5216.sydney.edu.au.group11.reciplan.materials;

public class Equipment {
    public int id;
    public String name;
    public String localizedName;
    public String image;

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    @Override
    public String toString() {
        return name;
    }
}
