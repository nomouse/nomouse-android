package nomouse.android.douga.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nomouse.android.douga.R;
import nomouse.android.douga.view.widget.PtrListView;
import nomouse.android.inject.InjectView;

public class ImageFragment extends android.support.v4.app.Fragment {

    @InjectView()
    private PtrListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_fragment, container, false);
        return rootView;
    }
}
