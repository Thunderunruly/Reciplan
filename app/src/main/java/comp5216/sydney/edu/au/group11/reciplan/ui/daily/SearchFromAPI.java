package comp5216.sydney.edu.au.group11.reciplan.ui.daily;

import java.util.HashMap;
import java.util.Map;

public class SearchFromAPI {
    public static Map<String, String> statusBuilder(String status) {
        HashMap<String, String> map = new HashMap<>();
        map.put("number","1");
        switch (status) {
            case "Happy":
                // 快乐
                map.put("minCalories","0");
                map.put("","");
                break;
            case "Sad":
                // 伤心 碱性 钙
                map.put("minCalories","0");
                map.put("minFolicAcid","");
                map.put("number","1");
                break;
            case "Anxiety":
                // 焦虑 钙 西 碱
                map.put("minCalories","0");
                map.put("minVitaminC","");
                map.put("minVitaminB6","");
                map.put("minCarbs","");
                map.put("number","1");
                break;
            case "Insomnia":
                //
                map.put("minCalories","0");
                map.put("minProtein","50");
                map.put("minVitaminA","");
                map.put("minFolicAcid","");
                map.put("minVitaminC","");
                map.put("number","1");
                break;
            case "Exhaustion":
                //
                map.put("minCalories","250");
                map.put("minProtein","50");
                map.put("minVitaminC","");
                map.put("number","1");
                break;
            case "Resting":
                // 休息
                map.put("minCalories","0");
                map.put("number","1");
                break;
            case "Fitness":
                // 健身 钠 钾
                map.put("minCalories","0");
                map.put("","");
                map.put("minProtein","50");
                map.put("number","1");
                break;
            case "Working":
                map.put("minCalories","250");
                map.put("diet","Gluten Free");
                map.put("minFiber","6");
                map.put("number","1");
                break;
            case "Sickness":
                // 生病
                map.put("minCalories","0");
                map.put("","");
                map.put("number","1");
                break;
            case "Lose Weight":
                // 减肥
                map.put("maxCalories","250");
                map.put("maxProtein","50");
                map.put("number","1");
                break;
            default:
                map.put("minCalories","0");
                break;
        }
        return map;
    }
}
