package comp5216.sydney.edu.au.group11.reciplan.entity;

import java.util.List;

public class DataEntity {

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    private List<DataBean> data;

    public static class DataBean {
        public String getId() {
            return id;
        }

        public void setId(String id) {
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

        private String id;
        private String title;
        private String image;
        private String imageType;
        private String usedIngredientCount;
        private String missedIngredientCount;
        private List<MissedIngredientsBean> missedIngredients;
        private List<UsedIngredientsBean> usedIngredients;
        private List<UnusedIngredientsBean> unusedIngredients;
        private String likes;

        public static class MissedIngredientsBean {
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getUnitLong() {
                return unitLong;
            }

            public void setUnitLong(String unitLong) {
                this.unitLong = unitLong;
            }

            public String getUnitShort() {
                return unitShort;
            }

            public void setUnitShort(String unitShort) {
                this.unitShort = unitShort;
            }

            public String getAisle() {
                return aisle;
            }

            public void setAisle(String aisle) {
                this.aisle = aisle;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }

            public String getOriginalName() {
                return originalName;
            }

            public void setOriginalName(String originalName) {
                this.originalName = originalName;
            }

            public List<String> getMeta() {
                return meta;
            }

            public void setMeta(List<String> meta) {
                this.meta = meta;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getExtendedName() {
                return extendedName;
            }

            public void setExtendedName(String extendedName) {
                this.extendedName = extendedName;
            }

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
            private String extendedName;
        }

        public static class UsedIngredientsBean {
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getUnitLong() {
                return unitLong;
            }

            public void setUnitLong(String unitLong) {
                this.unitLong = unitLong;
            }

            public String getUnitShort() {
                return unitShort;
            }

            public void setUnitShort(String unitShort) {
                this.unitShort = unitShort;
            }

            public String getAisle() {
                return aisle;
            }

            public void setAisle(String aisle) {
                this.aisle = aisle;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }

            public String getOriginalName() {
                return originalName;
            }

            public void setOriginalName(String originalName) {
                this.originalName = originalName;
            }

            public List<String> getMeta() {
                return meta;
            }

            public void setMeta(List<String> meta) {
                this.meta = meta;
            }

            public String getExtendedName() {
                return extendedName;
            }

            public void setExtendedName(String extendedName) {
                this.extendedName = extendedName;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

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
            private String extendedName;
            private String image;
        }

        public static class UnusedIngredientsBean {
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getUnitLong() {
                return unitLong;
            }

            public void setUnitLong(String unitLong) {
                this.unitLong = unitLong;
            }

            public String getUnitShort() {
                return unitShort;
            }

            public void setUnitShort(String unitShort) {
                this.unitShort = unitShort;
            }

            public String getAisle() {
                return aisle;
            }

            public void setAisle(String aisle) {
                this.aisle = aisle;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }

            public String getOriginalName() {
                return originalName;
            }

            public void setOriginalName(String originalName) {
                this.originalName = originalName;
            }

            public List<String> getMeta() {
                return meta;
            }

            public void setMeta(List<String> meta) {
                this.meta = meta;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

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
        }
    }
}
