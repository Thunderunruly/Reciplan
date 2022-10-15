package comp5216.sydney.edu.au.group11.reciplan.ui.detail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import comp5216.sydney.edu.au.group11.reciplan.databinding.FragmentDetailBinding;
import comp5216.sydney.edu.au.group11.reciplan.thread.ImageURL;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    TextView name;
    ImageView imageView;
    ImageButton back;
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        int id = 0;
        String nameTxt = null;
        String url = null;
        if (bundle != null) {
            id = bundle.getInt("id");
            nameTxt = bundle.getString("name");
            url = bundle.getString("image");
        }
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        name = binding.detailRecipe.detailName;
        imageView = binding.detailRecipe.detailImg;
        back = binding.backBtn;
        //TODO
        name.setText(nameTxt);
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };
        ImageURL.requestImg(handler, url);
        getDetail(id);
        back.setOnClickListener(v -> BackFragment());
        return root;
    }

    private void getDetail(int id) {

    }

    private void BackFragment() {
        FragmentManager manager = getParentFragmentManager();
        manager.popBackStack();
    }
}
