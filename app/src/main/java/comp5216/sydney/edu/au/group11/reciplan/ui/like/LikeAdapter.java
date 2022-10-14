package comp5216.sydney.edu.au.group11.reciplan.ui.like;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import comp5216.sydney.edu.au.group11.reciplan.R;

public class LikeAdapter extends BaseAdapter {

    private final ArrayList<LikeItem> items;
    private final Context context;

    public LikeAdapter(ArrayList<LikeItem> items, Context context) {
        this.items = items;
        this.context = context;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_img);
        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        TextView calorie = (TextView) convertView.findViewById(R.id.item_calorie);
        ImageButton delete = (ImageButton) convertView.findViewById(R.id.item_delete);
        // TODO bitmap url new thread
        Bitmap bitmap = Bitmap.createBitmap(312, 231, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor("#129575"));
        String nameTxt = items.get(position).getRecipeName();
        String calTxt = items.get(position).getCalorieVal() + " Cal";
        imageView.setImageBitmap(bitmap);
        name.setText(nameTxt);
        calorie.setText(calTxt);
        delete.setOnClickListener(v -> deleteItem(position));
        return convertView;
    }

    private void deleteItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog1 = builder.setTitle("Warning: ")
                .setMessage("Do you want to delete the recipe '" + items.get(position).getRecipeName() + "'.")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .setPositiveButton("OK", (dialog, which) -> {
                    // TODO
                })
                .create();
        dialog1.show();
    }
}
