package nomouse.android.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import nomouse.core.android.R;

/**
 * 加载中页面，第一次进入页面需要加载时显示
 *
 * @author wuch
 */
public class LoadingView extends LinearLayout {

    private Context context;

    // private ProgressBar progressBar;
    private TextView messageView;

    public LoadingView(Context context) {
        super(context);
        this.context = context;
        setupView();
    }

    public LoadingView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        setupView();
    }

    public LoadingView(Context context, String message) {
        super(context);
        this.context = context;
        setupView();
    }

    private void setupView() {

        LayoutInflater.from(context).inflate(R.layout.view_loading, this, true);

        // progressBar = (ProgressBar) findViewById(R.id.progress);

        messageView = (TextView) findViewById(R.id.text_message);
    }
}
