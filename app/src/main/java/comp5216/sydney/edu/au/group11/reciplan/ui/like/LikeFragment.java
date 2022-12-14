package comp5216.sydney.edu.au.group11.reciplan.ui.like;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentLikeBinding;

public class LikeFragment extends Fragment {

    private ArrayList<LikeItem> items;
    private FirebaseFirestore database;
    private LikeAdapter likeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        items = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        NavHostController controller = (NavHostController) Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentLikeBinding binding = FragmentLikeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        GridView gridView = binding.likeGrid;
        if (MainActivity.auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Please Login", Toast.LENGTH_SHORT).show();
            return null;
        }
        if(MainActivity.auth.getCurrentUser() != null) {
            database.collection("reciplan").document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("likes")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            items = new ArrayList<>();
                            for(QueryDocumentSnapshot document: task.getResult()){
                                int num = Integer.parseInt(document.getId());
                                System.out.println(num);
                                String cal;
                                String unit;
                                if(document.contains("calories")) {
                                    cal = document.get("calories").toString();
                                    unit = (String) document.get("unit");
                                }
                                else if(document.contains("likes")) {
                                    cal = document.get("likes").toString();
                                    unit = "likes";
                                }
                                else if(document.contains("healthScore")) {
                                    cal = document.get("healthScore").toString();
                                    unit = "healthScore";
                                }
                                else {
                                    cal = "0";
                                    unit = "";
                                }
                                items.add(new LikeItem(num,
                                        Objects.requireNonNull(document.get("title")).toString(),
                                        Objects.requireNonNull(document.get("image")).toString(),
                                        Double.parseDouble(cal),
                                        unit));
                            }
                            likeAdapter = new LikeAdapter(items, getContext(),this, (id,name,url,cal) -> {
                                MainActivity mainActivity = (MainActivity) getActivity();
                                if (mainActivity != null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("case","likes");
                                    bundle.putBoolean("likes",true);
                                    bundle.putInt("id", id);
                                    bundle.putString("title",name);
                                    bundle.putString("image",url);
                                    bundle.putString("calories",cal);
                                    mainActivity.showDetail(bundle);
                                }
                            });
                            gridView.setAdapter(likeAdapter);
                        }
                        else{
                            Toast.makeText(getContext(), "Fail to connect to database", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return root;
    }

    public void deleteItem(int id) {
        database.collection("reciplan").document(MainActivity.auth.getCurrentUser().getUid())
                .collection("likes")
                .document(id+"")
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        likeAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Recipe Removed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Fail to delete.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
