package nomouse.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nomouse.core.android.R;
import nomouse.util.StringUtils;


/**
 * 空布局，当数据为空时显示
 *
 * @author wuch
 */
public class EmptyView extends LinearLayout {

    private Context context;

    private ImageView imageView;
    private TextView messageView;

    public EmptyView(Context context) {
        super(context);
        this.context = context;
        setupView();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setupView();
    }

    public EmptyView(Context context, int resId, String message) {
        super(context);
        this.context = context;
        setupView();

        setText(message);
        setImage(resId);
    }

    private void setupView() {
        LayoutInflater.from(context).inflate(R.layout.view_empty, this, true);

        messageView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image);
    }

    public void setText(String message) {
        if (StringUtils.isNotEmpty(message)) {
            messageView.setText(message);
        }
    }

    public void setImage(int resId) {
        if (resId != 0) {
            imageView.setImageResource(resId);
        }
    }
}
