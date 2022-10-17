package comp5216.sydney.edu.au.group11.reciplan.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentCategoriesBinding;

public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentCategoriesBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        return root;
    }
}
