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
import android.widget.Toast;

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
    private int images[] = {R.drawable.ic_happy, R.drawable.ic_sad, R.drawable.ic_anxiety, R.drawable.ic_insomnia,
            R.drawable.ic_exhaustion, R.drawable.ic_resting, R.drawable.ic_fitness, R.drawable.ic_working};
    private String texts[] = {"Happy", "Sad", "Anxiety", "Insomnia", "Exhaustion", "Resting", "Fitness",
            "Working"};
    private int itemLength = 8;
    private int clickTemp = -1;
    private int status = 0;
    private int[] clickList = new int[itemLength];

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

        for(int i = 0; i < itemLength; i++) {
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.status_item,null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) view.findViewById(R.id.grid_img);
                holder.textView = (TextView) view.findViewById(R.id.grid_tv);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.imageView.setImageResource((Integer) list.get(i).get("image"));
            holder.textView.setText((String) list.get(i).get("text"));

            if(clickTemp == i) {
                if(clickList[i] == 0 && status == 0) {
                    view.setBackgroundColor(Color.BLUE);
                    clickList[i] = 1;
                    status += 1;
                } else if(clickList[i] == 1) {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    clickList[i] = 0;
                    status -= 1;
                }
            }

            return view;
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

