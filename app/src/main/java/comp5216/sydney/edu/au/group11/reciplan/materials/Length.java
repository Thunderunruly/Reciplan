package comp5216.sydney.edu.au.group11.reciplan.materials;

public class Length {
    public int number;
    public String unit;

    public int getNumber() {
        return number;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return number + " " + unit;
    }
}
