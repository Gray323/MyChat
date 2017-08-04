package com.rxd.mychat.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.ToastUtils;
import com.rxd.mychat.utils.Utils;
import com.rxd.mychat.widget.LoadingDialog;
import com.rxd.mychat.xmpp.XmppConnectionManager;
import com.rxd.mychat.xmpp.XmppUtils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_usertel_reg)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_password_reg)
    EditText etPassword;
    @BindView(R.id.btn_send_code)
    Button btnSend;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.title_txt_title)
    TextView tvTitle;
    @BindView(R.id.title_img_back)
    ImageView ivBack;

    private LoadingDialog dialog;

    private Handler regHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog.dismiss();
            switch (msg.what){
                case 0:
                    ToastUtils.showToast(getResources().getString(R.string.reg_error0));
                    break;
                case 1:
                    ToastUtils.showToast(getResources().getString(R.string.reg_error1));
                    finish();
                    break;
                case 2:
                    ToastUtils.showToast(getResources().getString(R.string.reg_error2));
                    break;
                case 3:
                case 4:
                    ToastUtils.showToast(getResources().getString(R.string.reg_error3));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        AppManager.getAppManager().addActivity(this);
        mContext  = this;
        dialog = new LoadingDialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        tvTitle.setText("注册");
        ivBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        if (dialog != null) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    @OnClick({R.id.btn_send_code,R.id.btn_register,R.id.title_img_back})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_send_code:
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.title_img_back:
                finish();
                break;
        }
    }

    /**
     *注册方法
     */
    private void register(){
        final String account = etPhone.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account)){
            ToastUtils.showToast(getResources().getString(R.string.no_account));
            return;
        }
        if (TextUtils.isEmpty(password)){
            ToastUtils.showToast(getResources().getString(R.string.no_password));
            return;
        }
        if (!Utils.isMobile(account)){
            ToastUtils.showToast(getResources().getString(R.string.error_phone));
            return;
        }
        if (!Utils.isPassword(password)){
            ToastUtils.showToast(getResources().getString(R.string.error_password));
            return;
        }
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                XmppConnectionManager manager = XmppConnectionManager.getInstance();
                XMPPTCPConnection conn = manager.init();

                try{
                    if (!conn.isConnected()){
                        conn.connect();
                    }
                    Map<String, String> attributes = new HashMap<String, String>();
                    int result = XmppUtils.register(attributes,account,password,conn);
                    regHandler.sendEmptyMessage(result);
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }



}
