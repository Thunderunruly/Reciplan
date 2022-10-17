package comp5216.sydney.edu.au.group11.reciplan.ui.status;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Set;

import comp5216.sydney.edu.au.group11.reciplan.R;

public class StatusAdapter extends BaseAdapter {
    private final int[] images = {R.drawable.ic_status,R.drawable.ic_happy, R.drawable.ic_sad, R.drawable.ic_anxiety,
            R.drawable.ic_insomnia, R.drawable.ic_exhaustion, R.drawable.ic_resting, R.drawable.ic_fitness,
            R.drawable.ic_working, R.drawable.ic_ill, R.drawable.ic_lose_weight};
    private final String[] texts = {"STATUS:","Happy", "Sad", "Anxiety", "Insomnia", "Exhaustion", "Resting", "Fitness",
            "Working", "Sickness", "Lose Weight"};
    private final HashMap<String,Boolean> map;
    private final Context context;
    private int clickPosition = 0;
    private int status = 0;
    public StatusAdapter(Context context) {
        this.context = context;
        map = new HashMap<>();
        for (String text : texts) {
            map.put(text, false);
        }
    }

    @Override
    public int getCount() {
        return texts.length;
    }

    @Override
    public Object getItem(int position) {
        return texts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public HashMap<String, Boolean> getMap() {
        return map;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.status_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.grid_tv);
        Set<String> set = map.keySet();
        textView.setText(texts[position]);
        textView.setCompoundDrawablesWithIntrinsicBounds(null,
                context.getDrawable(images[position]),
                null,
                null);
        if(clickPosition == position) {
            if(clickPosition == 0) {
                textView.setBackgroundColor(Color.GRAY);
            }
            if(!map.get(texts[position]) && status == 0 && clickPosition != 0) {
                textView.setBackgroundColor(Color.parseColor("#FFBB86FC"));
                map.replace(texts[position],true);
                status += 1;
            } else if (map.get(texts[position]) && status == 1) {
                textView.setBackgroundColor(Color.TRANSPARENT);
                map.replace(texts[position],false);
                status -= 1;
            }
        }
        textView.setOnClickListener(v -> {
            clickPosition = position;
            notifyDataSetChanged();
        });
        return convertView;
    }
}
