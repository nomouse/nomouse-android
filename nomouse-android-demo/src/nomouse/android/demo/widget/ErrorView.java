package nomouse.android.demo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import nomouse.core.android.R;


/**
 * 加载错误页面，第一次进入页面失败时显示
 *
 * @author wuch
 */
public class ErrorView extends LinearLayout {

    private Context context;

    // private ImageView imageView;

    public ErrorView(Context context) {
        super(context);
        this.context = context;
        setupView();
    }

    public ErrorView(Context context, String message) {
        super(context);
        this.context = context;
        setupView();
    }

    private void setupView() {
        LayoutInflater.from(context).inflate(R.layout.view_error, this, true);
        // imageView = (ImageView) findViewById(R.id.image);
    }

}
