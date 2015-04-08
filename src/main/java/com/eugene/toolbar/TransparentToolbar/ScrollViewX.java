package com.eugene.toolbar.TransparentToolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/*
 * @Reference
 * https://github.com/ramanadv/fadingActionBar
 */
public class ScrollViewX extends ScrollView {

    private OnScrollViewListener mOnScrollViewListener;

    public ScrollViewX(Context context) {
        super(context);
    }

    public ScrollViewX(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewX(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface OnScrollViewListener {
        void onScrollChanged(ScrollViewX v, int l, int t, int oldL, int oldT);
    }

    public void setOnScrollViewListener(OnScrollViewListener l) {
        this.mOnScrollViewListener = l;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
