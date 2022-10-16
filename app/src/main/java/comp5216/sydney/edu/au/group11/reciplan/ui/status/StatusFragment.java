package comp5216.sydney.edu.au.group11.reciplan.ui.status;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentStatusBinding;

public class StatusFragment extends Fragment {
    FragmentStatusBinding binding;
    private List<Map<String, Object>> list;
    private final int[] images = {R.drawable.ic_status,R.drawable.ic_happy, R.drawable.ic_sad, R.drawable.ic_anxiety,
            R.drawable.ic_insomnia, R.drawable.ic_exhaustion, R.drawable.ic_resting, R.drawable.ic_fitness,
            R.drawable.ic_working, R.drawable.ic_ill, R.drawable.ic_lose_weight};
    private final String[] texts = {"STATUS:","Happy", "Sad", "Anxiety", "Insomnia", "Exhaustion", "Resting", "Fitness",
            "Working", "Sickness", "Lose Weight"};
    private int clickTemp = 0;
    private int status = 0;
    private final int[] clickList = new int[11];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        GridView gridView = binding.gv;
        list = new ArrayList<>();
        for(int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", images[i]);
            map.put("text", texts[i]);
            list.add(map);
        }

        Arrays.fill(clickList, 0);

        MyAdapter adapter = new MyAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            adapter.setSelection(i);
            adapter.notifyDataSetChanged();

        });
        return root;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.status_item, binding.getRoot());
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.grid_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getContext().getDrawable((Integer) list.get(position).get("image")),
                    null,
                    null);
            holder.textView.setText((String) list.get(position).get("text"));

            if(clickTemp == position) {
                if(clickTemp == 0) {
                    convertView.setBackgroundColor(Color.GRAY);
                }
                if(clickList[position] == 0 && status == 0 && clickTemp!= 0) {
                    convertView.setBackgroundColor(Color.parseColor("#FFBB86FC"));
                    clickList[position] = 1;
                    status += 1;
                } else if(clickList[position] == 1 && status == 1) {
                    convertView.setBackgroundColor(Color.TRANSPARENT);
                    clickList[position] = 0;
                    status -= 1;
                }
            }

            return convertView;
        }

        public void setSelection(int i) {
            clickTemp = i;
        }
    }

    static class ViewHolder {
        TextView textView;
    }
}

