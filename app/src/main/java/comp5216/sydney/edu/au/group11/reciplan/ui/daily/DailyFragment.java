package comp5216.sydney.edu.au.group11.reciplan.ui.daily;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDailyBinding;

public class DailyFragment extends Fragment {
    private FragmentDailyBinding binding;

    Button dailyPlanBtn;
    ImageButton likeBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDailyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dailyPlanBtn = binding.dailyPlanBtn;
        likeBtn = binding.dailyLikeBtn;
        dailyPlanBtn.setOnClickListener(v -> dailyPlanBtnListener(v));
        likeBtn.setOnClickListener(v -> dailyLikeBtnListener(v));
        return root;
    }

    private void dailyLikeBtnListener(View v) {
        // TODO
        likeBtn.setBackgroundResource(R.drawable.ic_like_fill);
    }

    private void dailyPlanBtnListener(View v) {
        // TODO
        dailyPlanBtn.setBackgroundResource(R.drawable.btn_choose);
        dailyPlanBtn.setTextColor(getResources().getColor(R.color.white));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
