package com.rxd.mychat.ui.activity;

import android.content.Intent;
import android.os.Handler;

import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.utils.AppManager;

/**
 * 第一个界面
 */
public class SplashActivity extends BaseActivity {


    @Override
    protected void initData() {
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void initView() {
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
        }
    };

}
