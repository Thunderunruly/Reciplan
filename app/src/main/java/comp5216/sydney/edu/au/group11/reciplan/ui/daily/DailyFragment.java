package comp5216.sydney.edu.au.group11.reciplan.ui.daily;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDailyBinding;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;

public class DailyFragment extends Fragment {
    private FragmentDailyBinding binding;

    CheckBox dailyPlanBtn;
    Button detailBtn;
    CheckBox likeBtn;
    ImageView imageView;
    TextView calorie;
    TextView name;
    TextView summary;
    private int id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // TODO random from recipe with current status
        String path = "https://spoonacular.com/recipeimages/716429-312x231.jpg";
        int calorieValue = 0;
        id = 10001;
        String nameTxt = "Pasta";
        String summariseTxt = "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs";

        binding = FragmentDailyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imageView = binding.imageDisplay;
        dailyPlanBtn = binding.dailyPlanBtn;
        likeBtn = binding.dailyLikeBtn;
        detailBtn = binding.detailBtn;
        calorie = binding.calorieValue;
        name = binding.dailyHeading;
        summary = binding.dailySummary;
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };
        // TODO check like and plan
        ImageURL.requestImg(handler, path);
        String calTxt = calorieValue + " Cal";
        calorie.setText(calTxt);
        name.setText(nameTxt);
        summary.setText(summariseTxt);
        dailyPlanBtn.setOnClickListener(v -> dailyPlanBtnListener(v));
        likeBtn.setOnClickListener(v -> dailyLikeBtnListener(v));
        detailBtn.setOnClickListener(v -> goDetailFragment());
        return root;
    }

    private void goDetailFragment() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showDetail(DailyFragment.this, id);
    }

    private void dailyLikeBtnListener(View v) {
        // TODO
    }

    private void dailyPlanBtnListener(View v) {
        // TODO
        dailyPlanBtn.setTextColor(getResources().getColor(R.color.white));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
