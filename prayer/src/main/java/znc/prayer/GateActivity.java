package znc.prayer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by isaac on 10/19/15.
 */
public class GateActivity extends Activity {

    private static final String KEY = "amos";
    public static final String SP_GATE_KEY = "SP_GATE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gate);

        ((EditText)findViewById(R.id.password)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (TextUtils.equals(KEY, v.getText())) {
                        saveSharedPreferencesForGate();
                        goToNext();
                    }
                }

                return false;
            }
        });
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
