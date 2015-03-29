package nomouse.android.core.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.tixa.lx.servant.common.OpenPlatformHandlerUtil;
import com.tixa.lx.servant.common.manager.AppManager;
import com.tixa.lx.servant.common.util.StringUtils;
import com.tixa.util.LoggerUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by nomouse on 2014/8/11.
 */
public abstract class AbsActivity extends FragmentActivity {

    protected Activity activity;
    protected LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        inflater = LayoutInflater.from(this);
    }

    public static final void showToast(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static final void showToast(CharSequence text, int duration) {
        if (StringUtils.isNotEmpty(text)) {
            Toast.makeText(OpenPlatformHandlerUtil.getLXAppcation(), text, duration).show();
        }
    }

    public static final void showToast(int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static final void showToast(int resId, int duration) {
        if (resId > 0) {
            Toast.makeText(OpenPlatformHandlerUtil.getLXAppcation(), resId, duration).show();
        }
    }

    public static final void showRequestError(VolleyError volleyError) {
        String error;
        NetworkResponse response = volleyError.networkResponse;
        if (response == null) {
            error = "请检查您的网络";
        } else {
            error = "服务器异常，请稍后重试";
            LoggerUtil.w("request error", "code:" + response.statusCode);
        }
        showToast(error, Toast.LENGTH_SHORT);
    }

}
