package comp5216.sydney.edu.au.group11.reciplan.ui.plan;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentPlanBinding;
import comp5216.sydney.edu.au.group11.reciplan.materials.RandomRecipeApiResponse;

public class PlanFragment extends Fragment {

    private FragmentPlanBinding binding;
    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipesAdapter randomRecipesAdapter;
    RecyclerView recyclerView;
    Spinner spinner;
    String minProtein = "20";
    String maxCalories = "500";
    ImageButton refreshButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentPlanBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        if(MainActivity.auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Please Login to see the plan", Toast.LENGTH_SHORT).show();
            return null;
        }
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading..");

        spinner = binding.spinnerTags;
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.tags,
                R.layout.spinner_inner_text
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);
        manager = new RequestManager(getContext());
        refreshButton = binding.refresh;
        refreshButton.setOnClickListener(refreshButtonListener);
        return root;
    }
    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = binding.recyclerRandom;
            recyclerView .setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            randomRecipesAdapter = new RandomRecipesAdapter(getContext(), response.recipes);

            recyclerView.setAdapter(randomRecipesAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(adapterView.getSelectedItem().toString().equals("Muscle Build")){
                minProtein = "50";
                maxCalories = "1000";
                manager.getRandomRecipes(randomRecipeResponseListener, minProtein, maxCalories);
                dialog.show();
            }else if(adapterView.getSelectedItem().toString().equals("Lose Weight")){
                minProtein = "30";
                maxCalories = "500";
                manager.getRandomRecipes(randomRecipeResponseListener, minProtein, maxCalories);
                dialog.show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    private final View.OnClickListener refreshButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            manager.getRandomRecipes(randomRecipeResponseListener, minProtein, maxCalories);
        }
    };
}
