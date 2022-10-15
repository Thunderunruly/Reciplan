package comp5216.sydney.edu.au.group11.reciplan;

import android.content.Context;

import java.util.List;

import comp5216.sydney.edu.au.group11.reciplan.entity.DataBean;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataEntity;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVAdapter;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVHolder;

public class SearchRecyclerViewAdapter extends XRVAdapter<DataBean> {
    public SearchRecyclerViewAdapter(Context context, List<DataBean> list, int... layoutIds) {
        super(context, list, layoutIds);
    }

    @Override
    protected void onBindData(XRVHolder viewHolder, final int position, DataBean item) {
        viewHolder.setImageUrl(R.id.item_img,item.getImage())
                .setText(R.id.item_title,item.getTitle())
                .setText(R.id.item_content,item.getTitle());
    }
}
