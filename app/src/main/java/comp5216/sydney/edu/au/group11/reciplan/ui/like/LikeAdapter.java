package comp5216.sydney.edu.au.group11.reciplan.ui.like;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;

public class LikeAdapter extends BaseAdapter {

    private final ArrayList<LikeItem> items;
    private final Context context;
    private final LikeFragment fragment;
    private final Item item;

    public LikeAdapter(ArrayList<LikeItem> items, Context context,
                       LikeFragment fragment, Item item) {
        this.items = items;
        this.context = context;
        this.fragment = fragment;
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
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_recipe,parent, false);
        }
        ConstraintLayout layout = convertView.findViewById(R.id.item_show);
        ImageView imageView = convertView.findViewById(R.id.item_img);
        TextView name = convertView.findViewById(R.id.item_name);
        TextView subText = convertView.findViewById(R.id.item_calorie);
        ImageButton delete = convertView.findViewById(R.id.item_delete);
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };
        ImageURL.requestImg(handler, items.get(position).getImgURL());
        String nameTxt = items.get(position).getRecipeName();
        name.setText(nameTxt);
        if(items.get(position).getUnit().equals("likes")) {
            int l = (int) items.get(position).getCalorieVal();
            String like = "likes " + l;
            subText.setText(like);
            subText.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(context,R.drawable.likes_text_10),
                    null, null, null);
        }
        else if(items.get(position).getUnit().equals("healthScore")) {
            int l = (int) items.get(position).getCalorieVal();
            String score = "Health Score: " + l;
            subText.setText(score);
            subText.setCompoundDrawablesWithIntrinsicBounds(null,null,
                    ContextCompat.getDrawable(context,R.drawable.score_text_10),
                null);
        }
        else {
            String calTxt = items.get(position).getCalorieVal() + " "
                    + items.get(position).getUnit();
            subText.setText(calTxt);
        }
        delete.setOnClickListener(v -> deleteItem(position));
        layout.setOnClickListener(v -> goDetailFragment(position));
        return convertView;
    }

    private void goDetailFragment(int position) {
        int id = items.get(position).getId();
        String name = items.get(position).getRecipeName();
        String url = items.get(position).getImgURL();
        String cal = items.get(position).getCalorieVal() + " " + items.get(position).getUnit();
        item.showDetail(id,name,url,cal);
    }

    public interface Item {
        void showDetail(int id, String name, String url,String cal);
    }

    private void deleteItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.setTitle("Warning: ")
                .setMessage("Do you want to delete the recipe '"
                        + items.get(position).getRecipeName() + "'.")
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .setPositiveButton(R.string.ok, (dialog, which) ->
                        fragment.deleteItem(items.get(position).getId()))
                .create();
        alertDialog.show();
    }
}
