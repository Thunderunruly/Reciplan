package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import android.content.Context;


import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataBean;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataEntity;
import comp5216.sydney.edu.au.group11.reciplan.entity.UsedIngredientsBean;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XLVAdapter;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XLVHolder;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVAdapter;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVHolder;

import java.util.List;

public class DetailAdapter extends XLVAdapter<UsedIngredientsBean> {
    public DetailAdapter(Context context, List<UsedIngredientsBean> list, int... layoutIds) {
        super(context, list, layoutIds);
    }

    @Override
    public void convert(XLVHolder holder, int position, UsedIngredientsBean usedIngredientsBean) {
        holder.setImageUrl(R.id.item_img,usedIngredientsBean.getImage())
                .setText(R.id.item_title,usedIngredientsBean.getName())
                .setText(R.id.item_content,usedIngredientsBean.getOriginal());
    }

}
