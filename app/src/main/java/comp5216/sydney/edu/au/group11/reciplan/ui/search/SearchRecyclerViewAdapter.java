package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataBean;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVAdapter;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVHolder;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;

public class SearchRecyclerViewAdapter extends XRVAdapter<DataBean> {
    public SearchRecyclerViewAdapter(Context context, List<DataBean> list, int... layoutIds) {
        super(context, list, layoutIds);
    }

    @Override
    protected void onBindData(XRVHolder viewHolder, final int position, DataBean item) {
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                viewHolder.setImageBitmap(R.id.item_img,(Bitmap) msg.obj);
            }
        };
        ImageURL.requestImg(handler, item.getImage());
        viewHolder.setText(R.id.item_title,item.getTitle())
                .setText(R.id.item_content,item.getTitle());
    }
}
