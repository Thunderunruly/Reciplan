package comp5216.sydney.edu.au.group11.reciplan.ui.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDailyBinding;

public class DailyFragment extends Fragment {
    private FragmentDailyBinding binding;

    Button dailyPlanBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDailyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dailyPlanBtn = binding.dailyPlanBtn;
        dailyPlanBtn.setOnClickListener(v -> dailyPlanBtnListener(v));
        return root;
    }

    private void dailyPlanBtnListener(View v) {
        // TODO
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
