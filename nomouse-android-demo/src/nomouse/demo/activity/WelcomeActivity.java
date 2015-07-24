package nomouse.demo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import nomouse.android.douga.R;
import nomouse.demo.fragment.WelcomeFragment;


public class WelcomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_container);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_body, new WelcomeFragment())
                    .commit();
        }
    }

}
