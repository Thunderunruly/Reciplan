package comp5216.sydney.edu.au.group11.reciplan.ui.like;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentLikeBinding;

public class LikeFragment extends Fragment {

    private FragmentLikeBinding binding;
    private GridView gridView;
    private ArrayList<LikeItem> items;
    private LikeAdapter likeAdapter;
    private int listSize = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        items = new ArrayList<>();
        // TODO add list from firebase
        items.add(new LikeItem(716429,"Garlicky Kale","https://spoonacular.com/recipeImages/644387-312x231.jpg",345));
        items.add(new LikeItem(416009,"Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs","https://spoonacular.com/recipeimages/716429-312x231.jpg",774));

        binding = FragmentLikeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gridView = binding.likeGrid;
        likeAdapter = new LikeAdapter(items, getContext(), (id,name,url) -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("name",name);
                bundle.putString("image",url);
                mainActivity.showDetail(LikeFragment.this, bundle);
            }
        });
        gridView.setAdapter(likeAdapter);
        checkChanges(items);
        return root;
    }

    private void checkChanges(ArrayList<LikeItem> items) {
        if(listSize == 0) {
            listSize = items.size();
            likeAdapter.notifyDataSetChanged();
        }
        else if(listSize != items.size()) {
            listSize = items.size();
            likeAdapter.notifyDataSetChanged();
        }
    }
}
