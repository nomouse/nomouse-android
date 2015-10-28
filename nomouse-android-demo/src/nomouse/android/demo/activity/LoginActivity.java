package nomouse.android.demo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import nomouse.android.demo.R;
import nomouse.android.demo.fragment.LoginFragment;
import rx.Observable;
import rx.functions.Action1;


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

        Observable
                .just("Hello")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.print(s);
                    }
                });
    }

}
