package nomouse.demo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import nomouse.android.douga.R;
import nomouse.android.inject.InjectUtils;
import nomouse.android.inject.InjectView;
import nomouse.android.widget.NestedRadioGroup;


public class MainActivity extends FragmentActivity {

    @InjectView
    private NestedRadioGroup navGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        InjectUtils.inject(MainActivity.this);

        navGroup.setOnCheckedChangeListener(new NestedRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroup group, int checkedId) {

            }
        });


    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //拦截返回键
        }
        return super.onKeyUp(keyCode, event);
    }
}
