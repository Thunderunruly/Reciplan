package comp5216.sydney.edu.au.group11.reciplan.materials;

import java.util.ArrayList;

public class Step {
    public int number;
    public String step;
    public ArrayList<Ingredient> ingredients;
    public ArrayList<Equipment> equipment;
    public Length length;

    public int getNumber() {
        return number;
    }

    public String showEquipment() {
        String string = "";
        for(Equipment e:equipment) {
            if(e != null) {
                string += "Equipment: " +  e.getName() + "<br>";
            }
        }
        return string;
    }

    public String showIngredients() {
        String string = "";
        for(Ingredient ingredient:ingredients) {
            string += ingredient.getName() + "  ";
        }
        return string;
    }

    public String getStep() {
        return step;
    }

    public Length getLength() {
        return length;
    }
}
