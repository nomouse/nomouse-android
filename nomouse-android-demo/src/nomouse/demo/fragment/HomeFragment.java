package nomouse.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import nomouse.android.douga.R;
import nomouse.android.inject.InjectUtils;
import nomouse.android.inject.InjectView;
import nomouse.demo.adapter.HomeListAdapter;

public class HomeFragment extends android.support.v4.app.Fragment {

    @InjectView()
    private ListView listView;

    private List<String> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        InjectUtils.inject(this, rootView);

        data = new ArrayList<>();
        data.add("123");
        data.add("456");
        HomeListAdapter adapter = new HomeListAdapter(getActivity(), data);
        listView.setAdapter(adapter);
        return rootView;
    }
}
