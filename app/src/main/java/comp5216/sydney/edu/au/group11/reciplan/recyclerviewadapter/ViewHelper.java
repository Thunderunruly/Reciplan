package comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;


public interface ViewHelper {

    interface AbsListView<VH extends XLVHolder> {
        VH setText(int viewId, String value);
        VH setTextColor(int viewId, int color);
        VH setTextColorRes(int viewId, int colorRes);
        VH setImageResource(int viewId, int imgResId);
        VH setBackgroundColor(int viewId, int color);
        VH setBackgroundColorRes(int viewId, int colorRes);
        VH setImageDrawable(int viewId, Drawable drawable);
        VH setImageDrawableRes(int viewId, int drawableRes);
        VH setImageUrl(int viewId, String imgUrl);
        VH setImageBitmap(int viewId, Bitmap imgBitmap);
        VH setVisible(int viewId, boolean visible);
        VH setVisible(int viewId, int visible);
        VH setTag(int viewId, Object tag);
        VH setTag(int viewId, int key, Object tag);
        VH setChecked(int viewId, boolean checked);
        VH setAlpha(int viewId, float value);
        VH setTypeface(int viewId, Typeface typeface);
        VH setTypeface(Typeface typeface, int... viewIds);
        VH setOnClickListener(int viewId, View.OnClickListener listener);
    }

    interface RecyclerView<VH extends XRVHolder> {
        VH setText(int viewId, String value);
        VH setTextColor(int viewId, int color);
        VH setTextColorRes(int viewId, int colorRes);
        VH setImageResource(int viewId, int imgResId);
        VH setBackgroundColor(int viewId, int color);
        VH setBackgroundColorRes(int viewId, int colorRes);
        VH setImageDrawable(int viewId, Drawable drawable);
        VH setImageDrawableRes(int viewId, int drawableRes);
        VH setImageUrl(int viewId, String imgUrl);
        VH setImageBitmap(int viewId, Bitmap imgBitmap);
        VH setVisible(int viewId, boolean visible);
        VH setVisible(int viewId, int visible);
        VH setTag(int viewId, Object tag);
        VH setTag(int viewId, int key, Object tag);
        VH setChecked(int viewId, boolean checked);
        VH setAlpha(int viewId, float value);
        VH setTypeface(int viewId, Typeface typeface);
        VH setTypeface(Typeface typeface, int... viewIds);
        VH setOnClickListener(int viewId, View.OnClickListener listener);
    }

}
