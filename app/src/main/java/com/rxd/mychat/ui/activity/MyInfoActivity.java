package com.rxd.mychat.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.SharePreferencesUtils;
import com.rxd.mychat.xmpp.XmppUtils;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import butterknife.BindView;
import butterknife.OnClick;

import static com.rxd.mychat.utils.Consts.JUMP_TO_SET_BASIC_INFO;
import static com.rxd.mychat.utils.Consts.SET_EMAIL;
import static com.rxd.mychat.utils.Consts.SET_NICKNAME;
import static com.rxd.mychat.utils.Consts.SET_SIGNATURE;
import static com.rxd.mychat.utils.Consts.USER_ACCOUNT;
import static com.rxd.mychat.utils.Consts.VCARD_GENDER;
import static com.rxd.mychat.utils.Consts.VCARD_SIGNATURE;

/**
 * 我的个人信息界面
 */
public class MyInfoActivity extends BaseActivity {

    @BindView(R.id.title_img_back)
    ImageView titleImgBack;
    @BindView(R.id.title_txt_title)
    TextView titleTxtTitle;
    @BindView(R.id.iv_my_info_avatar)
    ImageView ivMyInfoAvatar;
    @BindView(R.id.tv_my_info_nickname)
    TextView tvMyInfoNickname;
    @BindView(R.id.tv_my_info_account)
    TextView tvMyInfoAccount;
    @BindView(R.id.tv_my_info_gender)
    TextView tvGender;
    @BindView(R.id.tv_my_info_email)
    TextView tvMyInfoEmail;
    @BindView(R.id.tv_my_info_org)
    TextView tvOrg;
    @BindView(R.id.tv_my_info_express)
    TextView tvMyInfoExpress;

    private VCard vCard;
    private String[] arrayType = new String[]{"男","女"};


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        titleTxtTitle.setText("个人信息");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_info;
    }

    @OnClick({R.id.title_img_back,R.id.rl_my_info_avatar,R.id.rl_my_info_account,R.id.rl_my_info_nickname,
                R.id.rl_my_info_gender,R.id.rl_my_info_org,R.id.rl_my_info_email,R.id.rl_my_info_sign})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_img_back:
                finish();
                break;
            case R.id.rl_my_info_avatar:
                Intent intent = new Intent(MyInfoActivity.this, ChooseAvatarPhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_info_nickname:
                jumpToSet(SET_NICKNAME);
                break;
            case R.id.rl_my_info_email:
                jumpToSet(SET_EMAIL);
                break;
            case R.id.rl_my_info_sign:
                jumpToSet(SET_SIGNATURE);
                break;
            case R.id.rl_my_info_gender:
                changeGender();
                break;
        }
    }

    /**
     * 改变性别的方法
     */
    private void changeGender(){
        final int[] selectedIndex = {0};
        Dialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("请选择您的身份")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XmppUtils.setGender(arrayType[selectedIndex[0]]);
                        tvGender.setText(arrayType[selectedIndex[0]]);
                    }
                })
                .setSingleChoiceItems(arrayType, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex[0] = which;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vCard = XmppUtils.getUserVCard();
        if (vCard.getAvatar() != null){
            Glide.with(mContext).load(vCard.getAvatar()).
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivMyInfoAvatar);
        }
        tvMyInfoAccount.setText(SharePreferencesUtils.getShareStringData(USER_ACCOUNT));
        tvGender.setText(vCard.getField(VCARD_GENDER));
        tvMyInfoNickname.setText(vCard.getNickName());
        tvMyInfoEmail.setText(vCard.getEmailHome());
        tvOrg.setText(vCard.getOrganization());
        tvMyInfoExpress.setText(vCard.getField(VCARD_SIGNATURE));
    }

    /**
     * 跳转到设置信息界面
     */
    private void jumpToSet(int goal){
        Intent intent = new Intent(this, SetBasicInfoActivity.class);
        intent.putExtra(JUMP_TO_SET_BASIC_INFO,goal);
        startActivity(intent);
    }

}
