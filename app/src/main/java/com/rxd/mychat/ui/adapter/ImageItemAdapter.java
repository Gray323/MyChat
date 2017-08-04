package com.rxd.mychat.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rxd.mychat.R;
import com.rxd.mychat.bean.Image;
import com.rxd.mychat.ui.activity.CutAvatarPhotoActivity;
import com.rxd.mychat.widget.SquareImageView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rxd.mychat.utils.Consts.CUT_IMAGE_PATH;

/**
 * Created by Administrator on 2017/7/31.
 */

public class ImageItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<Image> images;
    private LayoutInflater inflater;
    private cameraCallback cameraCallback;

    private static final int TYPE_TAKE = 0;
    private static final int TYPE_IMAGE = 1;

    public ImageItemAdapter(Context context ) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public ArrayList<Image> getData(){
        return images;
    }

    public void refresh(ArrayList<Image> data){
        images = data;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_TAKE;
        }else{
            return TYPE_IMAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TAKE){
            return new TakeViewHolder(inflater.inflate(R.layout.item_choose_photo_take, parent, false));
        }else{
            return new PhotoViewHolder(inflater.inflate(R.layout.item_choolse_photo_image, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TakeViewHolder){
            ((TakeViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cameraCallback != null){
                        cameraCallback.takeCamera();
                    }
                }
            });
        }else{
            final Image image = images.get(position);
            Glide.with(context).load(new File(image.getPath())).diskCacheStrategy(DiskCacheStrategy.NONE).
                 into(((PhotoViewHolder)holder).svImage);
            ((PhotoViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,CutAvatarPhotoActivity.class);
                    intent.putExtra(CUT_IMAGE_PATH,image.getPath());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }


    static class PhotoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_image_item_image)
        SquareImageView svImage;
        @BindView(R.id.iv_image_item_masking)
        SquareImageView svMasking;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class TakeViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.fl_photo_item_take)
        FrameLayout flTake;

        public TakeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setCameraCallback(cameraCallback cameraCallback){
        this.cameraCallback = cameraCallback;
    };

    public interface cameraCallback{
        void takeCamera();
    }

}
