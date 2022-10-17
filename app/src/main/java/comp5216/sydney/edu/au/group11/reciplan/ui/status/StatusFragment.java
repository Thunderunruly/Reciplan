package comp5216.sydney.edu.au.group11.reciplan.ui.status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentStatusBinding;

public class StatusFragment extends Fragment {
    FragmentStatusBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        GridView gridView = binding.gv;
        StatusAdapter adapter = new StatusAdapter(getContext());
        gridView.setAdapter(adapter);
        return root;
    }
}

