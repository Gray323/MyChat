package com.rxd.mychat.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rxd.mychat.R;
import com.rxd.mychat.app.MyChatApp;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.Consts;
import com.rxd.mychat.utils.SharePreferencesUtils;
import com.rxd.mychat.xmpp.SmackService;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.title_txt_title)
    TextView titleTxtTitle;
    @BindView(R.id.title_img_back)
    ImageView ivBack;
    @BindView(R.id.txt_usersafe)
    TextView txtUsersafe;
    @BindView(R.id.txt_msgtip)
    TextView txtMsgtip;
    @BindView(R.id.txt_yinsi)
    TextView txtYinsi;
    @BindView(R.id.txt_tongyong)
    TextView txtTongyong;
    @BindView(R.id.txt_about)
    TextView txtAbout;
    @BindView(R.id.btnexit)
    Button btnexit;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        AppManager.getAppManager().addActivity(this);
        mContext = this;
        titleTxtTitle.setText(getResources().getString(R.string.setting));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @OnClick({R.id.txt_usersafe,R.id.title_img_back,R.id.txt_msgtip,R.id.txt_yinsi,
            R.id.txt_tongyong,R.id.txt_about,R.id.btnexit})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnexit:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定要退出吗?");
                builder.setTitle("警告");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharePreferencesUtils.setShareStringData(Consts.USER_ACCOUNT,"");
                        SharePreferencesUtils.setShareStringData(Consts.USER_PASSWORD,"");
                        AppManager.getAppManager().finishAllActivity();
                        SmackService.getInstance().stopSelf();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        MyChatApp.xmpptcpConnection.instantShutdown();
                        MyChatApp.xmpptcpConnection = null;
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
    }

}
