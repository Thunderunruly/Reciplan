package comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter;

import android.content.Context;
import android.widget.ImageView;

public class AdapterImageLoader {

    public static ImageLoader sImageLoader;

    public static void init(ImageLoader loader) {
        sImageLoader = loader;
    }

    public interface ImageLoader {

        void loadImage(Context context, String url, ImageView view);
    }
}
