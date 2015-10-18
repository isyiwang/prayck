package znc.prayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final int FADE_IN_TEXT_MS = 5000;
    private static final int ZOOM_IN_TEXT_MS = 25000;
    private static final int BLUR_RADIUS = 30;

    private ImageView mBackgroundView;
    private KenBurnsView mBlurredBackgroundView;
    private TextView mTextView;
    private ObjectAnimator mAnimator;
    private boolean mUseVerse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackgroundView = (ImageView) findViewById(R.id.background);
        mBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePromise();
            }
        });

        mBlurredBackgroundView = (KenBurnsView) findViewById(R.id.blurred_background);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "questrial.ttf");
        mTextView = (TextView) findViewById(R.id.textview);
        mTextView.setTypeface(typeface);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextView.setLetterSpacing(0.3f);
        }

        asyncLoadImage(R.drawable.forest);
        fadeInPromise(getPromise());
    }

    private Promises.Promise getPromise() {
        int promiseIndex = getPromiseIndex();
        return Promises.PROMISES.get(promiseIndex);
    }

    private void fadeInPromise(Promises.Promise promise) {
        String text = mUseVerse ? promise.verse : promise.prayer;
        fadeInText(text);
    }

    private void togglePromise() {
        mUseVerse = !mUseVerse;

        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    fadeInPromise(getPromise());
                }
            });
            mAnimator.setFloatValues(0, (float) mAnimator.getAnimatedValue());
            mAnimator.setDuration(650);
            mAnimator.reverse();
        }
    }

    private void fadeInText(String text) {
        text = text.toUpperCase();
        mTextView.setAlpha(0.0f);
        mTextView.setText(text);
        mAnimator = ObjectAnimator.ofFloat(mTextView, "alpha", 0.0f, 1.0f).setDuration(FADE_IN_TEXT_MS);
        mAnimator.start();

        ScaleAnimation scale = new ScaleAnimation(1f, 1.15f, 1f, 1.15f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setFillAfter(true);
        scale.setDuration(ZOOM_IN_TEXT_MS);
        scale.setInterpolator(new AccelerateInterpolator());
        mTextView.setAnimation(scale);
    }

    /**
     * Reads the current promise index from SharedPreferences
     * @return
     */
    private int getPromiseIndex() {
        return 0;
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
