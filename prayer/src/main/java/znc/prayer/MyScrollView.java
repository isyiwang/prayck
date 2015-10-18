package znc.prayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by isaac on 10/18/15.
 */
public class MyScrollView extends ScrollView {
    public interface ScrollViewListener {
        void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    private ScrollViewListener mListener;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mListener != null) {
            mListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setOnScrollListener(ScrollViewListener listener) {
        mListener = listener;
    }
}
