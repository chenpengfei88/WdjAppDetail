package com.fe.wdj.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fe.wdj.DisplayUtils;
import com.fe.wdj.R;
import widget.DetailScrollView;
import widget.SVRootLinearLayout;

public class DetailActivity extends AppCompatActivity {

    private SVRootLinearLayout mSVRootLl;
    private DetailScrollView mScrollView;
    private ImageView mIconImageView;
    private LinearLayout mContentLl;
    private LinearLayout mBottomLl, mTitleLl;
    private TextView mAppNameTextView;
    private ColorDrawable mRootCDrawable;

    private int mContentTopOffsetNum;
    private int mContentBottomOffsetNum;
    private int mImageLeftOffsetNum;
    private int mImageTopOffsetNum;
    private int mColorInitAlpha = 150;


    private int mViewMarginTop;
    private int mImageId;
    private String mAppName;

    private boolean initData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setActionBar();
        receiveParams();
        initView();
        //设置初始化的位置
        mSVRootLl.setContentInitMarginTop(mViewMarginTop);
        mContentTopOffsetNum = mViewMarginTop - getResources().getDimensionPixelOffset(R.dimen.view_height);
        startAnimation();
    }

    private void initView() {
        //设置root节点view的背景透明度
        LinearLayout rootLl = (LinearLayout) findViewById(R.id.ll_root);
        Drawable rootBgDrawable = rootLl.getBackground();
        mRootCDrawable = (ColorDrawable) rootBgDrawable;
        mRootCDrawable.setAlpha(mColorInitAlpha);

        mScrollView = (DetailScrollView) findViewById(R.id.scrollView);
        mSVRootLl = (SVRootLinearLayout) findViewById(R.id.ll_sv_root);
        mIconImageView = (ImageView) findViewById(R.id.imageview_icon);
        mIconImageView.setImageResource(mImageId);
        mContentLl = (LinearLayout) findViewById(R.id.ll_content);
        mBottomLl = (LinearLayout) findViewById(R.id.ll_bottom);
        mAppNameTextView = (TextView) findViewById(R.id.textview_appname);
        mAppNameTextView.setText(mAppName);
        mTitleLl = (LinearLayout) findViewById(R.id.ll_title);

        mImageTopOffsetNum = getResources().getDimensionPixelOffset(R.dimen.title_view_height);

        mSVRootLl.setOnCloseListener(new SVRootLinearLayout.OnCloseListener() {
            @Override
            public void onClose() {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        mSVRootLl.setOnUpdateBgColorListener(new SVRootLinearLayout.OnUpdateBgColorListener() {
            @Override
            public void onUpdate(float ratio) {
                mRootCDrawable.setAlpha((int) (mColorInitAlpha - mColorInitAlpha * ratio));
            }
        });
    }

    private void receiveParams() {
        Intent intent = getIntent();
        mViewMarginTop = intent.getIntExtra("viewMarginTop", 0);
        mImageId = intent.getIntExtra("imageId", 0);
        mAppName = intent.getStringExtra("appName");
    }

    private void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(500);
        valueAnimator.setStartDelay(200);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(!initData) {
                    mContentBottomOffsetNum = mScrollView.getMeasuredHeight() - mContentLl.getBottom();
                    mSVRootLl.setInitBottom(mContentLl.getBottom());
                    mSVRootLl.setIsAnimation(true);
                    mSVRootLl.setLayoutImageView(true);
                    mImageLeftOffsetNum = (DisplayUtils.getScreenWidth(DetailActivity.this) - mIconImageView.getWidth()) / 2 - getResources().getDimensionPixelOffset(R.dimen.icon_margin);
                    initData = true;
                }
                float ratio = (float) animation.getAnimatedValue();
                //内容布局顶部偏移量
                int contentTopOffset = (int) (ratio * mContentTopOffsetNum);
                //内容布局底部偏移量
                int contentBottomOffset = (int) (ratio * mContentBottomOffsetNum);
                //图片左边偏移量
                int imageLeftOffset = (int) (ratio * mImageLeftOffsetNum);
                //图片上边偏移量
                int imageTopOffset =  (int) (ratio * mImageTopOffsetNum);

                mSVRootLl.setAllViewOffset(mViewMarginTop - contentTopOffset, contentBottomOffset, imageLeftOffset, imageTopOffset);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSVRootLl.setIsAnimation(false);
                mBottomLl.setVisibility(View.VISIBLE);
                mTitleLl.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            finish();
            overridePendingTransition(0, R.anim.activity_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
