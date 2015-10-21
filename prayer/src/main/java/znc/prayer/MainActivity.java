package znc.prayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.crittercism.app.Crittercism;
import com.flurry.android.FlurryAgent;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final int BLUR_RADIUS = 50;
    private static final float BLUR_Y_THRESHOLD = 400f;
    private static final float MIN_OVERLAY_ALPHA = 0.1f;
    private static final float MAX_OVERLAY_ALPHA = 0.33f;

    public static final String PROMISE_INDEX_KEY = "PROMISE_INDEX_KEY";

    private ImageView mBackgroundView;
    private ImageView mBlurredBackgroundView;
    private View mOverlayView;
    private TextView mTitleView;
    private TextView mVerseView;
    private TextView mPrayerView;

    private int mPromiseIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (!hasFinishedGate()) {
            startActivity(new Intent(this, GateActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }

        mBackgroundView = (ImageView) findViewById(R.id.background);
        mOverlayView = findViewById(R.id.overlay_view);
        mOverlayView.setAlpha(MIN_OVERLAY_ALPHA);
        mBlurredBackgroundView = (ImageView) findViewById(R.id.blurred_background);
        mTitleView = (TextView) findViewById(R.id.title);

        mVerseView = (TextView) findViewById(R.id.verse);
        mPrayerView = (TextView) findViewById(R.id.prayer);

        updateTextMargin();
        setupScrollListener();

        int promiseIndex = 0;
        if (getIntent() != null) {
            promiseIndex = getIntent().getIntExtra(PROMISE_INDEX_KEY, 0);
        }
        mPromiseIndex = promiseIndex;

        loadPromise(promiseIndex);

        // DEBUG
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                MainActivity.this.getApplicationContext().sendBroadcast(intent);
            }
        });

        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPromise(++mPromiseIndex);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        FlurryAgent.onEndSession(this);
    }

    private void updateTextMargin() {
        WindowManager wm =
                (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int deviceHeight = size.y;
        int titleHeight = getTextViewHeight(mTitleView);

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mTitleView.getLayoutParams();
        lp.topMargin = deviceHeight - titleHeight - 25;
        mTitleView.setLayoutParams(lp);
    }

    private boolean hasFinishedGate() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        return sp.getBoolean(GateActivity.SP_GATE_KEY, false);
    }

    private void setupScrollListener() {
        ((MyScrollView) findViewById(R.id.scrollview)).setOnScrollListener(new MyScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
                float imageAlpha = (BLUR_Y_THRESHOLD - y) / BLUR_Y_THRESHOLD;
                imageAlpha = Math.max(imageAlpha, 0.0f);

                mBackgroundView.setAlpha(imageAlpha);

                float overlayAlpha = (1f - imageAlpha) * MAX_OVERLAY_ALPHA;
                overlayAlpha = Math.max(overlayAlpha, MIN_OVERLAY_ALPHA);
                mOverlayView.setAlpha(overlayAlpha);
            }
        });
    }

    private void loadPromise(int promiseIndex) {
        Promises.Promise promise = Promises.PROMISES.get(promiseIndex % Promises.PROMISES.size());
        mTitleView.setText(promise.title);
        mVerseView.setText(promise.verse);
        mPrayerView.setText(promise.prayer);

        asyncLoadImage(promise.resId);
    }

    private void asyncLoadImage(final int resId) {
        Picasso.with(this).load(resId).fit().centerCrop().into(mBackgroundView);
        Picasso.with(this).load(resId).fit().centerCrop().into(mBlurredBackgroundView, new Callback() {
            @Override
            public void onSuccess() {
                asyncBlurBitmap(((BitmapDrawable) mBlurredBackgroundView.getDrawable()).getBitmap());
            }

            @Override
            public void onError() {

            }
        });
    }

    private void asyncBlurBitmap(final Bitmap bitmap) {
        new ParallelAsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                return NativeStackBlur.process(bitmap, BLUR_RADIUS);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                mBlurredBackgroundView.setImageBitmap(bitmap);
            }
        }.execute();
    }

    public static int getTextViewHeight(TextView textView) {
        WindowManager wm =
                (WindowManager) textView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int deviceWidth = size.x;

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);

        return textView.getMeasuredHeight();
    }
}
