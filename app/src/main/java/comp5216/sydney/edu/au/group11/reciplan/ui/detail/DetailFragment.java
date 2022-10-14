package comp5216.sydney.edu.au.group11.reciplan.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDetailBinding;
import comp5216.sydney.edu.au.group11.reciplan.ui.daily.DailyFragment;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    TextView name;
    ImageButton back;
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        int id = bundle.getInt("id");
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        name = binding.detailRecipe.detailName;
        back = binding.backBtn;
        //TODO
        name.setText("id:"+id);
        back.setOnClickListener(v -> BackFragment());
        return root;
    }

    private void BackFragment() {
        FragmentManager manager = getParentFragmentManager();
        manager.popBackStack();
    }
}
