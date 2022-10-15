package comp5216.sydney.edu.au.group11.reciplan.ui.search;

public class SearchFromAPI {
    public static void searchDailyRecipe(String status, Data data) {
    }
    public interface Data{
        void showData(int id, String name, String url, int calorie);
    }
}
