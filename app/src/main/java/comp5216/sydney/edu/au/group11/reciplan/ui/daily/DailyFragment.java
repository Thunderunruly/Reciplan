package comp5216.sydney.edu.au.group11.reciplan.ui.daily;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.TimeZone;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDailyBinding;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiBuilder;
import comp5216.sydney.edu.au.group11.reciplan.net.ApiClient;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;
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
    private String status;
    private int id;
    private String nameTxt;
    private String path;
    private String summariseTxt;
    private String calorieValue;
    private TextView time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        status = mainActivity.getStatus();
        TimeThread timeThread = new TimeThread();
        timeThread.start();
        id = 1001;
        nameTxt = "Pasta";
        path = "https://spoonacular.com/recipeimages/716429-312x231.jpg";
        summariseTxt = "recipe summary";
        calorieValue = "0 kcal";
        binding = FragmentDailyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imageView = binding.imageDisplay;
        dailyPlanBtn = binding.dailyPlanBtn;
        likeBtn = binding.dailyLikeBtn;
        detailBtn = binding.detailBtn;
        calorie = binding.calorieValue;
        name = binding.dailyHeading;
        summary = binding.dailySummary;
        setValue();
        dailyPlanBtn.setOnClickListener(this::dailyPlanBtnListener);
        likeBtn.setOnClickListener(this::dailyLikeBtnListener);
        detailBtn.setOnClickListener(this::goDetailFragment);
        time = (TextView) binding.tvTime;
        if(isNextDay()) {
            dailySearch();
        }
        else {
            // TODO  get from firebase
        }
        return root;
    }

    private boolean isNextDay() {
        String currentTime = (String) time.getText();
        // TODO
        return false;
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
            mainActivity.showDetail(DailyFragment.this, bundle);
        }
    }

    private void dailyLikeBtnListener(View v) {
        // TODO
    }

    private void dailyPlanBtnListener(View v) {
        // TODO
        if(dailyPlanBtn.isChecked()) {
            dailyPlanBtn.setTextColor(getActivity().getColor(R.color.white));
        }
        else {
            dailyPlanBtn.setTextColor(getActivity().getColor(R.color.default_color));
        }
    }

    private void dailySearch() {
        ApiBuilder builder =new ApiBuilder()
                .Url("/recipes/complexSearch")
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
    }

    private void checkLike() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    String now = (String) time.getText();
                    // TODO with firebase
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    time.setText(getTime());
                    break;
                default:
                    break;
            }
        }
    };

    public String getTime() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+11:00"));
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));
        String mSecond = String.valueOf(c.get(Calendar.SECOND));
        if("1".equals(mDay)) {
            mDay = "Sunday";
        } else if ("2".equals(mDay)) {
            mDay = "Monday";
        } else if ("3".equals(mDay)) {
            mDay = "Tuesday";
        } else if ("4".equals(mDay)) {
            mDay = "Wednesday";
        } else if ("5".equals(mDay)) {
            mDay = "Thursday";
        } else if ("6".equals(mDay)) {
            mDay = "Friday";
        } else if ("7".equals(mDay)) {
            mDay = "Saturday";
        }
        return mDay + " " + mHour + ":" + mMinute + ":" + mSecond;
    }
}
