package com.rxd.mychat.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rxd.mychat.R;
import com.rxd.mychat.bean.Folder;
import com.rxd.mychat.bean.Image;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/31.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder>{


    private Context context;
    private ArrayList<Folder> mFolders;
    private LayoutInflater inflater;
    private int mSelectedItem;
    private onFolderSelectListener listener;

    public FolderAdapter(Context context, ArrayList<Folder> mFolders) {
        this.context = context;
        this.mFolders = mFolders;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_choose_photo_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Folder folder = mFolders.get(position);
        ArrayList<Image> images = folder.getImages();
        holder.tvFolderName.setText(folder.getName());
        holder.ivSelected.setVisibility(mSelectedItem == position ? View.VISIBLE : View.GONE);
        if (images != null && !images.isEmpty()){
            holder.tvPhotoSize.setText(images.size() + "张");
            Glide.with(context).load(new File(images.get(0).getPath()))
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.ivImage);
        }else{
            holder.tvPhotoSize.setText("0张");
            holder.ivImage.setImageBitmap(null);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = holder.getAdapterPosition();
                notifyDataSetChanged();
                if (listener != null){
                    listener.onFolderSelect(folder);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFolders == null ? 0 : mFolders.size();
    }

    public void setOnFolderSelectListener(onFolderSelectListener listener){
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_photoimage)
        ImageView ivImage;
        @BindView(R.id.iv_photo_select)
        ImageView ivSelected;
        @BindView(R.id.tv_folder_photo_name)
        TextView tvFolderName;
        @BindView(R.id.tv_folder_photo_size)
        TextView tvPhotoSize;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface onFolderSelectListener{
        void onFolderSelect(Folder folder);
    }

}
