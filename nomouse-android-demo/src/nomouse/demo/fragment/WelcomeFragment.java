package nomouse.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nomouse.android.douga.R;
import nomouse.demo.activity.LoginActivity;

public class WelcomeFragment extends android.support.v4.app.Fragment {

    private static final int DELAY = 2000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELAY:
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.welcome_fragment, container, false);

        handler.sendMessageDelayed(handler.obtainMessage(DELAY), DELAY);
        return rootView;
    }
}
