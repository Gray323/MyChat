package com.rxd.mychat.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.Consts;
import com.rxd.mychat.utils.SharePreferencesUtils;
import com.rxd.mychat.utils.ToastUtils;
import com.rxd.mychat.widget.LoadingDialog;
import com.rxd.mychat.xmpp.SmackService;

import butterknife.BindView;
import butterknife.OnClick;

import static com.rxd.mychat.utils.Consts.USER_ACCOUNT;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_usertel)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_wenti)
    TextView tvProblem;
    @BindView(R.id.btn_to_register)
    Button btnToRegister;
    @BindView(R.id.title_txt_title)
    TextView tvTitle;
    @BindView(R.id.title_img_back)
    ImageView ivBack;

    private String userPhone;
    private String password;

    //登录广播
    private BroadcastReceiver receiver;

    private LoadingDialog dialog;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        dialog = new LoadingDialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        tvTitle.setText("登陆");
        initReceiver();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.btn_login,R.id.btn_to_register,R.id.tv_wenti})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                userPhone = etPhone.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                login(userPhone,password);

                break;
            case R.id.btn_to_register:
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;
            case R.id.tv_wenti:
                Intent intent1 = new Intent(mContext, ForgetPwdActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;
        }
    }

    /**
     * 登录方法
     * @param phone
     * @param pwd
     */
    private void login(final String phone, final String pwd){
        Log.d("sc",phone+","+pwd);
        if (TextUtils.isEmpty(phone)){
            ToastUtils.showToast(getResources().getString(R.string.no_account));
            return;
        }
        if (TextUtils.isEmpty(pwd)){
            ToastUtils.showToast(getResources().getString(R.string.no_account));
            return;
        }
        dialog.show();

        SharePreferencesUtils.setShareStringData(USER_ACCOUNT,phone);
        SharePreferencesUtils.setShareStringData(Consts.USER_PASSWORD,pwd);

        Intent intent = new Intent(mContext, SmackService.class);
        startService(intent);
    }

    private void initReceiver(){
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Consts.LOGIN_BROADCAST)){
                    boolean isLoginSuccess = intent.getBooleanExtra(Consts.LOGIN_EXTRA, false);
                    if (isLoginSuccess){
                        dialog.dismiss();
                        Intent intent1 = new Intent(mContext, MainActivity.class);
                        startActivity(intent1);
                        finish();
                    }else{
                        //登录失败，对话框消失，同时停止服务
                        dialog.dismiss();
                        ToastUtils.showToast(getResources().getString(R.string.login_fail));
                        SmackService.getInstance().stopSelf();
                    }
                }
            }
        };

        //注册广播
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(Consts.LOGIN_BROADCAST);
        registerReceiver(receiver,mFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑广播
        unregisterReceiver(receiver);
    }
}
