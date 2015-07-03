package nomouse.android.douga.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import nomouse.android.douga.R;
import nomouse.android.douga.fragment.LoginFragment;


public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle("Douga");

        setContentView(R.layout.abs_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new LoginFragment())
                    .commit();
        }
    }

}
