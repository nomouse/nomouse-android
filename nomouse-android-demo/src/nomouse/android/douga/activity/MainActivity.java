package nomouse.android.douga.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import nomouse.android.douga.R;
import nomouse.android.douga.fragment.MainFragment;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

}
