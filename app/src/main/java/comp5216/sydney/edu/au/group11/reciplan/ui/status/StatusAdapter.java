package comp5216.sydney.edu.au.group11.reciplan.ui.status;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Set;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.firebase.FireDoc;
import comp5216.sydney.edu.au.group11.reciplan.net.CallBack;

public class StatusAdapter extends BaseAdapter {
    private FireDoc fireDoc;
    private final int[] images = {R.drawable.ic_status,R.drawable.ic_happy, R.drawable.ic_sad, R.drawable.ic_anxiety,
            R.drawable.ic_insomnia, R.drawable.ic_exhaustion, R.drawable.ic_resting, R.drawable.ic_fitness,
            R.drawable.ic_working, R.drawable.ic_ill, R.drawable.ic_lose_weight};
    private final String[] texts = {"STATUS:","Happy", "Sad", "Anxiety", "Insomnia", "Exhaustion", "Resting", "Fitness",
            "Working", "Sickness", "Lose Weight"};
    private final Context context;
    public StatusAdapter(Context context) {
        this.context = context;
        fireDoc = new FireDoc(context);
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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.status_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.grid_tv);
        textView.setText(texts[position]);
        textView.setCompoundDrawablesWithIntrinsicBounds(null,
                context.getDrawable(images[position]),
                null,
                null);
        final String[] currentStatus = {null};
        fireDoc.update(new CallBack() {
            @Override
            public void onResponse(Object data) {
                currentStatus[0] = fireDoc.getStatus();
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
        if(position == 0) {
            textView.setBackgroundColor(Color.GRAY);
        }
        else if(texts[position].equals(currentStatus[0]) && position != 0) {
            textView.setBackgroundColor(Color.parseColor("#FFBB86FC"));
        }
        else {
            textView.setBackgroundColor(Color.TRANSPARENT);
        }
        textView.setOnClickListener(v -> {
            if(position != 0) {
                fireDoc.setKey("status",texts[position]);
                fireDoc.setMapById();
            }
            notifyDataSetChanged();
        });
        return convertView;
    }
}
