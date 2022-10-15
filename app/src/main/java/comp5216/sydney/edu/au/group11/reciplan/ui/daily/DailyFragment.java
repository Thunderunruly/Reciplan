package comp5216.sydney.edu.au.group11.reciplan.ui.daily;

import android.annotation.SuppressLint;
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

import java.util.Calendar;
import java.util.TimeZone;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDailyBinding;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;
import comp5216.sydney.edu.au.group11.reciplan.ui.search.SearchFromAPI;

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
    private String nameTxt;
    private String path;
    private String summariseTxt = " TODO ";
    private String calorieValue;
    private TextView time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // TODO random from recipe with current status
        String status = "Working";
//        boolean isOver0Clock = false;
//        TimeThread timeThread = new TimeThread();
//        timeThread.start();
//        if(isOver0Clock) {
//        }
        SearchFromAPI searchFromAPI = new SearchFromAPI(getActivity());
        searchFromAPI.searchDailyRecipe(status, (id, name, url, calorie) -> {
            this.id = id;
            this.nameTxt = name;
            this.path = url;
            this.calorieValue = calorie;
        });

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
        String calTxt = calorieValue;
        calorie.setText(calTxt);
        name.setText(nameTxt);
        summary.setText(summariseTxt);
        dailyPlanBtn.setOnClickListener(v -> dailyPlanBtnListener(v));
        likeBtn.setOnClickListener(v -> dailyLikeBtnListener(v));
        detailBtn.setOnClickListener(v -> goDetailFragment());
        time = (TextView) binding.tvTime;
        return root;
    }

    private void goDetailFragment() {
        MainActivity mainActivity = (MainActivity) getActivity();
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("name",nameTxt);
        bundle.putString("image",path);
        mainActivity.showDetail(DailyFragment.this, bundle);
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

    public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
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
