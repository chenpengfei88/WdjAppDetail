package com.fe.wdj.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by chenpengfei on 2016/11/22.
 */
public class DetailScrollView extends ScrollView {

    private float mInitY;
    private int mTouchSlop;
    private SVRootLinearLayout mSVRootLl;

    public DetailScrollView(Context context) {
        super(context);
    }

    public DetailScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public DetailScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DetailScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSVRootLl = (SVRootLinearLayout) getChildAt(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        System.out.println("=========ScrollView=========onTouchEvent===========================" + ev.getAction());
        return super.onTouchEvent(ev);
    }
}
