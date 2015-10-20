package nomouse.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import nomouse.android.demo.R;
import nomouse.android.inject.InjectUtils;
import nomouse.android.inject.InjectView;
import nomouse.android.widget.EmptyView;
import nomouse.demo.adapter.HomeListAdapter;

public class HomeFragment extends android.support.v4.app.Fragment {

    @InjectView
    private ListView listView;

    @InjectView
    private Button button;

    @InjectView
    private EmptyView viewEmpty;

    private int count;

    private List<String> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        InjectUtils.inject(this, rootView);

        data = new ArrayList<>();
//        data.add("123");
//        data.add("456");
        HomeListAdapter adapter = new HomeListAdapter(getActivity(), data);
        listView.setAdapter(adapter);
        listView.setEmptyView(viewEmpty);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                viewEmpty.setImage(R.drawable.welcome);
                viewEmpty.setText("未查询到数据" + count);
            }
        });
        return rootView;
    }
}
