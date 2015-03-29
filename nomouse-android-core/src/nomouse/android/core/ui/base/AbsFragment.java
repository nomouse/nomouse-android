package nomouse.android.core.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.tixa.lx.servant.common.manager.AppManager;
import com.tixa.lx.servant.common.util.StringUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by nomouse on 2014/8/11.
 */
public abstract class AbsFragment extends Fragment {
	protected Activity activity;
	protected LayoutInflater inflater;
	protected EventBus eventBus;
	protected AppManager appManager;

	protected View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		inflater = LayoutInflater.from(activity);
		eventBus = EventBus.getDefault();
		appManager = AppManager.getInstance();
	}

	protected final int getColor(int resId) {
		return appManager.getColor(resId);
	}
}
