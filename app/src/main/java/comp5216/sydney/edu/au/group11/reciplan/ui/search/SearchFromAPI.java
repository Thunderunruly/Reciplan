package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;
import comp5216.sydney.edu.au.group11.reciplan.ui.daily.DailyItem;
import comp5216.sydney.edu.au.group11.reciplan.ui.daily.Nutrition;

public class SearchFromAPI {

    public static final int DAILY_MODE = 1001;
    private final FragmentActivity activity;
    private String status;
    private Data data;
    private ApiBuilder builder;

    public SearchFromAPI(FragmentActivity activity) {
        this.activity = activity;
        this.builder = new ApiBuilder();
    }

    public void searchDailyRecipe(String status, Data data) {
        this.status = status;
        this.data = data;
        doSearch(DAILY_MODE);
    }

    private void doSearch(int mode) {
        final int[] id = new int[1];
        final String[] title = new String[1];
        final String[] image = new String[1];
        final String[] calories = new String[1];
        if(mode == DAILY_MODE) {
            builder.Url("/recipes/complexSearch")
                    .Params("apiKey","47b09f6a071f4865a43ac6a3f9329b4a");
            statusBuilder();
            ApiClient.getInstance().doGet(builder, new CallBack<DailyItem>() {
                @Override
                public void onResponse(DailyItem data) {
                    id[0] = data.getId();
                    title[0] = data.getTitle();
                    image[0] = data.getImage();
                    List<Nutrition> nutritions = data.getNutritions();
                    for(Nutrition nutrition: nutritions) {
                        if(nutrition.getName().equals("Calories")) {
                            calories[0] = nutrition.getAmount() + nutrition.getUnit();
                        }
                    }
                }

                @Override
                public void onFail(String msg) {

                }
            },DailyItem.class, activity);
            data.showDaily(id[0],title[0],image[0],calories[0]);
        }
    }

    private void statusBuilder() {
        switch (status) {
            case "Happy":
                // 快乐
                builder.Params("minCalories","0")
                        .Params("","")
                        .Params("number","1");
                break;
            case "Sad":
                // 伤心 碱性 钙
                builder.Params("minCalories","0")
                        .Params("minFolicAcid","")
                        .Params("number","1");
                break;
            case "Anxiety":
                // 焦虑 钙 西 碱
                builder.Params("minVitaminC","")
                        .Params("minVitaminB6","")
                        .Params("minCarbs","")
                        .Params("minCalories","0")
                        .Params("number","1");
                break;
            case "Insomnia":
                //
                builder.Params("minProtein","50")
                        .Params("minVitaminA","")
                        .Params("minFolicAcid","")
                        .Params("minVitaminC","")
                        .Params("minCalories","0")
                        .Params("number","1");
                break;
            case "Exhaustion":
                //
                builder.Params("minProtein","50")
                        .Params("minCalories","250")
                        .Params("minVitaminC","")
                        .Params("number","1");
                break;
            case "Resting":
                // 休息
                builder.Params("minCalories","0")
                        .Params("number","1");
                break;
            case "Fitness":
                // 健身 钠 钾
                builder.Params("minCalories","0")
                        .Params("","")
                        .Params("minProtein","50")
                        .Params("number","1");
                break;
            case "Working":
                builder.Params("diet","Gluten Free")
                        .Params("minFiber","6")
                        .Params("minCalories","250")
                        .Params("number","1");
                break;
            case "Sickness":
                // 生病
                builder.Params("minCalories","0")
                        .Params("number","1");
                break;
            case "Lose Weight":
                // 减肥
                builder.Params("minCalories","0")
                        .Params("number","1");
                break;
        }
    }

    public interface Data{
        void showDaily(int id, String name, String url, String calorie);
    }
}
