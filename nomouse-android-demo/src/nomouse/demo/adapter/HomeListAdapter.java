package nomouse.demo.adapter;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

import nomouse.android.base.AbsAdapter;
import nomouse.android.demo.R;

public class HomeListAdapter extends AbsAdapter<String> {

    public HomeListAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public void convert(ViewHolder holder, int position, String s) {
        TextView text = holder.getView(R.id.text_title);
        text.setText(list.get(position));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.home_list_item;
    }
}
