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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Set;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDetailBinding;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;

public class DetailFragment extends Fragment {

    TextView name;
    ImageView imageView;
    ImageButton back;
    TextView cal;
    TextView summarise;
    TextView ingredients;
    private String summary;

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
        comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imageView = binding.detailRecipe.detailImg;
        name = binding.detailRecipe.detailName;
        cal = binding.detailRecipe.detailCal;
        summarise = binding.detailRecipe.detailSummary;
        ingredients = binding.detailRecipe.detailIngredients;
        back = binding.backBtn;
        name.setText(nameTxt);
        cal.setText(calories);
        summarise.setText(Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY));
        ingredients.setText(Html.fromHtml(ingredientList,Html.FROM_HTML_MODE_LEGACY));
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };
        ImageURL.requestImg(handler, url);
        getDetail(id);
        back.setOnClickListener(v -> BackFragment());
        return root;
    }

    private void getDetail(int id) {
        if(checkLike(id)) {
            if(summary.equals("Null")) {
                doSummarySearch(id);
            }
            getIngredients(id);
        }
        else {
            readFromDatabase(id);
        }
    }

    private void readFromDatabase(int id) {
        // TODO
    }

    private boolean checkLike(int id) {
        // TODO
        return true;
    }

    private void getIngredients(int id) {
        ApiBuilder builder =new ApiBuilder()
                .Url("/recipes/" + id + "/ingredientWidget.json")
                .Params("apiKey",getResources().getString(R.string.apikey));
        ApiClient.getInstance().doGet(builder, new CallBack<Ingredients>() {
            @Override
            public void onResponse(Ingredients data) {
                ingredients.setText(Html.fromHtml(data.toString(),Html.FROM_HTML_MODE_LEGACY));
            }

            @Override
            public void onFail(String msg) {
                Log.d("Fail",msg);
            }
        },Ingredients.class,getContext());
    }

    private void doSummarySearch(int id) {
        ApiBuilder builder =new ApiBuilder()
                .Url("/recipes/" + id + "/summary")
                .Params("apiKey",getResources().getString(R.string.apikey));
        ApiClient.getInstance().summaryGet(builder, new CallBack<String>() {
            @Override
            public void onResponse(String data) {
                summarise.setText(Html.fromHtml(data,Html.FROM_HTML_MODE_LEGACY));
            }

            @Override
            public void onFail(String msg) {
                Log.d("Fail",msg);
            }
        });
    }

    private void BackFragment() {
        FragmentManager manager = getParentFragmentManager();
        manager.popBackStack();
    }
}
