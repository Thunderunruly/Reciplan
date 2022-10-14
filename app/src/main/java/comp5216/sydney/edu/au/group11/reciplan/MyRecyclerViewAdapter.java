package comp5216.sydney.edu.au.group11.reciplan;


import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVAdapter;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVHolder;
import comp5216.sydney.edu.au.group11.reciplan.ui.search.MyDialogFragment;

public class MyRecyclerViewAdapter extends XRVAdapter<String> {
    MyDialogFragment myDialogFragment;
    public MyRecyclerViewAdapter(Context context, List<String> list,MyDialogFragment myDialogFragment, int... layoutIds) {
        super(context, list, layoutIds);
        this.myDialogFragment = myDialogFragment;
    }

    @Override
    protected void onBindData(XRVHolder viewHolder, final int position, String item) {
        viewHolder.setText(R.id.item_text, item);
        CheckBox itemCheck = viewHolder.getView(R.id.item_text);
        if (myDialogFragment.getList().contains(item))
            itemCheck.setChecked(true);
        else
            itemCheck.setChecked(false);
        itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    myDialogFragment.addCheck(compoundButton.getText().toString());
                }else {
                    myDialogFragment.removeCheck(compoundButton.getText().toString());
                }
            }
        });
    }
}