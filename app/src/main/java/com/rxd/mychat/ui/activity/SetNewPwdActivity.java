package com.rxd.mychat.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.Consts;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * 忘记密码的第一个界面
 */
public class SetNewPwdActivity extends BaseActivity {

    @BindView(R.id.title_img_back)
    ImageView titleImgBack;
    @BindView(R.id.title_txt_title)
    TextView titleTxtTitle;
    @BindView(R.id.new_et_password)
    EditText newEtPassword;
    @BindView(R.id.new_et_password_second)
    EditText newEtPasswordSecond;
    @BindView(R.id.btn_set_done)
    Button btnDone;

    private String phone;

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra(Consts.FORGET_PHONE);
    }

    @Override
    protected void initView() {
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        titleTxtTitle.setText("填写新的密码");
        titleImgBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_new_pwd;
    }

    @OnClick({R.id.title_img_back,R.id.btn_set_done})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_img_back:
                finish();
                break;
            case R.id.btn_set_done:
                break;
        }
    }

}
