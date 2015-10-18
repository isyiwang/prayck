package znc.prayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackgroundView = (ImageView) findViewById(R.id.background);
        mOverlayView = findViewById(R.id.overlay_view);
        mOverlayView.setAlpha(MIN_OVERLAY_ALPHA);
        mBlurredBackgroundView = (ImageView) findViewById(R.id.blurred_background);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "questrial.ttf");
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setTypeface(typeface);

        mVerseView = (TextView) findViewById(R.id.verse);
        mPrayerView = (TextView) findViewById(R.id.prayer);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTitleView.setLetterSpacing(0.3f);
        }

        setupScrollListener();
        asyncLoadImage(R.drawable.forest);

        int promiseIndex = 0;
        if (getIntent() != null) {
            promiseIndex = getIntent().getIntExtra(PROMISE_INDEX_KEY, 0);
        }

        loadPromise(promiseIndex);

        // DEBUG
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                MainActivity.this.getApplicationContext().sendBroadcast(intent);
            }
        });
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
        Promises.Promise promise = Promises.PROMISES.get(promiseIndex);
        mTitleView.setText(promise.title);
        mVerseView.setText(promise.verse);
        mPrayerView.setText(promise.prayer);
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
}
