package com.rxd.mychat.ui.activity;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.Consts;
import com.rxd.mychat.utils.ToastUtils;
import com.rxd.mychat.utils.Utils;
import com.rxd.mychat.xmpp.XmppUtils;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import butterknife.BindView;
import butterknife.OnClick;

import static com.rxd.mychat.utils.Consts.VCARD_GENDER;
import static com.rxd.mychat.utils.Consts.VCARD_SIGNATURE;

/**
 * 添加好友信息界面
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.user_info_avatar)
    ImageView userInfoAvatar;
    @BindView(R.id.user_info_name)
    TextView userInfoName;
    @BindView(R.id.user_info_username)
    TextView userInfoUsername;
    @BindView(R.id.iv_userinfo_sex)
    ImageView ivUserinfoSex;
    @BindView(R.id.userinfo_email)
    TextView userinfoEmail;
    @BindView(R.id.userinfo_org)
    TextView userInfoOrg;
    @BindView(R.id.userinfo_signnature)
    TextView userInfoSign;
    @BindView(R.id.btn_to_add_friend)
    Button btnToAddFriend;

    private String username;
    private VCard vCard;

    @OnClick(R.id.btn_to_add_friend)
    public void onViewClicked() {
        /*if (XmppUtils.findFriendsNone().contains(username)){
            ToastUtils.showToast("你已经发送过请求或者你们已经是好友了");
        }else{*/
            XmppUtils.addFriend(username);
            ToastUtils.showToast("请求已发送");
       /* }*/

    }

    @Override
    protected void initData() {
        username  = getIntent().getStringExtra(Consts.USER_INFO);
        vCard = XmppUtils.getUserVCard(username);
        if (vCard.getAvatar() != null){
            userInfoAvatar.setImageBitmap(Utils.getPicFromBytes(vCard.getAvatar(), new BitmapFactory.Options()));
        }
        userInfoName.setText(vCard.getNickName());
        userInfoUsername.setText("账号：" + username);
        userinfoEmail.setText(vCard.getEmailHome());
        userInfoOrg.setText(vCard.getOrganization());
        userInfoSign.setText(vCard.getField(VCARD_SIGNATURE));
        if ("男".equals(vCard.getField(VCARD_GENDER))){
            ivUserinfoSex.setImageResource(R.mipmap.ic_sex_male);
        }else if ("女".equals(vCard.getField(VCARD_GENDER))){
            ivUserinfoSex.setImageResource(R.mipmap.ic_sex_female);
        }else{
            ivUserinfoSex.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView() {
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }
}
