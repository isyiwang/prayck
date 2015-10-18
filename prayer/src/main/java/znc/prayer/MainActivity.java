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

    private static final int BLUR_RADIUS = 30;

    private ImageView mBackgroundView;
    private KenBurnsView mBlurredBackgroundView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackgroundView = (ImageView) findViewById(R.id.background);

        mBlurredBackgroundView = (KenBurnsView) findViewById(R.id.blurred_background);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "questrial.ttf");
        mTextView = (TextView) findViewById(R.id.textview);
        mTextView.setTypeface(typeface);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextView.setLetterSpacing(0.3f);
        }

        asyncLoadImage(R.drawable.forest);
    }

    private Promises.Promise getPromise() {
        int promiseIndex = getPromiseIndex();
        return Promises.PROMISES.get(promiseIndex);
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
