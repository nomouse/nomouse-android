package nomouse.android.douga.ui.common;

import android.app.Activity;

import java.util.List;

import nomouse.android.base.AbsAdapter;


/**
 * 抽象分页适配器，配合CustomListView使用
 *
 * @param <T>
 * @author nomouse
 */
public abstract class AbsPageAdapter<T> extends AbsAdapter<T> {

    public static final int PAGE_SIZE = 10;

    protected PageListView<T> listView;

    public AbsPageAdapter(Activity context, List<T> currentList,
                          PageListView<T> listView) {
        super(context, currentList);
        this.context = context;
        this.listView = listView;
    }

    public void notifyDataSetChanged(List<T> result) {
        if (result == null) {
            // result说明返回数据失败,UI维持上次的状态，增加错误提示
            listView.isError = true;
        } else if (result.size() == 0 && list.size() == 0) {
            // 列表为空时要显示空布局
            listView.currentStatus = PageListView.Status.MAIN_EMPTY;
            listView.isError = false;
        } else {
            listView.isError = false;
            // 请求成功
            switch (listView.currentAction) {
                case INIT:
                    // 刷新
                    list.clear();
                    break;
                case MORE:
                    // 分页
                    // TODO 考虑total有变化时分页数据的有效性
                    break;
                case REFRESH:
                    // 刷新
                    list.clear();
                    break;
            }
            // 添加数据
            list.addAll(result);

            if (result.size() == 0 || result.size() < PAGE_SIZE) {
                // TODO 返回列表数量为0说明已经没有更多数据了,不能避免刚好加载最后PAGE_SIZE大小的数据的情况
                listView.currentStatus = PageListView.Status.LIST_FULL;
            } else {
                listView.currentStatus = PageListView.Status.LIST_MORE;
            }

            // 检查列表是否初始化
            if (listView.mainListView == null) {
                listView.checkMainList();
                listView.mainListView.setAdapter(this);
            } else {
                notifyDataSetChanged();
            }
        }

        listView.isLoading = false;
        listView.notifyUIChanged();
    }

}
