package com.rxd.mychat.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.bean.UserInfo;
import com.rxd.mychat.ui.adapter.FindNewFriendAdapter;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.ToastUtils;
import com.rxd.mychat.xmpp.XmppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 查找新的好友界面
 */
public class AddNewFriendActivity extends BaseActivity {

    @BindView(R.id.search_iv_back)
    ImageView searchIvBack;
    @BindView(R.id.et_search_new_friend)
    EditText etSearchNewFriend;
    @BindView(R.id.tv_search_new_friend)
    TextView tvSearchNewFriend;
    @BindView(R.id.rl_new_friend_search)
    RelativeLayout rlNewFriendSearch;
    @BindView(R.id.rv_search_new_friend_list)
    RecyclerView friendList;

    private List<UserInfo> searchLists = new ArrayList<>();
    private FindNewFriendAdapter adapter;


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        etSearchNewFriend.addTextChangedListener(watcher);
        adapter = new FindNewFriendAdapter(mContext,searchLists);
        friendList.setLayoutManager(new LinearLayoutManager(mContext));
        friendList.setAdapter(adapter);
    }




    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String search = etSearchNewFriend.getText().toString().trim();
            if ("".equals(search)){
                rlNewFriendSearch.setVisibility(View.GONE);
                rlNewFriendSearch.setClickable(false);
                //重新设置adapter
                searchLists.clear();
                adapter.notifyDataSetChanged();
            }else{
                rlNewFriendSearch.setVisibility(View.VISIBLE);
                tvSearchNewFriend.setText("搜索:"+search);
                //重新设置adapter
                searchLists.clear();
                String username1 = etSearchNewFriend.getText().toString();
                List<UserInfo> list =XmppUtils.searchFriends(username1);
                if (list == null){
                    ToastUtils.showToast("数据为空");
                    return;
                }
                searchLists.addAll(list);
                Log.d("sc","searchLists:"+searchLists.toString());
                adapter.notifyDataSetChanged();
                rlNewFriendSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username2 = etSearchNewFriend.getText().toString();
                        //重新设置adapter
                        searchLists.clear();
                        searchLists.addAll(XmppUtils.searchFriends(username2));
                        Log.d("sc","searchLists:"+searchLists.toString());
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_new_friend;
    }


}
