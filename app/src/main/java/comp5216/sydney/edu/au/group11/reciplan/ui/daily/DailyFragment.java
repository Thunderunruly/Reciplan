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

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.type.DateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    Button dailyRefreshBtn;
    Button detailBtn;
    CheckBox likeBtn;
    ImageView imageView;
    TextView calorie;
    TextView name;
    TextView summary;
    Map<String, Object> keys = new HashMap<>();
    Map<String, Object> daily = new HashMap<>();
    private String status;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        database = FirebaseFirestore.getInstance();
        binding = FragmentDailyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imageView = binding.imageDisplay;
        dailyRefreshBtn = binding.dailyRefreshBtn;
        likeBtn = binding.dailyLikeBtn;
        detailBtn = binding.detailBtn;
        calorie = binding.calorieValue;
        name = binding.dailyHeading;
        summary = binding.dailySummary;
        if(MainActivity.auth.getCurrentUser() != null){
            database.collection("reciplan")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            keys = task.getResult().getData();
                            if(keys != null && keys.containsKey("daily")) {
                                getDaily();
                            }
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
            noRandom();
        }
        dailyRefreshBtn.setOnClickListener(this::RefreshNow);
        likeBtn.setOnClickListener(this::dailyLikeBtnListener);
        detailBtn.setOnClickListener(this::goDetailFragment);
        return root;
    }

    private void noRandom() {
        database.collection("reciplan").document("daily")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        daily = task.getResult().getData();
                        if(daily != null && daily.containsKey("timestamp")) {
                            checkTime(daily.get("timestamp"));
                            setValue();
                        }
                        else {
                            dailySearch();
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "Fail to connect.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getDaily() {
        daily = (Map<String, Object>) keys.get("daily");
        if(daily != null && daily.containsKey("timestamp")) {
            checkTime(daily.get("timestamp"));
        }
        else {
            updateToDatabase();
        }
        setValue();
    }

    private void checkTime(Object time) {
        Date current = Timestamp.now().toDate();
        Timestamp cloud = (Timestamp) time;
        Date old = cloud.toDate();
        if(dateDiff(old,current) >= 1) {
            dailySearch();
        }
    }

    private int dateDiff(Date old, Date current) {
        return (int) (current.getTime() - old.getTime()) / (1000 * 3600 * 24);
    }

    private void setValue() {
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };
        checkLike(Objects.requireNonNull(daily.get("id")).toString());
        ImageURL.requestImg(handler, (String) daily.get("image"));
        String calTxt = daily.get("calories") + " " + daily.get("unit");
        calorie.setText(calTxt);
        name.setText((String) daily.get("title"));
        summary.setText(Html.fromHtml((String) daily.get("summary"), Html.FROM_HTML_MODE_LEGACY));
        summary.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void goDetailFragment(View v) {
        if(daily != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Bundle bundle = new Bundle();
            bundle.putString("case","daily");
            bundle.putBoolean("likes", likeBtn.isSelected());
            String sid = Objects.requireNonNull(daily.get("id")).toString();
            bundle.putInt("id", Integer.parseInt(sid));
            bundle.putString("name", (String) daily.get("title"));
            bundle.putString("image", (String) daily.get("image"));
            bundle.putString("calories",daily.get("calories") + " " + daily.get("unit"));
            bundle.putString("summary", (String) daily.get("summary"));
            if(mainActivity != null) {
                mainActivity.showDetail(bundle);
            }
        }
    }

    private void dailyLikeBtnListener(View v) {
        if(!likeBtn.isSelected()) {
            if(MainActivity.auth.getCurrentUser() != null) {
                likeBtn.setSelected(true);
                addToLike();
            }
            else {
                likeBtn.setSelected(false);
                Toast.makeText(getContext(), "Please Log In to add.", Toast.LENGTH_SHORT).show();
            }
        } else {
            removeLike();
            likeBtn.setSelected(false);
        }
    }

    private void removeLike() {
        database.collection("reciplan")
                .document(Objects.requireNonNull(MainActivity.auth.getCurrentUser()).getUid())
                .collection("likes")
                .document(daily.get("id")+"")
                .delete().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "Like Removed.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Please check you connection.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addToLike() {
        if(MainActivity.auth.getCurrentUser() != null) {
            database.collection("reciplan")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("likes")
                    .document(daily.get("id") + "")
                    .set(daily)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(getContext(), "Recipe saved", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(), "Fail to add, Please check you connection.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void RefreshNow(View v) {
        dailySearch();
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
                System.out.println(DateTime.getDefaultInstance());
                daily.put("id",data.getId());
                daily.put("title",data.getTitle());
                daily.put("image",data.getImage());
                daily.put("imageType",data.getImageType());
                daily.put("calories",data.getCalories());
                daily.put("unit",data.getUnit());
                updateSummary(data.getId());
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
                daily.put("summary",data);
                updateToDatabase();
                setValue();
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    private void updateToDatabase() {
        addCurrentTime();
        if(MainActivity.auth.getCurrentUser() != null) {
            keys.put("daily",daily);
            database.collection("reciplan")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .set(keys)
                    .addOnCompleteListener(task -> {
                        if(!task.isSuccessful()){
                            Toast.makeText(getContext(), "Fail to connect to database",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            database.collection("reciplan").document("daily")
                    .set(daily)
                    .addOnCompleteListener(task -> {
                        if(!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Fail to connect.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addCurrentTime() {
        Timestamp timestamp = Timestamp.now();
        daily.put("timestamp",timestamp);
    }

    private void checkLike(String id) {
        if(MainActivity.auth.getCurrentUser() != null) {
            database.collection("reciplan")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("likes")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            likeBtn.setSelected(false);
                            likeBtn.setChecked(false);
                            for(QueryDocumentSnapshot document:task.getResult()){
                                if(document.getId().equals(id)) {
                                    likeBtn.setSelected(true);
                                    likeBtn.setChecked(true);
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
