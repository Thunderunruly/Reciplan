package comp5216.sydney.edu.au.group11.reciplan.ui.daily;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDailyBinding;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;

public class DailyFragment extends Fragment {
    private FragmentDailyBinding binding;
    private FirebaseFirestore database;

    CheckBox dailyPlanBtn;
    Button detailBtn;
    CheckBox likeBtn;
    ImageView imageView;
    TextView calorie;
    TextView name;
    TextView summary;
    private String status;
    private int id;
    private String nameTxt;
    private String path;
    private String summariseTxt;
    private String calorieValue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        database = FirebaseFirestore.getInstance();
        binding = FragmentDailyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imageView = binding.imageDisplay;
        dailyPlanBtn = binding.dailyPlanBtn;
        likeBtn = binding.dailyLikeBtn;
        detailBtn = binding.detailBtn;
        calorie = binding.calorieValue;
        name = binding.dailyHeading;
        summary = binding.dailySummary;
        if(MainActivity.auth.getCurrentUser() != null){
            database.collection("reciplan").document(MainActivity.auth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Map<String,Object> daily = (Map<String, Object>) task.getResult().get("daily");
                            if(daily != null){}
                            else {
                                dailySearch();
                            }
                        }
                        else{
                            Toast.makeText(mainActivity, "Fail to connect to database", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            dailySearch();
        }
        dailyPlanBtn.setOnClickListener(this::dailyPlanBtnListener);
        likeBtn.setOnClickListener(this::dailyLikeBtnListener);
        detailBtn.setOnClickListener(this::goDetailFragment);
        return root;
    }

    private void setValue() {
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };
        // TODO check like and plan
        ImageURL.requestImg(handler, path);
        String calTxt = calorieValue;
        calorie.setText(calTxt);
        name.setText(nameTxt);
        summary.setText(Html.fromHtml(summariseTxt, Html.FROM_HTML_MODE_LEGACY));
        summary.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void goDetailFragment(View v) {
        MainActivity mainActivity = (MainActivity) getActivity();
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("name",nameTxt);
        bundle.putString("image",path);
        bundle.putString("calories",calorieValue);
        bundle.putString("summary",summariseTxt);
        if (mainActivity != null) {
            mainActivity.showDetail(bundle);
        }
    }

    private void dailyLikeBtnListener(View v) {
    }

    private void dailyPlanBtnListener(View v) {
        // TODO
        if(dailyPlanBtn.isChecked()) {
            dailyPlanBtn.setTextColor(requireActivity().getColor(R.color.white));
        }
        else {
            dailyPlanBtn.setTextColor(requireActivity().getColor(R.color.default_color));
        }
    }

    private void dailySearch() {
        if(status == null) {
            status = "null";
        }
        ApiBuilder builder =new ApiBuilder()
                .Url("/recipes/complexSearch")
                .Params("sort","random")
                .Params(SearchFromAPI.statusBuilder(status))
                .Params("apiKey",getResources().getString(R.string.apikey));
        ApiClient.getInstance().dailyGet(builder, new CallBack<DailyItem>() {
            @Override
            public void onResponse(DailyItem data) {
                id = data.getId();
                nameTxt = data.getTitle();
                path = data.getImage();
                calorieValue = data.getCalories() + " " + data.getUnit();
                updateSummary(id);
                checkLike();
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    private void updateSummary(int id) {
        ApiBuilder builder =new ApiBuilder()
                .Url("/recipes/" + id + "/summary")
                .Params("apiKey",getResources().getString(R.string.apikey));
        ApiClient.getInstance().summaryGet(builder, new CallBack<String>() {
            @Override
            public void onResponse(String data) {
                summariseTxt = data;
                updateToDatabase();
                setValue();
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    private void updateToDatabase() {
        // TODO
    }

    private void checkLike() {
        if(MainActivity.auth.getCurrentUser() != null) {
            database.collection("reciplan").document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("likes")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document:task.getResult()){
                                if(id ==(int) document.get("id")) {
                                    likeBtn.setSelected(true);
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
