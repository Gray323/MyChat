package com.rxd.mychat.ui.activity;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.db.ContactItem;
import com.rxd.mychat.utils.DBUtils;

import butterknife.BindString;
import butterknife.BindView;

import static com.rxd.mychat.utils.Consts.FRIEND_NAME;

/**
 * 好友信息界面
 */
public class FriendMsgActivity extends BaseActivity {

    @BindView(R.id.title_img_back)
    ImageView titleImgBack;
    @BindView(R.id.title_txt_title)
    TextView titleTxtTitle;
    @BindView(R.id.iv_friend_avatar)
    ImageView ivFriendAvatar;
    @BindView(R.id.tv_friend_name)
    TextView tvFriendName;
    @BindView(R.id.iv_friend_sex)
    ImageView ivFriendSex;
    @BindView(R.id.tv_friend_accout)
    TextView tvFriendAccout;
    @BindView(R.id.tv_friend_email)
    TextView tvFriendEmail;
    @BindView(R.id.tv_friend_region)
    TextView tvFriendRegion;
    @BindView(R.id.tv_friend_sign)
    TextView tvFriendSign;
    @BindView(R.id.btn_friend_sendmsg)
    Button btnFriendSendmsg;
    @BindString(R.string.more_info)
    String title;

    private ContactItem contactItem;

    @Override
    protected void initData() {
        String friend_name = getIntent().getStringExtra(FRIEND_NAME);
        contactItem = DBUtils.getContactItem(friend_name).get(0);
        titleImgBack.setVisibility(View.VISIBLE);
        titleTxtTitle.setText(title);
        if (contactItem.getAvatar() != null){
            Glide.with(mContext).load(Base64.decode(contactItem.getAvatar(), Base64.DEFAULT)).
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivFriendAvatar);
        }
        tvFriendName.setText(contactItem.getNickname());
        tvFriendAccout.setText("账号：" + friend_name);
        tvFriendEmail.setText(contactItem.getEmail());
        tvFriendRegion.setText(contactItem.getReg());
        tvFriendSign.setText(contactItem.getSignature());
        if ("男".equals(contactItem.getGender())){
            ivFriendSex.setImageResource(R.mipmap.ic_sex_male);
        }else if ("女".equals(contactItem.getGender())){
            ivFriendSex.setImageResource(R.mipmap.ic_sex_female);
        }else{
            ivFriendSex.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView() {
        mContext = this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_msg;
    }
}
