package com.rxd.mychat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rxd.mychat.R;
import com.rxd.mychat.ui.activity.MyInfoActivity;
import com.rxd.mychat.ui.activity.SettingActivity;
import com.rxd.mychat.utils.Consts;
import com.rxd.mychat.utils.SharePreferencesUtils;
import com.rxd.mychat.xmpp.XmppUtils;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.rxd.mychat.utils.Consts.VCARD_GENDER;

public class AboutMeFragment extends Fragment {

    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.tvname)
    TextView tvname;
    @BindView(R.id.tvmsg)
    TextView tvmsg;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.view_user)
    RelativeLayout viewUser;
    @BindView(R.id.txt_album)
    TextView txtAlbum;
    @BindView(R.id.txt_collect)
    TextView txtCollect;
    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.txt_card)
    TextView txtCard;
    @BindView(R.id.txt_smail)
    TextView txtSmail;
    @BindView(R.id.txt_setting)
    TextView txtSetting;
    Unbinder unbinder;

    private String userAccount;
    private VCard vCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_me, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();
        return view;
    }

    private void initView(){
        //获取账户信息
        userAccount = SharePreferencesUtils.getShareStringData(Consts.USER_ACCOUNT);
        vCard = XmppUtils.getUserVCard();
        tvname.setText(vCard.getNickName());
        tvmsg.setText("账号:" + userAccount);
        if ("男".equals(vCard.getField(VCARD_GENDER))){
            ivSex.setImageResource(R.mipmap.ic_sex_male);
        }else if ("女".equals(vCard.getField(VCARD_GENDER))){
            ivSex.setImageResource(R.mipmap.ic_sex_female);
        }else{
            ivSex.setVisibility(View.GONE);
        }
        if (vCard.getAvatar() != null){
            Glide.with(getActivity()).load(vCard.getAvatar()).
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).into(head);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.txt_album,R.id.txt_collect,R.id.txt_money,R.id.txt_card,R.id.txt_smail,R.id.txt_setting,R.id.view_user})
    public void onClick(View view){
        switch (view.getId()){
            //跳转到设置界面
            case R.id.txt_setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            //跳转到我的个人信息界面
            case R.id.view_user:
                Intent intent1 = new Intent(getActivity(), MyInfoActivity.class);
                startActivity(intent1);
        }
    }

}
