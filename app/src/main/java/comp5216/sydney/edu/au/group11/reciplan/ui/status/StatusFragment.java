package comp5216.sydney.edu.au.group11.reciplan.ui.status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentStatusBinding;
import comp5216.sydney.edu.au.group11.reciplan.firebase.FireDoc;

public class StatusFragment extends Fragment {
    FragmentStatusBinding binding;
    FireDoc doc;
    NavHostController controller;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        doc = new FireDoc(getContext());
        controller = (NavHostController) Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        if(!doc.checkLogin()) {
            controller.navigate(R.id.person_log);
        }
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        GridView gridView = binding.gv;
        StatusAdapter adapter = new StatusAdapter(getContext());
        gridView.setAdapter(adapter);
        return root;
    }
}

