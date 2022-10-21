package comp5216.sydney.edu.au.group11.reciplan.recyclerviewadapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

import comp5216.sydney.edu.au.group11.reciplan.R;
public abstract class XRVAdapter<T> extends RecyclerView.Adapter<XRVHolder> implements DataHelper<T> {
    public static final int TYPE_HEADER = -1, TYPE_FOOTER = -2;
    private View mHeaderView, mFooterView;
    private int headerViewId = -1, footerViewId = -2;

    protected Context mContext;
    protected List<T> mList;
    protected int[] layoutIds;
    protected LayoutInflater mLInflater;

    private SparseArray<View> mConvertViews = new SparseArray<>();

    private OnItemClickListener<T> itemClickListener;
    private OnItemLongClickListener<T> itemLongClickListener;

    public XRVAdapter(Context context, List<T> list, int... layoutIds) {
        this.mContext = context;
        this.mList = list;
        this.layoutIds = layoutIds;
        this.mLInflater = LayoutInflater.from(mContext);
    }

    @Override
    public XRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new XRVHolder(mContext, headerViewId, mHeaderView, getImageLoader());
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new XRVHolder(mContext, footerViewId, mFooterView, getImageLoader());
        }
        if (viewType < 0 || viewType > layoutIds.length) {
            throw new ArrayIndexOutOfBoundsException("layoutIndex");
        }
        if (layoutIds.length == 0) {
            throw new IllegalArgumentException("not layoutId");
        }
        int layoutId = layoutIds[viewType];
        View view = mConvertViews.get(layoutId);
        if (view == null) {
            view = mLInflater.inflate(layoutId, parent, false);
        }
        XRVHolder viewHolder = (XRVHolder) view.getTag();
        if (viewHolder == null || viewHolder.getLayoutId() != layoutId) {
            viewHolder = new XRVHolder(mContext, layoutId, view, getImageLoader());
            return viewHolder;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull XRVHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER)
            return;
        if (getItemViewType(position) == TYPE_FOOTER)
            return;

        position = getPosition(position);
        final T item = mList.get(position);

        holder.getItemView().setTag(R.id.tag_position, position);
        holder.getItemView().setTag(R.id.tag_item, item);

        holder.getItemView().setOnClickListener(clickListener);
        holder.getItemView().setOnLongClickListener(longClickListener);

        onBindData(holder, position, item);
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return mList == null ? 0 : mList.size();
        } else if (mHeaderView != null && mFooterView != null) {
            return mList == null ? 2 : mList.size() + 2;
        } else {
            return mList == null ? 1 : mList.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1 && mFooterView != null) {
            return TYPE_FOOTER;
        }
        position = getPosition(position);
        return getLayoutIndex(position, mList.get(position));
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }
    @Override
    public void onViewAttachedToWindow(XRVHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    private int getPosition(int position) {
        if (mHeaderView != null) {
            position = position - 1;
        }
        return position;
    }
    public int getLayoutIndex(int position, T item) {
        return 0;
    }
    public AdapterImageLoader.ImageLoader getImageLoader() {
        return null;
    }
    public View setHeaderView(int headerViewId) {
        return setHeaderView(headerViewId, null);
    }

    public View setHeaderView(int headerViewId, ViewGroup parent) {
        mHeaderView = mLInflater.inflate(headerViewId, parent, false);
        this.headerViewId = headerViewId;
        notifyItemInserted(0);
        return mHeaderView;
    }

    public void removeHeaderView() {
        if (mHeaderView != null) {
            mHeaderView = null;
            this.headerViewId = -1;
            notifyItemRemoved(0);
        }
    }

    public View setFooterView(int footerViewId) {
        return setFooterView(footerViewId, null);
    }

    public View setFooterView(int footerViewId, ViewGroup parent) {
        mFooterView = mLInflater.inflate(footerViewId, parent, false);
        this.footerViewId = footerViewId;
        notifyItemInserted(mList.size());
        return mFooterView;
    }

    public void removeFooterView() {
        if (mFooterView != null) {
            mFooterView = null;
            this.footerViewId = -2;
            notifyItemRemoved(mList.size() - 1);
        }
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public View getFooterView() {
        return mFooterView;
    }

    protected abstract void onBindData(XRVHolder viewHolder, int position, T item);

    @Override
    public boolean addAll(List<T> list) {
        boolean result = mList.addAll(list);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public boolean addAll(int position, List list) {
        boolean result = mList.addAll(position, list);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public void add(T data) {
        mList.add(data);
        notifyDataSetChanged();
    }

    @Override
    public void add(int position, T data) {
        mList.add(position, data);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public boolean contains(T data) {
        return mList.contains(data);
    }

    @Override
    public T getData(int index) {
        return mList.get(index);
    }

    @Override
    public void modify(T oldData, T newData) {
        modify(mList.indexOf(oldData), newData);
    }

    @Override
    public void modify(int index, T newData) {
        mList.set(index, newData);
        notifyDataSetChanged();
    }

    @Override
    public boolean remove(T data) {
        boolean result = mList.remove(data);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public void remove(int index) {
        mList.remove(index);
        notifyDataSetChanged();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tag_position);
            T item = (T) v.getTag(R.id.tag_item);

            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, position, item);
            }
        }
    };

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            int position = (int) v.getTag(R.id.tag_position);
            T item = (T) v.getTag(R.id.tag_item);

            if (itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(v, position, item);
            }

            return true;
        }
    };

    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }
    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T item);
    }
    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View view, int position, T item);
    }
}
