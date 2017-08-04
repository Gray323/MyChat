package com.rxd.mychat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.rxd.mychat.R;
import com.rxd.mychat.bean.SortModel;
import com.rxd.mychat.db.ContactItem;
import com.rxd.mychat.ui.activity.NewFriendListActivity;
import com.rxd.mychat.ui.adapter.ContactAdapter;
import com.rxd.mychat.utils.CharacterParser;
import com.rxd.mychat.utils.DBUtils;
import com.rxd.mychat.utils.PinyinComparator;
import com.rxd.mychat.widget.SideBar;
import com.rxd.mychat.xmpp.XmppUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.lv_contact_list)
    ListView sortListView;
    @BindView(R.id.sidrbar)
    SideBar sideBar;
    @BindView(R.id.dialog)
    TextView dialog;
    private LinearLayout llHeader;
    private LinearLayout llNewFriend;
    private LinearLayout llGroupChat;


    private CharacterParser characterParser;
    private ContactAdapter adapter;
    private List<SortModel> sourceDataList = new ArrayList<>();
    private PinyinComparator comparator;

    public static ContactFragment newInstance(boolean isFirstIn){
        ContactFragment fragment = new ContactFragment();
        Bundle bundle = new Bundle();
        Logger.d(isFirstIn);
        bundle.putBoolean("isFirstIn", isFirstIn);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact,container,false);
        ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView() {
        characterParser = CharacterParser.getInstance();

        comparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        //添加头部
        llHeader = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_friend_head,null);
        sortListView.addHeaderView(llHeader);
        llNewFriend = (LinearLayout) llHeader.findViewById(R.id.layout_addfriend);
        llGroupChat = (LinearLayout) llHeader.findViewById(R.id.layout_group);
        llNewFriend.setOnClickListener(this);
        llGroupChat.setOnClickListener(this);

        //sidebar的按下事件
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }
            }
        });


        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            if (position != 0){
            }
            if (position == 0){

            }
            }
        });

        Bundle bundle = getArguments();
        boolean isFirst = bundle.getBoolean("isFirstIn");

        Logger.d(isFirst);
        //判断是否是第一次登陆，如果是则从服务器获取数据，如果不是，则从本地数据库加载数据
        if (isFirst){
            sourceDataList = filledData(XmppUtils.findFriends());
        }else{
            List<ContactItem> contactItems  = DBUtils.getContacts();
            sourceDataList = filledData(contactItems);
        }

        //根据拼音顺序对姓名进行排序
        Collections.sort(sourceDataList, comparator);
        adapter = new ContactAdapter(getActivity(), sourceDataList);
        sortListView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 从array中获取姓名数据
     * @param date
     * @return
     */
    private List<SortModel> filledData(List<ContactItem> date){
        List<SortModel> mSortList = new ArrayList<SortModel>();
        Log.d("sc","data.size:"+ date.size());
        for(int i=0; i<date.size(); i++){

            SortModel sortModel = new SortModel();
            sortModel.setNickname(date.get(i).getNickname());
            sortModel.setBmAvatar(date.get(i).getAvatar());
            sortModel.setName(date.get(i).getAccount());

            String pinyin = characterParser.getSelling(date.get(i).getNickname());
            String sortString = "";
            if (pinyin != null && pinyin != "") {
                sortString = pinyin.substring(0, 1).toUpperCase();
            }


            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    @Override
    public void onStart() {
       /* sourceDataList = filledData(XmppUtils.findFriends());
        //根据拼音顺序对姓名进行排序
        Collections.sort(sourceDataList, comparator);
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }*/
        super.onStart();
    }

    /**
     * 头部两个按钮的监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_addfriend:
                Intent intent = new Intent(getActivity(), NewFriendListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_group:
                break;
        }
    }
}
