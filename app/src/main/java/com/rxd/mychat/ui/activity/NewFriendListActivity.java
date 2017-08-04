package com.rxd.mychat.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anye.greendao.gen.AddNewFriendBeanDao;
import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.db.AddNewFriendBean;
import com.rxd.mychat.db.AddNewFriendManager;
import com.rxd.mychat.ui.adapter.AddNewFriendListAdapter;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.SharePreferencesUtils;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

import static com.rxd.mychat.utils.Consts.USER_ACCOUNT;

/**
 * 申请的新的好友界面
 */
public class NewFriendListActivity extends BaseActivity {

    @BindView(R.id.title_txt_title)
    TextView titleTxt;
    @BindView(R.id.title_img_back)
    ImageView titleImgBack;
    @BindView(R.id.title_txt_right)
    TextView titleTxtRight;
    @BindView(R.id.new_friend_list)
    RecyclerView newFriendList;
    @BindString(R.string.add_new_friend)
    String addNewFriend;
    @BindString(R.string.new_friend)
    String newFriend;

    private AddNewFriendManager addNewFriendManager;
    private List<AddNewFriendBean> beanList;
    private AddNewFriendListAdapter adapter;

    @Override
    protected void initData() {
        beanList = addNewFriendManager.getAbstractDao().queryBuilder()
                .where(AddNewFriendBeanDao.Properties.Currentname.eq(SharePreferencesUtils.getShareStringData(USER_ACCOUNT)))
                .where(AddNewFriendBeanDao.Properties.Username.eq(SharePreferencesUtils.getShareStringData(USER_ACCOUNT)))
                .build().list();
        adapter = new AddNewFriendListAdapter(mContext,beanList);
        newFriendList.setLayoutManager(new LinearLayoutManager(mContext));
        newFriendList.setAdapter(adapter);
    }

    @Override
    protected void initView() {
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        addNewFriendManager = new AddNewFriendManager();
        titleTxt.setVisibility(View.VISIBLE);
        titleTxt.setText(newFriend);
        titleTxtRight.setText(addNewFriend);
        titleImgBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_friend_list;
    }

    @OnClick({R.id.title_img_back,R.id.title_txt_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_img_back:
                finish();
                break;
            case R.id.title_txt_right:
                Intent intent = new Intent(mContext,AddNewFriendActivity.class);
                startActivity(intent);
                break;
        }
    }

}
