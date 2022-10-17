package comp5216.sydney.edu.au.group11.reciplan.ui.like;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentLikeBinding;
import comp5216.sydney.edu.au.group11.reciplan.firebase.FireDoc;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;

public class LikeFragment extends Fragment {

    private FragmentLikeBinding binding;
    private NavHostController controller;
    private FireDoc doc;
    private GridView gridView;
    private ArrayList<LikeItem> items;
    private LikeAdapter likeAdapter;
    private int listSize = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        items = new ArrayList<>();
        doc = new FireDoc(getContext());
        controller = (NavHostController) Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        binding = FragmentLikeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gridView = binding.likeGrid;
        if (!doc.checkLogin()) {
            controller.navigate(R.id.person_log);
            return null;
        }
        likeAdapter = new LikeAdapter(items, getContext(), (id,name,url) -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("name",name);
                bundle.putString("image",url);
                mainActivity.showDetail(bundle);
            }
        });
        gridView.setAdapter(likeAdapter);
        doc.updateLikes(new CallBack<ArrayList<LikeItem>>() {
            @Override
            public void onResponse(ArrayList<LikeItem> data) {
                for(LikeItem item:data) {
                    items.add(item);
                }
                likeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
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

    public void deleteItem(int id) {
        Toast.makeText(getActivity(), id + "", Toast.LENGTH_SHORT).show();
    }
}
