package znc.prayer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by isaac on 10/19/15.
 */
public class OnboardingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding);
        ((TextView)findViewById(R.id.welcome_text)).setText("Hi Christine, Happy birthday!\nI miss you a lot");

        findViewById(R.id.time_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    
            }
        });
    }
}
