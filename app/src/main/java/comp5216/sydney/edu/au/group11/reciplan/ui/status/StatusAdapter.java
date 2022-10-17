package comp5216.sydney.edu.au.group11.reciplan.ui.status;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.MainActivity;
import comp5216.sydney.edu.au.group11.reciplan.R;

public class StatusAdapter extends BaseAdapter {
    private final FirebaseFirestore database;
    private final int[] images = {R.drawable.ic_status,R.drawable.ic_happy, R.drawable.ic_sad, R.drawable.ic_anxiety,
            R.drawable.ic_insomnia, R.drawable.ic_exhaustion, R.drawable.ic_resting, R.drawable.ic_fitness,
            R.drawable.ic_working, R.drawable.ic_ill, R.drawable.ic_lose_weight};
    private final String[] texts = {"STATUS:","Happy", "Sad", "Anxiety", "Insomnia", "Exhaustion", "Resting", "Fitness",
            "Working", "Sickness", "Lose Weight"};
    private final Context context;
    private Map<String, Object> keys;
    public StatusAdapter(Context context) {
        this.context = context;
        database = FirebaseFirestore.getInstance();
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
        if(MainActivity.auth.getCurrentUser() != null) {
            database.collection("reciplan").document(MainActivity.auth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            keys = (Map<String, Object>) task.getResult();
                            currentStatus[0] = task.getResult().getString("status");
                        }
                        else{
                            Toast.makeText(context, "Fail to connect", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
            if(position != 0 && MainActivity.auth.getCurrentUser() != null) {
                keys.put("status",texts[position]);
                database.collection("reciplan").document(MainActivity.auth.getCurrentUser().getUid())
                        .set(keys)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(context, "Status change saved.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "Fail to update status", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            notifyDataSetChanged();
        });
        return convertView;
    }
}
