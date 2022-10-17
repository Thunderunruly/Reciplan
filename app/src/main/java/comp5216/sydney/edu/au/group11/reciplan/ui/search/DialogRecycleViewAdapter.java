package comp5216.sydney.edu.au.group11.reciplan.ui.search;


import android.content.Context;
import android.widget.CheckBox;

import java.util.List;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVAdapter;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVHolder;

public class DialogRecycleViewAdapter extends XRVAdapter<String> {
    SearchDialogFragment myDialogFragment;
    public DialogRecycleViewAdapter(Context context, List<String> list, SearchDialogFragment myDialogFragment, int... layoutIds) {
        super(context, list, layoutIds);
        this.myDialogFragment = myDialogFragment;
    }

    @Override
    protected void onBindData(XRVHolder viewHolder, final int position, String item) {
        viewHolder.setText(R.id.item_text, item);
        CheckBox itemCheck = viewHolder.getView(R.id.item_text);
        itemCheck.setChecked(myDialogFragment.getList().contains(item));
        itemCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                myDialogFragment.addCheck(compoundButton.getText().toString());
            }else {
                myDialogFragment.removeCheck(compoundButton.getText().toString());
            }
        });
    }
}