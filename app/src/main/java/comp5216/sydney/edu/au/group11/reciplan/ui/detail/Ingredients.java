package comp5216.sydney.edu.au.group11.reciplan.ui.detail;

import java.util.List;

public class Ingredients {
    private IngredientList data;

    @Override
    public String toString() {
        String str = "";
        List<Ingredient> ingredients = data.getIngredients();
        for(Ingredient ingredient:ingredients) {
            str += ingredient.getName() + ": " + ingredient.getAmount().toString() + "<br><br>";
        }
        return str;
    }

    public IngredientList getIngredients() {
        return data;
    }

    public void setIngredients(IngredientList data) {
        this.data = data;
    }

    private class IngredientList {
        private List<Ingredient> ingredients;

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }
    }
    private class Ingredient {
        private String name;
        private Item amount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Item getAmount() {
            return amount;
        }

        public void setAmount(Item amount) {
            this.amount = amount;
        }
    }

    private class Amount {
        private double value;
        private String unit;

        public String getUnit() {
            return unit;
        }

        public double getValue() {
            return value;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }

    private class Item {
        private Amount metric;
        private Amount us;

        @Override
        public String toString() {
            String str = "";
            str += "<b>" + us.getValue() + "</b> " + us.getUnit();
            return str;
        }

        public Amount getMetric() {
            return metric;
        }

        public Amount getUs() {
            return us;
        }

        public void setMetric(Amount metric) {
            this.metric = metric;
        }

        public void setUs(Amount us) {
            this.us = us;
        }
    }
}
