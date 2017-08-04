package com.rxd.mychat.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rxd.mychat.R;
import com.rxd.mychat.bean.SortModel;
import com.rxd.mychat.ui.activity.FriendMsgActivity;

import java.util.List;

import static com.rxd.mychat.utils.Consts.FRIEND_NAME;

/**
 * Created by Gray on 2017/7/10.
 * 通讯录adapter
 */

public class ContactAdapter extends BaseAdapter implements SectionIndexer{

    private List<SortModel> list = null;
    private Context mContext;

    public ContactAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        this.list = list;
    }


    public void updateListView(List<SortModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_list, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.contact_tv_name);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.contact_tv_letter);
            viewHolder.ivAvatar = (ImageView) view.findViewById(R.id.contact_iv_avatar);
            viewHolder.rlContact = (RelativeLayout) view.findViewById(R.id.rl_contact_info);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //获取当前这个人的下标值所在分类组
        int section = getSectionForPosition(position);

        //判断这条数据是否处于当前分类组中的第一条
        if(position == getPositionForSection(section)){
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        }else{
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        if (mContent.getBmAvatar() != null){
            Glide.with(mContext).load(Base64.decode(mContent.getBmAvatar(),Base64.DEFAULT)).
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.ivAvatar);
        }
        //viewHolder.ivAvatar.setImageBitmap(mContent.getBmAvatar());
        viewHolder.tvTitle.setText(mContent.getNickname());
        viewHolder.rlContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendMsgActivity.class);
                intent.putExtra(FRIEND_NAME, mContent.getName());
                mContext.startActivity(intent);
            }
        });

        return view;

    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        ImageView ivAvatar;
        RelativeLayout rlContact;
    }


    /**
     * 根据position获取section值
     * @param position
     * @return
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据sectionId获取当前分类组的第一个数据的下标值
     * @param section
     * @return
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    private String getAlpha(String str) {
        String  sortStr = str.trim().substring(0, 1).toUpperCase();
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
