package znc.prayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by isaac on 10/19/15.
 */
public class GateActivity extends Activity {

    private static final String KEY = "imissyouz";
    public static final String SP_GATE_KEY = "SP_GATE_KEY";

    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gate);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (TextUtils.equals(KEY, mPasswordView.getText())) {
                                saveSharedPreferencesForGate();
                                goToNext();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPasswordView.requestFocus();
        mPasswordView.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mPasswordView, 0);
            }
        }, 200);
    }

    private void goToNext() {
        overridePendingTransition(R.anim.enter, R.anim.leave);
        startActivity(new Intent(this, OnboardingActivity.class));
        finish();
    }

    private void saveSharedPreferencesForGate() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putBoolean(SP_GATE_KEY, true).apply();
    }
}
