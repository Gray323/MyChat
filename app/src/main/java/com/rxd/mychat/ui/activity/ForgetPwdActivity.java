package com.rxd.mychat.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.Consts;
import com.rxd.mychat.utils.ToastUtils;
import com.rxd.mychat.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码的第一个界面
 */
public class ForgetPwdActivity extends BaseActivity {


    @BindView(R.id.title_img_back)
    ImageView titleImgBack;
    @BindView(R.id.title_txt_title)
    TextView titleTxtTitle;
    @BindView(R.id.et_forget_phone)
    EditText etForgetPhone;
    @BindView(R.id.et_forget_code)
    EditText etForgetCode;
    @BindView(R.id.forget_btn_send_code)
    Button forgetBtnSendCode;
    @BindView(R.id.btn_forget_next)
    Button btnForgetNext;


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        titleTxtTitle.setText(getResources().getString(R.string.set_forget_account));
        titleImgBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @OnClick({R.id.title_img_back,R.id.forget_btn_send_code,R.id.btn_forget_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_img_back:
                finish();
                break;
            case R.id.forget_btn_send_code:
                break;
            case R.id.btn_forget_next:
                String phone = etForgetPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    ToastUtils.showToast(getResources().getString(R.string.no_account));
                    return;
                }
                if (!Utils.isMobile(phone)){
                    ToastUtils.showToast(getResources().getString(R.string.error_phone));
                    return;
                }
                Intent intent = new Intent(mContext, SetNewPwdActivity.class);
                intent.putExtra(Consts.FORGET_PHONE, phone);
                startActivity(intent);
                break;
        }
    }

}
