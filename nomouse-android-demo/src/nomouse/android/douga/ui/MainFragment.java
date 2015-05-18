package nomouse.android.douga.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nomouse.android.douga.R;
import nomouse.android.inject.InjectView;

public class MainFragment extends android.support.v4.app.Fragment {

    @InjectView()
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        return rootView;
    }
}
