package com.rxd.mychat.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.widget.ClipZoomImageView;
import com.rxd.mychat.xmpp.XmppUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.rxd.mychat.utils.Consts.CUT_IMAGE_PATH;

public class CutAvatarPhotoActivity extends BaseActivity {

    @BindView(R.id.title_cut_avatar_img_back)
    ImageView ImgBack;
    @BindView(R.id.btn_cut_img_ok)
    Button btnOk;
    @BindView(R.id.czv_image)
    ClipZoomImageView zoomImageView;

    @Override
    protected void initData() {
        String path = getIntent().getStringExtra(CUT_IMAGE_PATH);
        Drawable drawable = Drawable.createFromPath(path);
        zoomImageView.setImageDrawable(drawable);
    }

    @Override
    protected void initView() {
        mContext = this;
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cut_avatar_photo;
    }

    @OnClick({R.id.btn_cut_img_ok,R.id.title_cut_avatar_img_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_cut_avatar_img_back:
                finish();
                break;
            case R.id.btn_cut_img_ok:
                Bitmap bitmap = zoomImageView.clip();
                XmppUtils.setAvatar(bitmap);
                finish();
                AppManager.getAppManager().finishActivity(ChooseAvatarPhotoActivity.class);
                break;
        }
    }


}
