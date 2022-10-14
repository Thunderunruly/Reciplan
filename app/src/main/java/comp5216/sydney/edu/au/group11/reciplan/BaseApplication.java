package comp5216.sydney.edu.au.group11.reciplan;

import android.app.Application;

import com.bumptech.glide.Glide;

import comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter.AdapterImageLoader;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AdapterImageLoader.init((context, url, view) -> Glide.with(context)
                .load(url)
                .into(view));
    }
}
