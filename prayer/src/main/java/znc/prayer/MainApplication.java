package znc.prayer;

import android.app.Application;

import com.flurry.android.FlurryAgent;

/**
 * Created by isaac on 10/21/15.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        Crittercism.initialize(getApplicationContext(), "56273f5d8d4d8c0a00d07ed9");
        FlurryAgent.init(getApplicationContext(), "2JPYFZQH8QSKMJTVNX2X");
    }
}
