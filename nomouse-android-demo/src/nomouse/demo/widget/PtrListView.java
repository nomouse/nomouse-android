package nomouse.demo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import nomouse.android.demo.R;

/**
 * 包含下拉刷新和上拉加载更多的ListView
 */
@SuppressLint({"InflateParams"})
public class PtrListView<T> extends FrameLayout {

    public static final int PAGE_SIZE = 10;

    // 当前操作
    private Action currentAction = Action.INIT;
    // 列表当前状态
    private Status currentStatus = Status.EMPTY;
    // 是否正在加载
    private boolean isLoading = false;
    // 是否有错
    private boolean isError = false;

    private static LayoutParams LAYOUT_PARAMS = new LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT);

    private Context context;
    private LayoutInflater inflater;

    private PullToRefreshListView mainListView;
    private ListView listView;

    private BaseAdapter adapter;
    private List<T> list;

    /**
     * 错误
     */
    private View mainError;

    /**
     * 加载中
     */
    private View mainLoading;

    /**
     * 空
     */
    private View mainEmpty;

    /**
     * 列表脚部
     */
    private View footerContainer;

    /**
     * 脚部加载更多
     */
    private View footerMore;

    /**
     * 脚部加载中
     */
    private View footerLoading;

    /**
     * 脚部加载失败
     */
    private View footerError;

    private PullToRefreshBase.OnLastItemVisibleListener onLastItemVisibleListener;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private PullToRefreshBase.OnRefreshListener<ListView> onRefreshListener;
    private PullToRefreshBase.OnRefreshListener2<ListView> onRefreshListener2;
    private OnClickListener onFooterMoreClickListener;
    private OnClickListener onFooterErrorClickListener;

    private OnClickListener onMainErrorClickListener;

    private PullToRefreshBase.Mode mode = PullToRefreshBase.Mode.PULL_FROM_START;

    public PtrListView(Context context) {
        super(context);
        this.context = context;
        this.setLayoutParams(LAYOUT_PARAMS);
        setupView();
    }

    public PtrListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setupView();
    }

    /**
     * 初始加载
     */
    private void setupView() {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.ptr_list_view, this, true);

        // ListView
        this.mainListView = (PullToRefreshListView) findViewById(R.id.list_view);

        // 设置不反弹
        this.mainListView.setPullToRefreshOverScrollEnabled(false);
        // 刷新时不允许滑动
        this.mainListView.setScrollingWhileRefreshingEnabled(false);

        // ListView FooterView
        footerContainer = inflater.inflate(R.layout.view_footer_panel, null);
        footerMore = footerContainer.findViewById(R.id.listview_footer_more);
        footerLoading = footerContainer.findViewById(R.id.listview_footer_loading);
        footerError = footerContainer.findViewById(R.id.listview_footer_error);

        this.listView = mainListView.getRefreshableView();
        this.listView.setDividerHeight(0);
        this.listView.addFooterView(footerContainer);

        // 注册所有有关ListView的事件
        setOnItemClickListener(onItemClickListener);
        setOnItemLongClickListener(onItemLongClickListener);
        setOnLastItemVisibleListener(onLastItemVisibleListener);
        setOnRefreshListener(onRefreshListener);
        setOnRefreshListener(onRefreshListener2);
        setOnFooterMoreClickListener(onFooterMoreClickListener);
        setOnFooterErrorClickListener(onFooterErrorClickListener);

        this.init();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isError() {
        return isError;
    }

    /**
     * 设置下拉刷新模式
     *
     * @param mode
     */
    public void setMode(PullToRefreshBase.Mode mode) {
        this.mode = mode;
        if (mainListView != null) {
            mainListView.setMode(mode);
        }
    }

    public void setEmptyView(View view) {
        if (view == null) {
        } else {
            if (mainEmpty != null) {
                removeView(mainEmpty);
            }
            mainEmpty = view;
            if (mainEmpty.getParent() != null) {
                ViewGroup parent = (ViewGroup) mainEmpty.getParent();
                parent.removeView(mainEmpty);
            }
            addView(mainEmpty, getChildCount());

            notifyUIChanged();
        }
    }

    /**
     * 列表脚部显示载入中
     */
    public boolean more() {
        boolean result;
        if (isLoading || currentStatus == Status.FULL) {
            //如果正在加载，或者当前已经是全部数据的话，不加载
            result = false;
        } else {
            result = true;
            isLoading = true;
            currentAction = Action.MORE;
            footerMore.setVisibility(GONE);
            footerLoading.setVisibility(VISIBLE);
            footerError.setVisibility(GONE);
        }
        return result;
    }

    /**
     * 主布局显示初始化
     */
    public boolean init() {
        boolean result;
        if (isLoading) {
            result = false;
        } else {
            result = true;
            //还原状态
            isLoading = true;
            isError = false;
            currentStatus = Status.EMPTY;
            currentAction = Action.INIT;
            notifyUIChanged();
        }
        return result;
    }

    /**
     * 主布局显示列表刷新
     */
    public boolean refresh() {
        boolean result = false;
        if (!isLoading) {
            result = true;
            isLoading = true;
            currentAction = Action.REFRESH;
        }
        return result;
    }

    public void notifyUIChanged() {

        checkMainLoading();

        checkMainError();

        checkMainEmpty();

        checkMainList();

    }

    //初始化adapter和数据源，只能调用一次
    public void setAdapter(BaseAdapter adapter, List<T> list) {
        if (this.adapter == null) {
            this.list = list;
            this.adapter = adapter;
            this.mainListView.setAdapter(adapter);
            if (list.size() > 0) {
                this.currentAction = Action.INIT;
                this.currentStatus = Status.MORE;
                this.isError = false;
                this.isLoading = false;
                notifyUIChanged();
            }
        }
    }

    /**
     * 是否显示主布局
     */
    protected void checkMainList() {
        boolean flag = (currentStatus == Status.FULL || currentStatus == Status.MORE);
        if (flag) {
            //列表状态
            mainListView.setVisibility(VISIBLE);
            //脚部状态
            if (currentStatus == Status.MORE) {
                footerContainer.setVisibility(VISIBLE);
                footerError
                        .setVisibility((!isLoading && isError) ? VISIBLE
                                : GONE);
                footerLoading
                        .setVisibility((isLoading && !isError) ? VISIBLE
                                : GONE);
                footerMore
                        .setVisibility((!isLoading && !isError) ? VISIBLE
                                : GONE);
            } else {
                footerContainer.setVisibility(GONE);
            }

            //下拉刷新
            if (currentStatus == Status.MORE || currentStatus == Status.FULL
                    || !isLoading) {
                mainListView.onRefreshComplete();
            }
        } else {
            if (mainListView != null) {
                mainListView.setVisibility(GONE);
                footerContainer.setVisibility(GONE);
            }
        }
    }

    /**
     * 是否显示加载布局
     */
    protected void checkMainLoading() {
        boolean flag = (isLoading && !isError);
        if (flag) {
            if (mainLoading == null) {
                ViewStub mainLoadingView = (ViewStub) findViewById(R.id.view_loading);
                mainLoading = mainLoadingView.inflate();
            }
            mainLoading.setVisibility(VISIBLE);
        } else {
            if (mainLoading != null) {
                mainLoading.setVisibility(GONE);
            }
        }
    }

    /**
     * 是否显示空布局
     */
    protected void checkMainEmpty() {
        boolean flag = (!isLoading && !isError && currentStatus == Status.EMPTY);
        if (flag) {
            if (mainEmpty == null) {
                ViewStub mainEmptyStub = (ViewStub) findViewById(R.id.view_empty);
                mainEmpty = mainEmptyStub.inflate();
            }
            mainEmpty.setVisibility(VISIBLE);
        } else {
            if (mainEmpty != null) {
                mainEmpty.setVisibility(GONE);
            }
        }
    }

    /**
     * 是否显示错误布局
     */
    protected void checkMainError() {
        boolean flag = (!isLoading && isError);
        if (flag) {
            if (mainError == null) {
                ViewStub mainErrorStub = (ViewStub) findViewById(R.id.view_error);
                mainError = mainErrorStub.inflate();
                mainError.setOnClickListener(onMainErrorClickListener);
            }
            mainError.setVisibility(VISIBLE);
        } else {
            if (mainError != null) {
                mainError.setVisibility(GONE);
            }
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        if (listener == null) {
            return;
        }
        onItemClickListener = listener;
        if (listView != null) {
            listView.setOnItemClickListener(onItemClickListener);
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        if (listener == null) {
            return;
        }
        onItemLongClickListener = listener;
        if (listView != null) {
            listView.setOnItemLongClickListener(onItemLongClickListener);
        }
    }

    public void setOnLastItemVisibleListener(PullToRefreshBase.OnLastItemVisibleListener listener) {
        if (listener == null) {
            return;
        }
        onLastItemVisibleListener = listener;
        if (mainListView != null) {
            mainListView.setOnLastItemVisibleListener(listener);
        }
    }

    public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<ListView> listener) {
        if (listener == null) {
            return;
        }
        onRefreshListener = listener;
        if (mainListView != null) {
            mainListView.setOnRefreshListener(listener);
        }

    }

    public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener2<ListView> listener) {
        if (listener == null) {
            return;
        }
        onRefreshListener2 = listener;
        if (mainListView != null) {
            mainListView.setOnRefreshListener(listener);
        }
    }

    /**
     * 为“脚部更多”按钮添加点击事件
     *
     * @param listener
     */
    public void setOnFooterMoreClickListener(OnClickListener listener) {
        if (listener == null) {
            return;
        }
        onFooterMoreClickListener = listener;
        if (mainListView != null) {
            footerMore.setOnClickListener(listener);
        }
    }

    /**
     * 为“脚部错误”按钮添加点击事件
     *
     * @param listener
     */
    public void setOnFooterErrorClickListener(OnClickListener listener) {
        if (listener == null) {
            return;
        }
        onFooterErrorClickListener = listener;
        if (mainListView != null) {
            footerError.setOnClickListener(listener);
        }
    }

    /**
     * 为“点击重新加载”按钮添加点击事件
     *
     * @param listener
     */
    public void setOnMainErrorClickListener(OnClickListener listener) {
        if (listener == null) {
            return;
        }
        onMainErrorClickListener = listener;
        if (mainListView != null) {
            mainError.setOnClickListener(listener);
        }
    }

    /**
     * 当前列表的显示状态
     */
    public static enum Status {

        /**
         * 为空
         */
        EMPTY(0),
        /**
         * 列表已满
         */
        FULL(10),
        /**
         * 列表可以加载更多
         */
        MORE(11);

        Status(int status) {
        }
    }

    public static enum Action {
        /**
         * 初始化加载
         */
        INIT(1),
        /**
         * 刷新
         */
        REFRESH(2),
        /**
         * 加载更新
         */
        MORE(3);

        Action(int status) {
        }
    }

    public void notifyDataSetChanged(List<T> result) {
        if (result == null) {
            // result说明返回数据失败,UI维持上次的状态，增加错误提示
            this.isError = true;
        } else if (result.size() == 0 && list.size() == 0) {
            // 列表为空时要显示空布局
            this.currentStatus = Status.EMPTY;
            this.isError = false;
        } else {
            this.isError = false;
            // 请求成功
            switch (this.currentAction) {
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
                this.currentStatus = Status.FULL;
            } else {
                this.currentStatus = Status.MORE;
            }

            // 检查列表是否初始化
            if (this.mainListView == null) {
                this.checkMainList();
            } else {
                adapter.notifyDataSetChanged();
            }
        }

        this.isLoading = false;
        this.notifyUIChanged();
    }


}
