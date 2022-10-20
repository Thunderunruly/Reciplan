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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDetailBinding;
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
    TextView method;
    private String summary;
    String caseName;
    boolean likes;
    Map<String,Object> keys = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        int id = 0;
        String nameTxt = "Recipe Name";
        String url = "https://spoonacular.com/recipeimages/716429-312x231.jpg";
        summary = "Null";
        String ingredientList = "Null";
        String calories = "0 kcal";
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            for(String key:keys) {
                if(key.equals("case")){
                    caseName = bundle.getString(key);
                }
                if(key.equals("likes")){
                    likes = bundle.getBoolean(key);
                }
                if(key.equals("id")) {
                    id = bundle.getInt(key);
                }
                if(key.equals("name")) {
                    nameTxt = bundle.getString(key);
                }
                if(key.equals("image")) {
                    url = bundle.getString(key);
                }
                if(key.equals("summary")) {
                    summary = bundle.getString(key);
                }
                if(key.equals("calories")) {
                    calories = bundle.getString(key);
                }
            }
        }
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        View root = binding.getRoot();
        imageView = binding.detailRecipe.detailImg;
        likeBtn = binding.detailRecipe.detailLikeBtn;
        name = binding.detailRecipe.detailName;
        cal = binding.detailRecipe.detailCal;
        summarise = binding.detailRecipe.detailSummary;
        ingredients = binding.detailRecipe.detailIngredients;
        method = binding.detailRecipe.detailMethod;
        back = binding.backBtn;
        name.setText(nameTxt);
        cal.setText(calories);
        if(!summary.equals("Null")) {
            summarise.setText(Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY));
        }
        ingredients.setText(Html.fromHtml(ingredientList,Html.FROM_HTML_MODE_LEGACY));
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };
        ImageURL.requestImg(handler, url);
        checkLike(id);
        back.setOnClickListener(v -> BackFragment());
        return root;
    }

    private void checkLike(int id) {
        if(likes) {
            getFromDataBase(id);
        }
        else {
            getFromSearch(id);
        }
    }

    private void getFromSearch(int id) {
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
                                if(Integer.parseInt(document.get("id").toString()) == id) {
                                    keys = document.getData();
                                    likeBtn.setSelected(true);
                                    likeBtn.setChecked(true);
                                    if(keys.containsKey("summary")) {
                                        summarise.setText(Html.fromHtml((String) keys.get("summary"),Html.FROM_HTML_MODE_LEGACY));
                                    }
                                    else {
                                        doSummarySearch(id);
                                    }
                                    if(keys.containsKey("ingredients")) {
                                        ingredients.setText(Html.fromHtml((String) keys.get("ingredients"),Html.FROM_HTML_MODE_LEGACY));
                                    }
                                    else {
                                        getIngredients(id);
                                    }
                                    if(keys.containsKey("method")) {
                                        method.setText(Html.fromHtml((String) keys.get("method"),Html.FROM_HTML_MODE_LEGACY));
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
                keys.put("ingredients",data);
                ingredients.setText(Html.fromHtml(data.toString(),Html.FROM_HTML_MODE_LEGACY));
                checkUpdate(id);
            }

            @Override
            public void onFail(String msg) {
                Log.d("Fail",msg);
            }
        },Ingredients.class,getContext());
    }

    private void getMethod(int id) {
        // TODO
    }

    private void doSummarySearch(int id) {
        ApiBuilder builder =new ApiBuilder()
                .Url("/recipes/" + id + "/summary")
                .Params("apiKey",getResources().getString(R.string.apikey));
        ApiClient.getInstance().summaryGet(builder, new CallBack<String>() {
            @Override
            public void onResponse(String data) {
                keys.put("summary",data);
                summarise.setText(Html.fromHtml(data,Html.FROM_HTML_MODE_LEGACY));
                checkUpdate(id);
            }

            @Override
            public void onFail(String msg) {
                Log.d("Fail",msg);
            }
        });
    }

    private void checkUpdate(int id) {
        if(MainActivity.auth.getCurrentUser() != null && likeBtn.isChecked()) {
            database.collection("reciplan").document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("likes").document(id+"")
                    .set(keys).addOnCompleteListener(task -> {
                        if(!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Fail to connect.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void BackFragment() {
        FragmentManager manager = getParentFragmentManager();
        manager.popBackStack();
    }
}
