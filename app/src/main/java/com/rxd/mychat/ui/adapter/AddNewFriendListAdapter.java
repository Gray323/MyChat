package com.rxd.mychat.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rxd.mychat.R;
import com.rxd.mychat.db.AddNewFriendBean;
import com.rxd.mychat.utils.ToastUtils;
import com.rxd.mychat.xmpp.XmppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gray on 2017/7/24.
 * 新的好友的申请的adapter
 */

public class AddNewFriendListAdapter extends RecyclerView.Adapter<AddNewFriendListAdapter.ViewHolder>{

    private Context context;
    private List<AddNewFriendBean> list;

    public AddNewFriendListAdapter(Context context, List<AddNewFriendBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        ViewHolder holder = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_new_friend_list, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String username = list.get(position).getMyName();
        int state = list.get(position).getState();
        holder.tvName.setText(username);
        if (state == 1){
            holder.tvState.setText(context.getResources().getString(R.string.agree_add_state));
        }else{
            holder.tvState.setVisibility(View.GONE);
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean is =  XmppUtils.agreeFriend(username);
                    if (is){
                        holder.tvState.setVisibility(View.VISIBLE);
                        holder.tvState.setText(context.getResources().getString(R.string.agree_add_state));
                        holder.btnAdd.setVisibility(View.GONE);
                    }else{
                        ToastUtils.showToast("添加失败，请稍后重试");
                    }
                }
            });
        }
        holder.rlList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.new_friend_list_iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.new_friend_list_tv_name)
        TextView tvName;
        @BindView(R.id.new_friend_list_add_state)
        TextView tvState;
        @BindView(R.id.rl_new_friend_list)
        RelativeLayout rlList;
        @BindView(R.id.btn_agree_new)
        Button btnAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
