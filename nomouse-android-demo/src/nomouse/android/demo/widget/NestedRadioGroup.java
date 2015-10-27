package nomouse.android.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

/**
 * 可嵌套的RadioGroup
 *
 * @author nomouse
 */
public class NestedRadioGroup extends LinearLayout {

    // 被选中的ID
    private int mCheckedId = -1;
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    private SparseArray<RadioButton> mRadioButtomMap = new SparseArray<>();
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;

    public NestedRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedRadioGroup(Context context) {
        super(context);
        init();
    }

    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
    }

    @Override
    public void addView(View child, int index,
                        ViewGroup.LayoutParams params) {

        findChildRadioButton(child);

        super.addView(child, index, params);
    }

    private void findChildRadioButton(View view) {

        int resId = view.getId();

        if (view instanceof RadioButton && resId != View.NO_ID) {

            RadioButton rb = (RadioButton) view;
            mRadioButtomMap.put(resId, rb);

            rb.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
        } else if (view instanceof ViewGroup) {

            ViewGroup viewGroup = (ViewGroup) view;

            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                findChildRadioButton(viewGroup.getChildAt(i));
            }
        }

    }

    public void check(int id) {

        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);

    }

    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        RadioButton rb = mRadioButtomMap.get(viewId);
        if (rb != null) {
            rb.setChecked(checked);
        }
    }

    public interface OnCheckedChangeListener {
        public void onCheckedChanged(NestedRadioGroup group, int checkedId);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    private class CheckedStateTracker implements
            CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }
}
