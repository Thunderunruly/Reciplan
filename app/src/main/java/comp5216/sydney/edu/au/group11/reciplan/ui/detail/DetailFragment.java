package comp5216.sydney.edu.au.group11.reciplan.ui.detail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDetailBinding;
import comp5216.sydney.edu.au.group11.reciplan.entity.MixedIngredients;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;

public class DetailFragment extends Fragment {

    FirebaseFirestore database;
    TextView name;
    ImageView imageView;
    ImageButton back;
    CheckBox likeBtn;
    TextView cal;
    TextView summarise;
    TextView ingredients;
    TextView mixedIngredients;
    TextView method;
    private String summary;
    String caseName;
    boolean likes;
    int id;
    String calories;
    String url;
    String ingredientList;
    String imageType;
    Gson gson = new Gson();
    Map<String,Object> keys = new HashMap<>();
    Map<String, Object> normalKeys = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        database = FirebaseFirestore.getInstance();
        Bundle bundle = this.getArguments();
        summary = "Null";
        ingredientList = "Null";
        calories = "Null";
        FragmentDetailBinding binding = FragmentDetailBinding
                .inflate(inflater,container,false);
        View root = binding.getRoot();
        imageView = binding.detailRecipe.detailImg;
        likeBtn = binding.detailRecipe.detailLikeBtn;
        name = binding.detailRecipe.detailName;
        cal = binding.detailRecipe.detailCal;
        summarise = binding.detailRecipe.detailSummary;
        ingredients = binding.detailRecipe.detailIngredients;
        mixedIngredients = binding.detailRecipe.mixedIngredients;
        method = binding.detailRecipe.detailMethod;
        back = binding.backBtn;
        dealMessage(bundle);
        checkLike(id,bundle);
        back.setOnClickListener(this::BackFragment);
        likeBtn.setOnClickListener(this::addLike);
        return root;
    }

    private void addLike(View v) {
        if(likes) {
            deleteLikes();
            likes = false;
        }else {
            addToLike();
            likes = true;
        }
    }

    private void addToLike() {
        if(MainActivity.auth.getCurrentUser() != null) {
            database.collection("reciplan")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("likes")
                    .document(id+"")
                    .set(keys)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Recipe Saved.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void deleteLikes() {
        if(MainActivity.auth.getCurrentUser() != null) {
            database.collection("reciplan")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("likes")
                    .document(id+"")
                    .delete();
        }
    }

    private void dealMessage(Bundle bundle) {
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            for(String key:keys) {
                switch (key) {
                    case "case":
                        caseName = bundle.getString(key);
                        break;
                    case "likes":
                        likes = bundle.getBoolean(key);
                        break;
                    case "id":
                        System.out.println("id");
                        id = bundle.getInt(key);
                        break;
                    case "title":
                        name.setText(bundle.getString(key));
                        break;
                    case "image":
                        url = bundle.getString(key);
                        break;
                    case "imageType":
                        imageType = bundle.getString(key);
                        break;
                    case "summary":
                        summary = bundle.getString(key);
                        if (!summary.equals("Null")) {
                            summarise.setText(Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY));
                        }
                        break;
                    case "calories":
                        calories = bundle.getString("calories");
                        cal.setText(calories);
                        break;
                    default:
                        cal.setVisibility(View.INVISIBLE);
                        break;
                }
            }
            if(!calories.contains("cal")) {
                cal.setCompoundDrawables(null,null,null,null);
            }
        }
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };
        ImageURL.requestImg(handler, url);
    }

    private void checkLike(int id, Bundle bundle) {
        if(likes) {
            getFromDataBase(id);
        }
        else if(caseName.equals("daily")) {
            getFromDaily(id);
        }
        else if(caseName.equals("search")) {
            getFromSearch(bundle);
        }
        else if(caseName.equals("plan")){
            getFromPlan(bundle);
        }
    }

    private void getFromDaily(int id) {
        if(MainActivity.auth.getCurrentUser() != null) {
            database.collection("reciplan")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            normalKeys = task.getResult().getData();
                            keys = (Map<String, Object>) (normalKeys != null ? normalKeys.get("daily") : null);
                            if(keys != null) {
                                if(keys.containsKey("ingredients")) {
                                    ingredients.setText(
                                            Html.fromHtml((String) keys.get("ingredients")
                                            ,Html.FROM_HTML_MODE_LEGACY));
                                }
                                else {
                                    getIngredients(id);
                                }
                                if(keys.containsKey("method")) {
                                    String string = gson.toJson(keys.get("method"));
                                    Method methodTxt = gson.fromJson(string,Method.class);
                                    if (methodTxt != null) {
                                        method.setText(Html.fromHtml(methodTxt.toString()
                                                ,Html.FROM_HTML_MODE_LEGACY));
                                    }
                                }
                                else {
                                    getMethod(id);
                                }
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "Fail to connect",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void getFromPlan(Bundle bundle) {
        String string = bundle.getString("data");
        JsonObject jsonObject = gson.fromJson(string,JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for(Map.Entry<String,JsonElement> entry: entrySet) {
            Object obj = gson.fromJson(entry.getValue(),Object.class);
            keys.put(entry.getKey(),obj);
        }
        System.out.println(keys.toString());
        if(keys.containsKey("summary")) {
            summarise.setText(Html.fromHtml((String) keys.get("summary")
                    ,Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            doSummarySearch(id);
        }
        if(keys.containsKey("ingredients")) {
            ingredients.setText(Html.fromHtml((String) keys.get("ingredients")
                    ,Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            getIngredients(id);
        }
        if(keys.containsKey("method")) {
            String string1 = gson.toJson(keys.get("method"));
            Method methodTxt = gson.fromJson(string1,Method.class);
            if (methodTxt != null) {
                method.setText(Html.fromHtml(methodTxt.toString()
                        ,Html.FROM_HTML_MODE_LEGACY));
            }
        }
        else {
            getMethod(id);
        }
    }

    private void getFromSearch(Bundle bundle) {
        String string = bundle.getString("data");
        JsonObject jsonObject = gson.fromJson(string,JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for(Map.Entry<String,JsonElement> entry: entrySet) {
            Object obj = gson.fromJson(entry.getValue(),Object.class);
            keys.put(entry.getKey(),obj);
        }
        System.out.println(keys.toString());
        if(keys.containsKey("summary")) {
            summarise.setText(Html.fromHtml((String) keys.get("summary")
                    ,Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            doSummarySearch(id);
        }
        if(keys.containsKey("ingredients")) {
            ingredients.setText(Html.fromHtml((String) keys.get("ingredients")
                    ,Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            getIngredients(id);
        }
        if(keys.containsKey("method")) {
            String string1 = gson.toJson(keys.get("method"));
            Method methodTxt = gson.fromJson(string1,Method.class);
            if (methodTxt != null) {
                method.setText(Html.fromHtml(methodTxt.toString()
                        ,Html.FROM_HTML_MODE_LEGACY));
            }
        }
        else {
            getMethod(id);
        }
        if(keys.containsKey("likes")) {
            String likeTxt = "<b>likes</b> " + keys.get("likes");
            cal.setText(Html.fromHtml(likeTxt,Html.FROM_HTML_MODE_LEGACY));
            cal.setCompoundDrawables(null,null,null,null);
            cal.setVisibility(View.VISIBLE);
        }
        StringBuilder list = new StringBuilder();
        if(keys.containsKey("usedIngredientCount")){
            int a = Integer.parseInt(keys.get("usedIngredientCount")+"");
            if(a > 0) {
                if(keys.containsKey("usedIngredients")) {
                    list.append("<b>UsedIngredients</b>");
                    List<Object> objects = (List<Object>) keys.get("usedIngredients");
                    for(int i = 0; i < a; i++) {
                        String json = gson.toJson(objects != null ? objects.get(i) : null);
                        MixedIngredients mixedIngredients = gson.fromJson(json,MixedIngredients.class);
                        list.append("<br>").append(mixedIngredients.getOriginal());
                    }
                    list.append("<br>");
                }
            }
        }
        if(keys.containsKey("missedIngredientCount")){
            int a = Integer.parseInt(keys.get("missedIngredientCount")+"");
            if(a > 0) {
                if(keys.containsKey("missedIngredients")) {
                    list.append("<b>MissedIngredients</b>");
                    List<Object> objects = (List<Object>) keys.get("missedIngredients");
                    for(int i = 0; i < a; i++) {
                        String json = gson.toJson(objects != null ? objects.get(i) : null);
                        MixedIngredients mixedIngredients = gson.fromJson(json,MixedIngredients.class);
                        list.append("<br>").append(mixedIngredients.getOriginal());
                    }
                    list.append("<br>");
                }
            }
        }
        if(keys.containsKey("unusedIngredientCount")){
            int a = Integer.parseInt(keys.get("unusedIngredientCount")+"");
            if(a > 0) {
                if(keys.containsKey("unusedIngredients")) {
                    list.append("<b>UnusedIngredients</b>");
                    List<Object> objects = (List<Object>) keys.get("unusedIngredients");
                    for(int i = 0; i < a; i++) {
                        String json = gson.toJson(objects != null ? objects.get(i) : null);
                        MixedIngredients mixedIngredients = gson.fromJson(json,MixedIngredients.class);
                        list.append("<br>").append(mixedIngredients.getOriginal());
                    }
                    list.append("<br>");
                }
            }
        }
        if(list.length() > 0) {
            mixedIngredients.setText(Html.fromHtml(list.toString(),Html.FROM_HTML_MODE_LEGACY));
        }
    }

    private void getFromDataBase(int id) {
        if(MainActivity.auth.getCurrentUser() != null) {
            database.collection("reciplan")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("likes")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()) {
                                int sid = (int)Double.parseDouble(Objects.requireNonNull(document.get("id")).toString());
                                if(sid == id) {
                                    keys = document.getData();
                                    likeBtn.setSelected(true);
                                    likeBtn.setChecked(true);
                                    if(keys.containsKey("summary")) {
                                        summarise.setText(Html.fromHtml((String) keys.get("summary")
                                                ,Html.FROM_HTML_MODE_LEGACY));
                                    }
                                    else {
                                        doSummarySearch(id);
                                    }
                                    if(keys.containsKey("ingredients")) {
                                        ingredients.setText(Html.fromHtml((String) keys.get("ingredients")
                                                ,Html.FROM_HTML_MODE_LEGACY));
                                    }
                                    else {
                                        getIngredients(id);
                                    }
                                    if(keys.containsKey("method")) {
                                        String string = gson.toJson(keys.get("method"));
                                        Method methodTxt = gson.fromJson(string,Method.class);
                                        if (methodTxt != null) {
                                            method.setText(Html.fromHtml(methodTxt.toString()
                                                    ,Html.FROM_HTML_MODE_LEGACY));
                                        }
                                    }
                                    else {
                                        getMethod(id);
                                    }
                                }
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "Fail to connect.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void getIngredients(int id) {
        ApiBuilder builder =new ApiBuilder()
                .Url("/recipes/" + id + "/ingredientWidget.json")
                .Params("apiKey",getResources().getString(R.string.apikey));
        ApiClient.getInstance().doGet(builder, new CallBack<Ingredients>() {
            @Override
            public void onResponse(Ingredients data) {
                keys.put("ingredients",data.toString());
                if(likes) {
                    checkUpdateNow();
                }
                else if(caseName.equals("daily")) {
                    normalKeys.put("daily",keys);
                    checkUpdateNow();
                }
                // TODO
                ingredients.setText(Html.fromHtml(data.toString(),Html.FROM_HTML_MODE_LEGACY));
            }

            @Override
            public void onFail(String msg) {
                Log.d("Fail",msg);
            }
        },Ingredients.class,getContext());
    }

    private void getMethod(int id) {
        ApiBuilder builder = new ApiBuilder()
                .Url("/recipes/"+ id + "/analyzedInstructions")
                .Params("apiKey",getResources().getString(R.string.apikey));
        ApiClient.getInstance().doGet(builder, new CallBack<Method>() {
            @Override
            public void onResponse(Method data) {
                String object = gson.toJson(data);
                Map<String,Object> map = new HashMap<>();
                JsonObject jsonObject = gson.fromJson(object,JsonObject.class);
                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                for(Map.Entry<String,JsonElement> entry: entrySet) {
                    Object obj = gson.fromJson(entry.getValue(),Object.class);
                    map.put(entry.getKey(),obj);
                }
                keys.put("method",map);
                if(likes) {
                    checkUpdateNow();
                }
                else if(caseName.equals("daily")) {
                    normalKeys.put("daily",keys);
                    checkUpdateNow();
                }
//                 TODO
                method.setText(Html.fromHtml(data.toString(),Html.FROM_HTML_MODE_LEGACY));
            }

            @Override
            public void onFail(String msg) {

            }
        },Method.class,getContext());
    }

    private void doSummarySearch(int id) {
        ApiBuilder builder =new ApiBuilder()
                .Url("/recipes/" + id + "/summary")
                .Params("apiKey",getResources().getString(R.string.apikey));
        ApiClient.getInstance().summaryGet(builder, new CallBack<String>() {
            @Override
            public void onResponse(String data) {
                keys.put("summary",data);
                if(likes) {
                    checkUpdateNow();
                }
                else if(caseName.equals("daily")) {
                    normalKeys.put("daily",keys);
                    checkUpdateNow();
                }
                // TODO
                summarise.setText(Html.fromHtml(data,Html.FROM_HTML_MODE_LEGACY));
            }

            @Override
            public void onFail(String msg) {
                Log.d("Fail",msg);
            }
        });
    }

    private void checkUpdateNow() {
        if(likes) {
            if(MainActivity.auth.getCurrentUser() != null && likeBtn.isChecked()) {
                database.collection("reciplan").document(MainActivity.auth.getCurrentUser().getUid())
                        .collection("likes").document(Objects.requireNonNull(keys.get("id")).toString())
                        .set(keys)
                        .addOnCompleteListener(task -> {});
            }
        }
        else if(caseName.equals("daily")){
            if(MainActivity.auth.getCurrentUser() != null) {
                database.collection("reciplan").document(MainActivity.auth.getCurrentUser().getUid())
                        .set(normalKeys)
                        .addOnCompleteListener(task -> {});
            }
        }
    }

    private void BackFragment(View v) {
        FragmentManager manager = getParentFragmentManager();
        manager.popBackStack();
    }
}
