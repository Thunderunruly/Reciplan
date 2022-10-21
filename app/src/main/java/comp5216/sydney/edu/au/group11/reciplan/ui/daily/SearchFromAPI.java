package comp5216.sydney.edu.au.group11.reciplan.ui.daily;

import java.util.HashMap;
import java.util.Map;

public class SearchFromAPI {
    public static Map<String, String> statusBuilder(String status) {
        HashMap<String, String> map = new HashMap<>();
        map.put("number","1");
        switch (status) {
            case "Happy":
                map.put("minCalories","2000");
                map.put("minSugar","10");
                break;
            case "Sad":
                map.put("minCalories","2000");
                map.put("minFolicAcid","5");
                map.put("number","1");
                break;
            case "Anxiety":
                map.put("minCalories","0");
                map.put("minVitaminC","5");
                map.put("minVitaminB6","5");
                map.put("minCarbs","5");
                map.put("number","1");
                break;
            case "Insomnia":
                map.put("minCalories","0");
                map.put("minProtein","50");
                map.put("minVitaminA","5");
                map.put("minFolicAcid","5");
                map.put("minVitaminC","5");
                map.put("number","1");
                break;
            case "Exhaustion":
                map.put("minCalories","250");
                map.put("minProtein","50");
                map.put("minVitaminC","5");
                map.put("number","1");
                break;
            case "Resting":
                map.put("minCalories","0");
                map.put("number","1");
                break;
            case "Fitness":
                map.put("maxCalories","1500");
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
                map.put("maxCalories","2000");
                map.put("minVitaminC","10");
                map.put("number","1");
                break;
            case "Lose Weight":
                map.put("maxCalories","250");
                map.put("maxProtein","50");
                map.put("number","1");
                break;
            case "null":
            default:
                map.put("minCalories","0");
                break;
        }
        return map;
    }
}
