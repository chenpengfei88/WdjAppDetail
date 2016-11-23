package com.fe.wdj.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.fe.wdj.util.BigDecimalUtils;
import com.fe.wdj.R;

/**
 * Created by chenpengfei on 2016/11/22.
 */
public class SVRootLinearLayout extends LinearLayout {


    private LinearLayout mContentLL;
    private ImageView mIconImageView;
    private int mMargin;
    private boolean mIsAnimation, mIsLayoutImageView;
    private int mInitBottom;
    public int mContentMarginTop, mContentBottomOffset, mImageLeftOffset, mImageTopOffset, mTouchMoveOffset;


    private ScrollView mParentScrollView;
    private int mTitleViewHeight;
    private int mCenterVisibleViewHeight;
    private int mContentLlHeight, mContentLlWidth, mIconImageViewHeight, mIconImageViewWidth;

    /**
     *  拖拽
     */
    private float mInitY;
    private int mTouchSlop;
    private boolean mIsDrag;
    private OnCloseListener mOnCloseListener;
    private OnUpdateBgColorListener mOnUpdateBgColorListener;


    public void setOnCloseListener(OnCloseListener onCloseListener) {
        mOnCloseListener = onCloseListener;
    }

    public void setOnUpdateBgColorListener(OnUpdateBgColorListener onUpdateBgColorListener) {
        mOnUpdateBgColorListener = onUpdateBgColorListener;
    }

    public SVRootLinearLayout(Context context) {
        super(context);
    }

    public SVRootLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMargin = context.getResources().getDimensionPixelOffset(R.dimen.icon_margin);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SVRootLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SVRootLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setInitBottom(int bottom) {
            mInitBottom = bottom;
    }

    public void setAnimationStatus(boolean isAnimation) {
        mIsAnimation = isAnimation;
    }

    public void setLayoutImageView(boolean layoutImageView) {
        mIsLayoutImageView = layoutImageView;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentLL = (LinearLayout) findViewById(R.id.ll_content);
        mIconImageView = (ImageView) findViewById(R.id.imageview_icon);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int contentTop = mContentMarginTop + mTouchMoveOffset;
        mContentLL.layout(0, contentTop, mContentLlWidth, !mIsAnimation ? contentTop + mContentLlHeight : mInitBottom + mContentBottomOffset);

        if(!mIsLayoutImageView) return;
        int left = mMargin + mImageLeftOffset;
        int top = mMargin + mImageTopOffset;
        mIconImageView.layout(left, top, left + mIconImageViewWidth, top + mIconImageViewHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childNumHeight = 0;
        for(int i = 0; i < getChildCount(); i++) {
            childNumHeight += getChildAt(i).getMeasuredHeight();
        }
        if(childNumHeight > getMeasuredHeight()) {
            setMeasuredDimension(getMeasuredWidth(), childNumHeight);
        }

        mTitleViewHeight = getChildAt(0).getMeasuredHeight();
        mContentLlHeight = mContentLL.getMeasuredHeight();
        mContentLlWidth = mContentLL.getMeasuredWidth();
        mIconImageViewHeight = mIconImageView.getMeasuredHeight();
        mIconImageViewWidth = mIconImageView.getMeasuredWidth();
    }

    public void setContentInitMarginTop(int marginTop) {
        mContentMarginTop = marginTop;
        requestLayout();
    }

    public void setAllViewOffset(int contentMarginTop, int contentBottomOffset, int imageLeftOffset, int imageTopOffset) {
        mContentMarginTop = contentMarginTop;
        mContentBottomOffset = contentBottomOffset;
        mImageLeftOffset = imageLeftOffset;
        mImageTopOffset = imageTopOffset;
        requestLayout();
    }

    public void setTouchMoveOffset(float touchMoveOffset) {
        if(touchMoveOffset < 0) touchMoveOffset = 0;
        System.out.println("===============touchMoveOffset====================" + touchMoveOffset);
        mTouchMoveOffset = (int) touchMoveOffset;
        requestLayout();
        updateBgColor(mTouchMoveOffset);
    }

    public void updateBgColor(int offset) {
        if(mOnUpdateBgColorListener != null) {
            float ratio = BigDecimalUtils.divide(offset, mCenterVisibleViewHeight);
            if(ratio > 1) ratio = 1;
            if(ratio < 0) ratio = 0;
            mOnUpdateBgColorListener.onUpdate(ratio);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(mParentScrollView == null) mParentScrollView = (ScrollView) getParent();
        mCenterVisibleViewHeight = mParentScrollView.getHeight() - mTitleViewHeight;
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean consumption = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitY = event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                float yOffset = moveY - mInitY;
              //  System.out.println("=========RootLinearLayout================ACTION_MOVE===========" + yOffset + "======t===" +mTouchSlop );
                //拖动
                if((mParentScrollView.getScrollY() <= 0 && moveY > mInitY) || mIsDrag) {
                    setTouchMoveOffset(yOffset);
                    mIsDrag = true;
                    consumption = true;
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    consumption = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsDrag = false;
                boolean isUp = false;
                int animationMoveOffset;
                if(mContentLL.getTop() <= mCenterVisibleViewHeight / 2 + mTitleViewHeight) {
                    animationMoveOffset = mTouchMoveOffset;
                    isUp = true;
                } else {
                    animationMoveOffset = mParentScrollView.getHeight() - mContentLL.getTop();
                }
                startAnimation(animationMoveOffset, isUp, mTouchMoveOffset);
                break;
        }
        return consumption;
    }

    private void startAnimation(final int moveOffset, final boolean isUp, final int currentMoveOffset) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(moveOffset / mTouchSlop * 10);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = (float) animation.getAnimatedValue();
                if(isUp)
                    mTouchMoveOffset = (int) (moveOffset * (1 - ratio));
                else
                    mTouchMoveOffset = currentMoveOffset + (int) (moveOffset * ratio);
                requestLayout();
                updateBgColor(mTouchMoveOffset);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(!isUp && mOnCloseListener != null) mOnCloseListener.onClose();
            }
        });
    }

    public interface OnCloseListener {
        void onClose();
    }

    public interface OnUpdateBgColorListener {
        void onUpdate(float ratio);
    }
}
