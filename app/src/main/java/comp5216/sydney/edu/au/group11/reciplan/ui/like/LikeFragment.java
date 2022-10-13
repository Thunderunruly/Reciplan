package comp5216.sydney.edu.au.group11.reciplan.ui.like;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentLikeBinding;

public class LikeFragment extends Fragment {

    private FragmentLikeBinding binding;
    private GridView gridView;
    private ArrayList<LikeItem> items;
    private LikeAdapter likeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        items = new ArrayList<>();
        items.add(new LikeItem(716429,"Pasta","https://spoonacular.com/recipeimages/716429-312x231.jpg",345));
        items.add(new LikeItem(716429,"Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs","https://spoonacular.com/recipeimages/716429-312x231.jpg",774));
        binding = FragmentLikeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gridView = binding.likeGrid;
        likeAdapter = new LikeAdapter(items, getContext());
        gridView.setAdapter(likeAdapter);
        return root;
    }
}
