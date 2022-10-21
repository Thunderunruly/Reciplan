package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentSearchDialogBinding;

public class SearchDialogFragment extends DialogFragment {

    private final String[] vegetableStrings = new String[]{"leek", "caraway", "spinach", "cabbage", "greens"
            , "celery", "broccoli", "lettuce", "tarragon", "carrot", "turnip", "potato"
            , "pea", "soybean", "lotus root", "green pepper", "onion"
            , "chili", "tomato", "cucumber", "pumpkin"};
    private final String[] meatStrings = new String[]{"shortribs", "tenderloin", "ribs"
            , "omasum", "tripes"
            , "wing middle joint", "wing tips", "paws"
            , "goose liver", "pork", "chop", "streaky"
            , "mutton", "sausage", "fishball", "pilchard", "cod"
            , "pomfret", "beef", "veal", "chicken"};

    private final String[] equipStrings = new String[]{"pan", "oven", "frying pan"
            , "pie form","bowl","cooking Spoon","kettle","mixer"
            ,"steamer","stockpot","wok","saucepan"};

    private final String[] condimentStrings = new String[]{"Salt", "Oli", "Vinegar"
            , "Sugar","Salad Dressing","scallions","peanut oil","Cheese"
            ,"semolina","vegetable oil","water","ketchup","Soy Sauce","Nutella","Mustard"
            ,"BBQ Sauce","Vegan Mayonnaise","Sour Cream","Honey","Wasabi"
            ,"Fish Sauce","black pepper","white pepper"};
    private DialogRecycleViewAdapter adapter;
    NavHostController controller;

    private int step = 1;
    private final List<String> stringList = new ArrayList<>();
    private TextView textView;
    private CheckBox step1, step2, step3, step4;
    private View line1, line2, line3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.AnimBottom;

        FragmentSearchDialogBinding binding = FragmentSearchDialogBinding.inflate(inflater, container, false);
        controller = (NavHostController) Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_activity_main);
        View root = binding.getRoot();
        RecyclerView dialogRecyclerview = binding.recyclerview;
        StaggeredGridLayoutManager staggeredGridLayoutManager = new
                StaggeredGridLayoutManager(4
                , StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        dialogRecyclerview
                .setLayoutManager(staggeredGridLayoutManager);
        List<String> stringList = new ArrayList<>(Arrays.asList(vegetableStrings));
        adapter = new DialogRecycleViewAdapter(getActivity(),
                stringList, this, R.layout.item_text);
        dialogRecyclerview.setAdapter(adapter);
        textView = binding.text;
        step1 = binding.stepBar.step1;
        step2 = binding.stepBar.step2;
        step3 = binding.stepBar.step3;
        step4 = binding.stepBar.step4;
        line1 = binding.stepBar.line1;
        line2 = binding.stepBar.line2;
        line3 = binding.stepBar.line3;
        ImageView imgL = binding.imgl;
        ImageView imgR = binding.imgr;
        ImageView foodX = binding.dialogClose;
        imgL.setOnClickListener(v -> goBack());
        imgR.setOnClickListener(v -> goNext());
        foodX.setOnClickListener(v -> SearchDialogFragment.this.dismiss());
        return root;
    }

    private void goNext() {
        if (step == 4) {
            Bundle bundle = new Bundle();
            bundle.putString("key",textView.getText().toString());
            this.dismiss();
            controller.navigate(R.id.navigation_search,bundle);
            return;
        }
        step++;
        switch (step) {
            case 1:
                toVegetable();
                break;
            case 2:
                toMeat();
                break;
            case 3:
                toEquipment();
                break;
            case 4:
                toCondiment();
                break;
        }
    }

    private void goBack() {
        if (step == 1)
            return;
        step--;
        switch (step) {
            case 1:
                toVegetable();
                break;
            case 2:
                toMeat();
                break;
            case 3:
                toEquipment();
                break;
            case 4:
                toCondiment();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        initText();
    }

    @Override
    public void onResume() {
        super.onResume();
        initText();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window win = Objects.requireNonNull(getDialog()).getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics dm = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    public void toVegetable() {
        setLineColor(Arrays.asList(line1,line2,line3),R.color.line_false);
        setTextColor(Collections.singletonList(step1), R.color.line);
        setTextColor(Arrays.asList(step2,step3,step4), R.color.line_false);
        adapter.clear();
        adapter.addAll(Arrays.asList(vegetableStrings));
    }

    public void toMeat() {
        setLineColor(Collections.singletonList(line1),R.color.line);
        setLineColor(Arrays.asList(line2,line3),R.color.line_false);
        setTextColor(Arrays.asList(step1,step2), R.color.line);
        setTextColor(Arrays.asList(step3,step4), R.color.line_false);
        adapter.clear();
        adapter.addAll(Arrays.asList(meatStrings));
    }

    public void toEquipment() {
        setLineColor(Collections.singletonList(line1),R.color.line);
        setLineColor(Collections.singletonList(line2),R.color.line);
        setLineColor(Collections.singletonList(line3),R.color.line_false);
        setTextColor(Arrays.asList(step1,step2,step3), R.color.line);
        setTextColor(Collections.singletonList(step4), R.color.line_false);
        adapter.clear();
        adapter.addAll(Arrays.asList(equipStrings));
    }
    public void toCondiment() {
        setLineColor(Collections.singletonList(line1),R.color.line);
        setLineColor(Collections.singletonList(line2),R.color.line);
        setLineColor(Collections.singletonList(line3),R.color.line);
        setTextColor(Arrays.asList(step1,step2,step3,step4), R.color.line);
        adapter.clear();
        adapter.addAll(Arrays.asList(condimentStrings));
    }

    private void setTextColor(List<CheckBox> checkBoxes, int p) {
        for (CheckBox view : checkBoxes) {
            view.setTextColor(requireContext().getColor(p));
            view.setDrawingCacheBackgroundColor(requireContext().getColor(p));
            view.setChecked(p == R.color.line);
        }
    }

    private void setLineColor(List<View> views, int p) {
        for(View view: views) {
            view.setBackgroundColor(requireContext().getColor(p));
        }
    }

    public void addCheck(String s) {
        if (stringList.contains(s))
            return;
        stringList.add(s);
        initText();
    }

    private void initText() {
        StringBuilder s = new StringBuilder();
        if (stringList.size() > 0) {
            for (int i = 0; i < stringList.size(); i++) {
                s.append(stringList.get(i)).append(i == stringList.size() - 1 ? "" : ",");
            }
        }
        textView.setText(s.toString());
    }

    public void removeCheck(String s) {
        stringList.remove(s);
        initText();
    }

    public List<String> getList() {
        return stringList;
    }

}
