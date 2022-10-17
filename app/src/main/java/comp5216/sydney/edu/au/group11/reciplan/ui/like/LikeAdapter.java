package comp5216.sydney.edu.au.group11.reciplan.ui.like;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;

public class LikeAdapter extends BaseAdapter {

    private final ArrayList<LikeItem> items;
    private final Context context;
    private final Item item;

    public LikeAdapter(ArrayList<LikeItem> items, Context context, Item item) {
        this.items = items;
        this.context = context;
        this.item = item;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        }
        ConstraintLayout layout = convertView.findViewById(R.id.item_show);
        ImageView imageView = convertView.findViewById(R.id.item_img);
        TextView name = convertView.findViewById(R.id.item_name);
        TextView calorie = convertView.findViewById(R.id.item_calorie);
        ImageButton delete = convertView.findViewById(R.id.item_delete);
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };
        Log.d("image",items.get(position).getImgURL());
        ImageURL.requestImg(handler, items.get(position).getImgURL());
        String nameTxt = items.get(position).getRecipeName();
        String calTxt = items.get(position).getCalorieVal() + " Cal";
        name.setText(nameTxt);
        calorie.setText(calTxt);
        delete.setOnClickListener(v -> deleteItem(position));
        layout.setOnClickListener(v -> goDetailFragment(position));
        return convertView;
    }

    private void goDetailFragment(int position) {
        int id = items.get(position).getId();
        String name = items.get(position).getRecipeName();
        String url = items.get(position).getImgURL();
        item.showDetail(id,name,url);
    }

    public interface Item {
        void showDetail(int id, String name, String url);
    }

    private void deleteItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.setTitle("Warning: ")
                .setMessage("Do you want to delete the recipe '" + items.get(position).getRecipeName() + "'.")
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    LikeFragment likeFragment = new LikeFragment();
                    likeFragment.deleteItem(items.get(position).getId());
                })
                .create();
        alertDialog.show();
    }
}
