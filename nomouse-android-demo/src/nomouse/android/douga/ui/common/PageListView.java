package nomouse.android.douga.ui.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import nomouse.android.douga.R;
import nomouse.android.widget.EmptyView;
import nomouse.android.widget.ErrorView;
import nomouse.android.widget.LoadingView;


/**
 * 包含下拉刷新和上拉加载更多的ListView
 */
@SuppressLint({"InflateParams"})
public class PageListView<T> extends FrameLayout {

    // 当前操作
    protected Action currentAction = Action.INIT;
    // 列表当前状态
    protected Status currentStatus = Status.MAIN_ERROR;
    // 是否正在加载
    protected boolean isLoading = false;
    // 是否有错
    protected boolean isError = false;

    private static LayoutParams LAYOUT_PARAMS = new LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT);

    private Context context;
    private LayoutInflater inflater;

    protected PullToRefreshListView mainListView;
    private ListView listView;


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

    public PageListView(Context context) {
        super(context);
        this.context = context;
        this.setLayoutParams(LAYOUT_PARAMS);
        setupView();
    }

    public PageListView(Context context, AttributeSet attrs) {
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

        // ListView LoadingView
        mainLoading = new LoadingView(context);
        this.addView(mainLoading, LAYOUT_PARAMS);
        mainLoading.setVisibility(View.VISIBLE);

        mainEmpty = new EmptyView(context);
        this.addView(mainEmpty, LAYOUT_PARAMS);

        mainError = new ErrorView(context);
        this.addView(mainError, LAYOUT_PARAMS);
        mainError.setOnClickListener(onMainErrorClickListener);

        // ListView FooterView
        footerContainer = inflater.inflate(R.layout.listview_footer_panel,
                null);
        footerMore = footerContainer.findViewById(R.id.listview_footer_more);
        footerLoading = footerContainer
                .findViewById(R.id.listview_footer_loading);
        footerError = footerContainer.findViewById(R.id.listview_footer_error);

        // ListView
        // 设置下拉刷新，默认可以下拉
        this.mainListView = new PullToRefreshListView(context, mode,
                PullToRefreshBase.AnimationStyle.FLIP);
        // 设置不反弹
        this.mainListView.setPullToRefreshOverScrollEnabled(false);
        // 刷新时不允许滑动
        this.mainListView.setScrollingWhileRefreshingEnabled(false);

        this.listView = mainListView.getRefreshableView();
        this.listView.setDividerHeight(0);
        this.listView.addFooterView(footerContainer);

        this.addView(mainListView, LAYOUT_PARAMS);
        // this.mainListView.setVisibility(GONE);

        // 注册所有有关ListView的事件
        setOnItemClickListener(onItemClickListener);
        setOnItemLongClickListener(onItemLongClickListener);
        setOnLastItemVisibleListener(onLastItemVisibleListener);
        setOnRefreshListener(onRefreshListener);
        setOnRefreshListener(onRefreshListener2);
        setOnFooterMoreClickListener(onFooterMoreClickListener);
        setOnFooterErrorClickListener(onFooterErrorClickListener);

        // 默认状态
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
        if (isLoading || currentStatus == Status.LIST_FULL) {
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
     * 开始初始化布局
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
            currentStatus = Status.MAIN_ERROR;
            currentAction = Action.INIT;
            notifyUIChanged();
        }
        return result;
    }

    /**
     * 下拉刷新列表
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

    /**
     * 是否显示主布局
     */
    protected void checkMainList() {
        boolean flag = (currentStatus == Status.LIST_FULL || currentStatus == Status.LIST_MORE);
        if (flag) {
            if (mainListView == null) {

            }

            //列表状态
            mainListView.setVisibility(VISIBLE);
            //脚部状态
            if (currentStatus == Status.LIST_MORE) {
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
            if (currentStatus == Status.LIST_MORE || currentStatus == Status.LIST_FULL
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
                mainLoading = new LoadingView(context);
                this.addView(mainLoading, LAYOUT_PARAMS);
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
        boolean flag = (!isLoading && !isError && currentStatus == Status.MAIN_EMPTY);
        if (flag) {
            if (mainEmpty == null) {
                mainEmpty = new EmptyView(context);
                this.addView(mainEmpty, LAYOUT_PARAMS);
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
                mainError = new ErrorView(context);
                this.addView(mainError, LAYOUT_PARAMS);
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

    public static enum Status {
        /**
         * UI已经初始化
         */
        MAIN_ERROR(-1),
        /**
         * 为空
         */
        MAIN_EMPTY(0),
        /**
         * 列表已满
         */
        LIST_FULL(10),
        /**
         * 列表可以加载更多
         */
        LIST_MORE(11);

        Status(int status) {
        }
    }

    public static enum Action {
        /**
         * 初始化加载
         */
        INIT(1),
        /**
         * 为空
         */
        REFRESH(2),
        /**
         *
         */
        MORE(3);

        Action(int status) {
        }
    }

}
