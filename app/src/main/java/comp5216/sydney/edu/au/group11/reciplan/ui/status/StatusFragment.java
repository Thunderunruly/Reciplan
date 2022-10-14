package comp5216.sydney.edu.au.group11.reciplan.ui.status;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp5216.sydney.edu.au.group11.reciplan.R;
import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentStatusBinding;

public class StatusFragment extends Fragment {
    private FragmentStatusBinding binding;
    private GridView gridView;
    private List<Map<String, Object>> list;
    private int images[] = {R.drawable.ic_status,R.drawable.ic_happy, R.drawable.ic_sad, R.drawable.ic_anxiety,
            R.drawable.ic_insomnia, R.drawable.ic_exhaustion, R.drawable.ic_resting, R.drawable.ic_fitness,
            R.drawable.ic_working, R.drawable.ic_ill, R.drawable.ic_lose_weight};
    private String texts[] = {"STATUS:","Happy", "Sad", "Anxiety", "Insomnia", "Exhaustion", "Resting", "Fitness",
            "Working", "Sickness", "Lose Weight"};
    private int clickTemp = 0;
    private int status = 0;
    private int[] clickList = new int[11];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gridView = binding.gv;
        list = new ArrayList<Map<String,Object>>();
        for(int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("image", images[i]);
            map.put("text", texts[i]);
            list.add(map);
        }

        for(int i = 0; i < clickList.length; i++) {
            clickList[i] = 0;
        }

        MyAdapter adapter = new MyAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelection(i);
                adapter.notifyDataSetChanged();

            }
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
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.status_item,null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.grid_img);
                holder.textView = (TextView) convertView.findViewById(R.id.grid_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imageView.setImageResource((Integer) list.get(position).get("image"));
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

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}

