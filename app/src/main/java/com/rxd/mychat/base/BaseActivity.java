package com.rxd.mychat.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.rxd.mychat.R;
import com.rxd.mychat.utils.StatusBarCompat;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/6.
 */

public abstract class BaseActivity extends AppCompatActivity{

    protected int statusBarColor = 0;
    protected View statusBarView = null;
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (statusBarColor == 0) {
            statusBarView = StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.bar_color));
        } else if (statusBarColor != -1) {
            statusBarView = StatusBarCompat.compat(this, statusBarColor);
        }
        transparent19and20();
        ButterKnife.bind(this);
        
        initView();
        initData();
    }

    protected void transparent19and20() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract int getLayoutId();

}
