package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comp5216.sydney.edu.au.group11.reciplan.MyRecyclerViewAdapter;
import comp5216.sydney.edu.au.group11.reciplan.R;

public class MyDialogFragment extends DialogFragment {

    private String[] vegetableStrings = new String[]{"leek", "caraway", "spinach", "cabbage", "greens"
            , "celery", "broccoli", "lettuce", "tarragon", "carrot", "turnip", "potato"
            , "pea", "soybean", "lotus root", "green pepper", "onion"
            , "chili", "tomato", "cucumber", "pumpkin"};
    private String[] meatStrings = new String[]{"shortribs", "tenderloin", "ribs"
            , "omasum", "tripes"
            , "wing middle joint", "wing tips", "paws"
            , "goose liver", "pork", "chop", "streaky"
            , "mutton", "sausage", "fishball", "pilchard", "cod"
            , "pomfret", "beef", "veal", "chicken"};
    private RecyclerView dialogRecyclerview;
    private MyRecyclerViewAdapter recyclerViewAdapter;

    private int step = 1;
    private List<String> stringList = new ArrayList<>();
    private TextView textView;
    private ImageView imgl;
    private ImageView imgr;
    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;
    private TextView line1;
    private TextView line2;
    private TextView line3;
    private ImageView foodX;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.AnimBottom;

        View view = inflater.inflate(R.layout.fragment_search_dialog, container);

        dialogRecyclerview = view.findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new
                StaggeredGridLayoutManager(4
                , StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        // staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        dialogRecyclerview
                .setLayoutManager(staggeredGridLayoutManager);
        List<String> stringList = new ArrayList<>();
        stringList.addAll(Arrays.asList(vegetableStrings));
        recyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(),
                stringList, this, R.layout.item_text);
        dialogRecyclerview.setAdapter(recyclerViewAdapter);
        textView = view.findViewById(R.id.text);
        step1 = view.findViewById(R.id.step1);
        step2 = view.findViewById(R.id.step2);
        step3 = view.findViewById(R.id.step3);
        step4 = view.findViewById(R.id.step4);
        line1 = view.findViewById(R.id.line1);
        line2 = view.findViewById(R.id.line2);
        line3 = view.findViewById(R.id.line3);
        imgl = view.findViewById(R.id.imgl);
        foodX = view.findViewById(R.id.dialog_close);
        foodX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialogFragment.this.dismiss();
            }
        });
        imgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                }
            }
        });
        imgr = view.findViewById(R.id.imgr);
        imgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (step == 4)
                    return;
                step++;
                switch (step) {
                    case 1:
                        toVegetable();
                        break;
                    case 2:
                        toMeat();
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(),SearchActivity.class)
                                .putExtra("key", textView.getText().toString()));
                        break;
                    case 4:
                        break;
                }
            }
        });
        return view;
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
        Window win = getDialog().getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }


    public void toVegetable() {
        step2.setTextColor(getActivity().getResources().getColor(R.color.line_false));
        step3.setTextColor(getActivity().getResources().getColor(R.color.line_false));
        step4.setTextColor(getActivity().getResources().getColor(R.color.line_false));
        step2.setBackground(getActivity().getResources().getDrawable(R.drawable.food_false));
        step3.setBackground(getActivity().getResources().getDrawable(R.drawable.food_false));
        step4.setBackground(getActivity().getResources().getDrawable(R.drawable.food_false));
        line1.setBackgroundColor(getActivity().getResources().getColor(R.color.line_false));
        line2.setBackgroundColor(getActivity().getResources().getColor(R.color.line_false));
        line3.setBackgroundColor(getActivity().getResources().getColor(R.color.line_false));
        recyclerViewAdapter.clear();
        recyclerViewAdapter.addAll(Arrays.asList(vegetableStrings));
    }


    public void toMeat() {
        step2.setTextColor(getActivity().getResources().getColor(R.color.line));
        step3.setTextColor(getActivity().getResources().getColor(R.color.line_false));
        step4.setTextColor(getActivity().getResources().getColor(R.color.line_false));
        step2.setBackground(getActivity().getResources().getDrawable(R.drawable.food_true));
        step3.setBackground(getActivity().getResources().getDrawable(R.drawable.food_false));
        step4.setBackground(getActivity().getResources().getDrawable(R.drawable.food_false));
        line1.setBackgroundColor(getActivity().getResources().getColor(R.color.line));
        line2.setBackgroundColor(getActivity().getResources().getColor(R.color.line_false));
        line3.setBackgroundColor(getActivity().getResources().getColor(R.color.line_false));
        recyclerViewAdapter.clear();
        recyclerViewAdapter.addAll(Arrays.asList(meatStrings));
    }

    public void addCheck(String s) {
        if (stringList.contains(s))
            return;
        stringList.add(s);
        initText();
    }

    private void initText() {
        String s = "";
        if (stringList.size() > 0) {
            for (int i = 0; i < stringList.size(); i++) {
                s = s + (stringList.get(i) + (i == stringList.size() - 1 ? "" : ","));
            }
        }
        textView.setText(s);
    }

    public void removeCheck(String s) {
        stringList.remove(s);
        initText();
    }

    public List<String> getList() {
        return stringList;
    }

}
