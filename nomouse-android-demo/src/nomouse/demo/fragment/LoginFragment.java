package nomouse.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import nomouse.android.demo.R;
import nomouse.android.inject.InjectUtils;
import nomouse.android.inject.InjectView;
import nomouse.demo.activity.MainActivity;

public class LoginFragment extends android.support.v4.app.Fragment {

    @InjectView
    private TextView username;

    @InjectView
    private TextView password;

    @InjectView
    private Button login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);

        InjectUtils.inject(LoginFragment.this, rootView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
