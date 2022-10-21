package comp5216.sydney.edu.au.group11.reciplan.ui.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;

import java.util.List;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.entity.DataBean;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVAdapter;
import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.XRVHolder;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;

public class SearchRecyclerViewAdapter extends XRVAdapter<DataBean> {
    private final NavHostController controller;
    Gson gson = new Gson();
    public SearchRecyclerViewAdapter(FragmentActivity activity, Context context,
                                     List<DataBean> list, int... layoutIds) {
        super(context, list, layoutIds);
        controller = (NavHostController) Navigation.findNavController(activity,
                R.id.nav_host_fragment_activity_main);
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
        viewHolder.setOnClickListener(R.id.item_panel, v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id",item.getId());
            bundle.putString("case","search");
            bundle.putString("title",item.getTitle());
            bundle.putString("image",item.getImage());
            bundle.putString("imageType",item.getImageType());
            System.out.println(gson.toJson(item));
            bundle.putString("data",gson.toJson(item));
            controller.navigate(R.id.recipe_detail,bundle);
        });
    }
}
