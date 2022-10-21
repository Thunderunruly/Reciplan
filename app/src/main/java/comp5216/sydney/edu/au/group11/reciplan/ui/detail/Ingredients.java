package comp5216.sydney.edu.au.group11.reciplan.ui.detail;

import androidx.annotation.NonNull;

import java.util.List;

public class Ingredients {
    private final IngredientList data;

    public Ingredients(IngredientList data) {
        this.data = data;
    }

    public IngredientList getData() {
        return data;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        List<Ingredient> ingredients = data.getIngredients();
        for(Ingredient ingredient:ingredients) {
            str.append(ingredient.getName()).append(": ").append(ingredient.getAmount().toString()).append("<br>");
        }
        return str.toString();
    }

    public static class IngredientList {
        private final List<Ingredient> ingredients;

        private IngredientList(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

    }
    public static class Ingredient {
        private String name;
        private final Item amount;

        private Ingredient(Item amount) {
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Item getAmount() {
            return amount;
        }

    }

    public static class Amount {
        private final double value;
        private String unit;

        private Amount(double value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public double getValue() {
            return value;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

    }

    public static class Item {
        private final Amount us;

        private Item(Amount us) {
            this.us = us;
        }

        @NonNull
        @Override
        public String toString() {
            String str = "";
            str += "<b>" + us.getValue() + "</b> " + us.getUnit();
            return str;
        }
    }
}
