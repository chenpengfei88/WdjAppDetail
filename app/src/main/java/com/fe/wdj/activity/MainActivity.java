package com.fe.wdj.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.fe.wdj.DividerItemDecoration;
import com.fe.wdj.R;
import com.fe.wdj.adapter.MyAdapter;

public class MainActivity extends AppCompatActivity {

    private MyAdapter mMyAdapter;
    private RecyclerView mRecyclerView;

    private Object[][] mImageArray = new Object[][] {{R.drawable.ic_aqy, "爱奇艺"}, {R.drawable.ic_bb, "哔哩哔哩"},
                                                                               {R.drawable.ic_cz, "赤足"}, {R.drawable.ic_kk, "快看"},
                                                                               {R.drawable.ic_kr, "kingRoot"}, {R.drawable.ic_sg, "搜狗"},
                                                                               {R.drawable.ic_xl, "迅雷"}, {R.drawable.ic_yk, "优酷"},
                                                                                {R.drawable.ic_yyy, "网易云音乐"}, {R.drawable.ic_qq, "QQ"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar();
        initView();
        fillData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
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

    private void fillData() {
        mMyAdapter = new MyAdapter(this, mImageArray);
        mRecyclerView.setAdapter(mMyAdapter);
        mMyAdapter.setOnItemClickLitener(new MyAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                int imageId = (int) mImageArray[position][0];
                String appName = (String) mImageArray[position][1];
                int viewMarginTop = view.getTop() + getResources().getDimensionPixelOffset(R.dimen.bar_view_height);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("viewMarginTop", viewMarginTop);
                intent.putExtra("imageId", imageId);
                intent.putExtra("appName", appName);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }
}
