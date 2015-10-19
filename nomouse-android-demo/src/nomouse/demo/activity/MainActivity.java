package nomouse.demo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.Map;

import nomouse.android.douga.R;
import nomouse.android.inject.InjectUtils;
import nomouse.android.inject.InjectView;
import nomouse.android.widget.NestedRadioGroup;
import nomouse.demo.fragment.HomeFragment;


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
                dispatchOption(checkedId);
            }
        });

        navGroup.check(R.id.navHome);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //拦截返回键
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        fragmentMap.clear();
        super.onDestroy();
    }

    private Map<Class<? extends Fragment>, Fragment> fragmentMap = new HashMap<>();

    private int currentResId;
    private Fragment currentFragment;

    public void dispatchOption(int resId) {
        if (currentResId == resId) {
            return;
        }
        switch (resId) {
            case R.id.navHome:
                //        setTitle(currentOptionId);
                currentResId = resId;
                dispatchFragment(HomeFragment.class);
                break;
        }
    }

    private void dispatchFragment(Class<? extends Fragment> cls) {
        Fragment fragment = loadFragment(cls);
        FragmentManager fm = getSupportFragmentManager();
        if (currentFragment == null) {
            // getSupportFragmentManager().beginTransaction()
            // .detach(currentFragment)
            // .replace(R.id.layout_content, fragment, cls.getName())
            // .attach(fragment).commit();
            fm.beginTransaction()
                    .replace(R.id.layout_content, fragment, cls.getName())
                    .commit();
        } else if (fm.findFragmentByTag(cls.getName()) != fragment) {
            fm.beginTransaction().hide(currentFragment)
                    .replace(R.id.layout_content, fragment, cls.getName())
                    .commit();
        } else {
            fm.beginTransaction().hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
    }

    public Fragment loadFragment(Class<? extends Fragment> fragment) {
        Fragment result = null;

        if (fragmentMap.containsKey(fragment)) {
            result = fragmentMap.get(fragment);
        } else {
            try {
                result = fragment.newInstance();
                fragmentMap.put(fragment, result);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
