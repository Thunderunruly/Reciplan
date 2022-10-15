package comp5216.sydney.edu.au.group11.reciplan.ui.detail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;
import java.util.Set;

import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDetailBinding;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    TextView name;
    ImageView imageView;
    ImageButton back;
    TextView cal;
    TextView summarise;
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        int id = 0;
        String nameTxt = "Recipe Name";
        String url = "https://spoonacular.com/recipeimages/716429-312x231.jpg";
        String summary = "Null";
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
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        name = binding.detailRecipe.detailName;
        cal = binding.detailRecipe.detailCal;
        summarise = binding.detailRecipe.detailSummary;
        imageView = binding.detailRecipe.detailImg;
        back = binding.backBtn;
        //TODO
        name.setText(nameTxt);
        cal.setText(calories);
        summarise.setText(Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY));
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

    }

    private void BackFragment() {
        FragmentManager manager = getParentFragmentManager();
        manager.popBackStack();
    }
}
