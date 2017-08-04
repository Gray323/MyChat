package com.rxd.mychat.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.xmpp.XmppUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

import static com.rxd.mychat.utils.Consts.JUMP_TO_SET_BASIC_INFO;
import static com.rxd.mychat.utils.Consts.SET_EMAIL;
import static com.rxd.mychat.utils.Consts.SET_NICKNAME;
import static com.rxd.mychat.utils.Consts.SET_SIGNATURE;

public class SetBasicInfoActivity extends BaseActivity {

    @BindView(R.id.title_set_info_img_back)
    ImageView titleSetInfoImgBack;
    @BindView(R.id.title_set_info_tv)
    TextView titleSetInfoTv;
    @BindView(R.id.btn_set_info_ok)
    Button btnSetInfoOk;
    @BindView(R.id.et_set_info_nickname)
    EditText etSetInfoNickname;
    @BindView(R.id.et_set_info_email)
    EditText etSetInfoEmail;
    @BindView(R.id.et_set_info_signature)
    EditText etSetInfoSignature;
    @BindString(R.string.change_nickname)
    String changeNickname;
    @BindString(R.string.change_email)
    String changeEmail;
    @BindString(R.string.change_signature)
    String changeSignature;

    private int isWhich;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        isWhich = intent.getIntExtra(JUMP_TO_SET_BASIC_INFO,0);
        if (isWhich == SET_NICKNAME){//如果是要修改昵称
            etSetInfoNickname.setVisibility(View.VISIBLE);
            titleSetInfoTv.setText(changeNickname);
        }else if (isWhich == SET_EMAIL){//如果是要修改邮箱
            etSetInfoEmail.setVisibility(View.VISIBLE);
            titleSetInfoTv.setText(changeEmail);
        }else if (isWhich == SET_SIGNATURE){//如果是要修改个性签名
            etSetInfoSignature.setVisibility(View.VISIBLE);
            titleSetInfoTv.setText(changeSignature);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_basic_info;
    }

    @OnClick({R.id.title_set_info_img_back, R.id.btn_set_info_ok})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_set_info_img_back:
                finish();
                break;
            case R.id.btn_set_info_ok:
                if (isWhich == SET_NICKNAME){//如果是要修改昵称
                    String nickname = etSetInfoNickname.getText().toString().trim();
                    XmppUtils.setNickName(nickname);
                }else if (isWhich == SET_EMAIL){//如果是要修改邮箱
                    String email = etSetInfoEmail.getText().toString().trim();
                    XmppUtils.setEmail(email);
                }else if (isWhich == SET_SIGNATURE){//如果是要修改个性签名
                    String signature = etSetInfoSignature.getText().toString();
                    XmppUtils.setSignnature(signature);
                }
                finish();
                break;
        }
    }

}
