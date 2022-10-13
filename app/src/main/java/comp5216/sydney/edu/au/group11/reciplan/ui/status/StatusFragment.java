package comp5216.sydney.edu.au.group11.reciplan.ui.status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentStatusBinding;

public class StatusFragment extends Fragment {
    private FragmentStatusBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
}
