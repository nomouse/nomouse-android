package nomouse.android.demo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.igexin.sdk.PushManager;

import nomouse.android.demo.R;
import nomouse.android.demo.fragment.LoginFragment;


public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle("Douga");

        setContentView(R.layout.view_container);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_body, new LoginFragment())
                    .commit();
        }

        PushManager.getInstance().initialize(this.getApplicationContext());
    }

}