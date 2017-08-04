package com.rxd.mychat.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rxd.mychat.R;
import com.rxd.mychat.bean.UserInfo;
import com.rxd.mychat.ui.activity.UserInfoActivity;
import com.rxd.mychat.utils.Consts;
import com.rxd.mychat.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gray on 2017/7/12.
 * 查找好友adapter
 */

public class FindNewFriendAdapter extends RecyclerView.Adapter<FindNewFriendAdapter.ViewHolder>{

    private Context context;
    private List<UserInfo> nameList;

    public FindNewFriendAdapter(Context context, List<UserInfo> nameList) {
        this.context = context;
        this.nameList = nameList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        ViewHolder holder = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_add_new_friend,parent,false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String username = nameList.get(position).getUsername().substring(1,nameList.get(position).getUsername().length() -1);
        holder.tvName.setText(username);
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra(Consts.USER_INFO, username);
                context.startActivity(intent);
            }
        });
        if (nameList.get(position).getAvatar() != null){
            holder.ivAvatar.setImageBitmap(Utils.getPicFromBytes(nameList.get(position).getAvatar(),new BitmapFactory.Options()));
        }
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.add_new_friend_avatar)
        ImageView ivAvatar;
        @BindView(R.id.add_new_friend_name)
        TextView tvName;
        @BindView(R.id.rl_new_friend_container)
        RelativeLayout rlContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
