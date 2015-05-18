package nomouse.android.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by nomouse on 2014/8/11.
 */
public abstract class AbsFragment extends Fragment {
    protected Activity activity;
    protected LayoutInflater inflater;

    protected View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        inflater = LayoutInflater.from(activity);
    }

}
