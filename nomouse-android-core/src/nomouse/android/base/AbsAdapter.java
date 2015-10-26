package nomouse.android.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * 抽象ListView Adapter
 */
public abstract class AbsAdapter<T> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;

    protected List<T> list;

    public AbsAdapter(Context context, List<T> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getViewHolder(context, convertView,
                parent, getLayoutResId());
        convertView = holder.getConvertView();
        convert(holder, position, getItem(position));
        return convertView;
    }

    /**
     * 获取Layout布局
     *
     * @return
     */
    public abstract int getLayoutResId();

    /**
     * 渲染
     *
     * @param holder
     * @param t
     */
    public abstract void convert(ViewHolder holder, int position, T t);

    public static class ViewHolder {
        private SparseArray<View> viewMap;
        private View convertView;

        private ViewHolder(Context context, ViewGroup parent, int layoutId) {
            viewMap = new SparseArray<>();

            this.convertView = LayoutInflater.from(context).inflate(layoutId,
                    parent, false);
            this.convertView.setTag(this);
        }

        /**
         * 静态方法，生成ViewHolder，需要注意的是convertView为空时，要通过getConvertView重启获取一下，
         * 否则会报空指针异常
         *
         * @param context
         * @param convertView
         * @param parent
         * @param layoutId
         * @return
         */
        public static ViewHolder getViewHolder(Context context,
                                               View convertView, ViewGroup parent, int layoutId) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder(context, parent, layoutId);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return viewHolder;
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int viewId) {
            View view = viewMap.get(viewId);
            if (view == null) {
                view = convertView.findViewById(viewId);
                viewMap.put(viewId, view);
            }
            return (T) view;
        }

        public View getConvertView() {
            return convertView;
        }

    }

    /**
     * 获取R文件的URI
     *
     * @param resId
     * @return
     */
    protected String getResourceUri(int resId) {
        // return (Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
        // + context.getResources().getResourcePackageName(resId) + "/"
        // + context.getResources().getResourceTypeName(resId) + "/"
        // + context.getResources().getResourceEntryName(resId))).toString();
        return "drawable://" + resId;
    }

}
