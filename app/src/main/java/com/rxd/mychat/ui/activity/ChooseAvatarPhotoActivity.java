package com.rxd.mychat.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.bean.Folder;
import com.rxd.mychat.bean.Image;
import com.rxd.mychat.ui.adapter.FolderAdapter;
import com.rxd.mychat.ui.adapter.ImageItemAdapter;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.DateUtils;
import com.rxd.mychat.utils.FileSaveUtil;
import com.rxd.mychat.utils.FileUtils;
import com.rxd.mychat.utils.ImageCheckoutUtil;
import com.rxd.mychat.utils.PictureUtil;
import com.rxd.mychat.utils.SDCardImageUtils;
import com.rxd.mychat.utils.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.rxd.mychat.utils.Consts.CUT_IMAGE_PATH;

public class ChooseAvatarPhotoActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    FrameLayout btnBack;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.rv_image)
    RecyclerView rvImage;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.masking)
    View masking;
    @BindView(R.id.rv_folder)
    RecyclerView rvFolder;
    @BindView(R.id.tv_folder_name)
    TextView tvFolderName;
    @BindView(R.id.btn_folder)
    FrameLayout btnFolder;
    @BindView(R.id.rl_bottom_bar)
    RelativeLayout rlBottomBar;

    private static final int PERMISSION_REQUEST_CODE = 0X00000011;//读取内存卡权限请求码
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0X00000012;//相机权限请求码
    private static final int REQUEST_CAMERA = 1;//相机权限请求码
    private boolean isToSettings = false;//是否跳去设置界面
    private ArrayList<Folder> mFolders;//图片文件夹数组
    private boolean isInitFolder;//是否初始化文件夹列表
    private Folder mFolder;//当前的folder
    private boolean isOpenFolder;//是否打卡了文件夹列表
    private boolean isShowTime;//是否显示时间
    //照相机图片路径地址
    private String camPicPath;
    private String rootPath;
    //图片大小的最大值
    private static final int IMAGE_SIZE = 100 * 1024;// 300kb
    private GridLayoutManager manager;
    private ImageItemAdapter mAdapter;


    private Handler mHideHandler = new Handler();
    private Runnable mHide = new Runnable() {
        @Override
        public void run() {
            hideTime();
        }
    };

    @Override
    protected void initData() {
        rvImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                changeTime();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                changeTime();
            }
        });

    }

    @Override
    protected void initView() {
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        checkPermissionAndLoadImages();
        initImageList();
        hideFolderList();
        hideTime();
        //设置照相机图片的点击事件
        mAdapter.setCameraCallback(new ImageItemAdapter.cameraCallback() {
            @Override
            public void takeCamera() {
                checkCameraPermission();
            }
        });
        FileUtils.init();
        rootPath = FileUtils.getFileDir()+File.separator;
    }

    /**
     * 判断是否有相机权限
     */
    private void checkCameraPermission(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return;
        }
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED){
            //有权限，开启相机
            openCamera();
        } else{
            //没有权限，申请权限
            ActivityCompat.requestPermissions(ChooseAvatarPhotoActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 打开相机
     */
    private void openCamera(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            camPicPath = getSavePicPath();
            File nowFile = new File(camPicPath);
            FileUtils.startActionCapture(ChooseAvatarPhotoActivity.this,nowFile,REQUEST_CAMERA);
        }else{
            ToastUtils.showToast("请检查内存卡");
        }
    }

    //图片路径地址和文件名称
    private String getSavePicPath(){
        File path = new File(rootPath);
        if (!path.exists()) {
            path.mkdirs();
        }
        String piturePath = System.currentTimeMillis() + ".jpg";
        File file = new File(path, piturePath);
        if (file.exists()) {
            file.delete();
        }
        return rootPath+piturePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CAMERA){
                FileInputStream is = null;
                try {
                    is = new FileInputStream(camPicPath);
                    File camFile = new File(camPicPath);
                    if(camFile.exists()){
                        int size = ImageCheckoutUtil.getImageSize(ImageCheckoutUtil.getLoacalBitmap(camPicPath));
                        if(size > IMAGE_SIZE){
                            showDialog(camPicPath);
                        }else{
                            gotoCut(camPicPath);
                        }
                    }else{
                        ToastUtils.showToast("该文件不存在");
                    }
                } catch (FileNotFoundException e) {
                    ToastUtils.showToast("出错了");
                    e.printStackTrace();
                }finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void gotoCut(String camPicPath) {
        Intent intent = new Intent(mContext, CutAvatarPhotoActivity.class);
        intent.putExtra(CUT_IMAGE_PATH, camPicPath);
        startActivity(intent);
    }

    private void showDialog(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
            try {
                String compressPicPath = getSavePicPath();
                Bitmap bitmap = PictureUtil.compressSizeImage(path);
                boolean isSave = FileSaveUtil.saveBitmap(bitmap, compressPicPath);
                File fileafter = new File(compressPicPath);
                if (fileafter.exists() && isSave) {
                    gotoCut(compressPicPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        }).start();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_photo;
    }

    @OnClick({R.id.btn_back,R.id.btn_folder})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_folder:
                if(isInitFolder){
                    if (isOpenFolder){
                        closeFolder();
                    }else{
                        openFolder();
                    }
                }
        }
    }



    /**
     * 初始化图片列表
     */
    private void initImageList(){
        //判断屏幕方向
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            manager = new GridLayoutManager(mContext, 3);
        }else {
            manager = new GridLayoutManager(mContext, 5);
        }
        rvImage.setLayoutManager(manager);
        mAdapter = new ImageItemAdapter(mContext);
        rvImage.setAdapter(mAdapter);
        ((SimpleItemAnimator)rvImage.getItemAnimator()).setSupportsChangeAnimations(false);
        if (mFolders != null && !mFolders.isEmpty()){
            setFolder(mFolders.get(0));
        }
    }

    /**
     * 判断是否有权限WRITE_EXTERNAL_STORAGE
     */
    private void checkPermissionAndLoadImages(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return;
        }
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED){
            //有权限，加载图片
            loadImageForSDCard();
        } else{
            //没有权限，申请权限
            ActivityCompat.requestPermissions(ChooseAvatarPhotoActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                loadImageForSDCard();
            }else{
                showExceptionDialog("相册");
            }
        }else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else{
                showExceptionDialog("相机");
            }
        }
    }

    /**
     * 如果没有权限时，显示一个提示dialog
     */
    private void showExceptionDialog(String message){
        new AlertDialog.Builder(mContext)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage("该"+message+"需要赋予访问存储的权限，请到“设置”>“应用”>“权限”中配置权限。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startAppSetting();
                isToSettings = true;
            }
        }).show();
    }

    /**
     * 从sd卡加载图片
     */
    private void loadImageForSDCard(){
        SDCardImageUtils.loadImageForSDCard(mContext, new SDCardImageUtils.DataCallback() {
            @Override
            public void onSuccess(ArrayList<Folder> folders) {
                Logger.d("folders: "+ folders);
                mFolders = folders;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mFolders != null && !mFolders.isEmpty()){
                            initFolderList();
                            setFolder(mFolders.get(0));
                        }
                    }
                });
            }
        });
    }

    /**
     * 初始化图片文件夹列表
     */
    private void initFolderList(){
        if (mFolders != null && !mFolders.isEmpty()){
            isInitFolder = true;
            rvFolder.setLayoutManager(new LinearLayoutManager(mContext));
            FolderAdapter adapter = new FolderAdapter(mContext, mFolders);
            adapter.setOnFolderSelectListener(new FolderAdapter.onFolderSelectListener() {
                @Override
                public void onFolderSelect(Folder folder) {
                    setFolder(folder);
                    closeFolder();
                }
            });
            rvFolder.setAdapter(adapter);
        }
    }

    /**
     * 设置选中的文件夹，同时刷新图片列表
     * @param folder
     */
    private void setFolder(Folder folder){
        if (folder != null && mAdapter != null && !folder.equals(mFolder)){
            mFolder = folder;
            tvFolderName.setText(folder.getName());
            rvImage.scrollToPosition(0);
            mAdapter.refresh(folder.getImages());
        }
    }

    /**
     * 关闭文件夹选项列表
     */
    private void closeFolder(){
        if (isOpenFolder){
            masking.setVisibility(View.GONE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(rvFolder, "translationY", 0, rvFolder.getHeight()).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rvFolder.setVisibility(View.GONE);
                }
            });
            animator.start();
            isOpenFolder = false;
        }
    }

    /**
     * 打开文件夹选项列表
     */
    private void openFolder(){
        if (!isOpenFolder){
            masking.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(rvFolder, "translationY",rvFolder.getHeight(), 0).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rvFolder.setVisibility(View.VISIBLE);
                }
            });
            animator.start();
            isOpenFolder = true;
        }
    }

    /**
     * 默认隐藏文件夹列表
     */
    private void hideFolderList(){
        rvFolder.post(new Runnable() {
            @Override
            public void run() {
                rvFolder.setTranslationY(rvFolder.getHeight());
                rvFolder.setVisibility(View.GONE);
            }
        });
    }

    private void changeTime(){
        int firstVisibleItem = getFirstVisibleItem();
        if (firstVisibleItem > 0 && firstVisibleItem < mAdapter.getData().size()){
            Image image = mAdapter.getData().get(firstVisibleItem);
            String time = DateUtils.getImageTime(image.getTime() * 1000);
            tvTime.setText(time);
            showTime();
            mHideHandler.removeCallbacks(mHide);
            mHideHandler.postDelayed(mHide, 1500);
        }
    }

    /**
     * 显示时间的方法
     */
    private void showTime(){
        if (!isShowTime){
            ObjectAnimator.ofFloat(tvTime, "alpha", 0, 1).setDuration(300).start();
            isShowTime = true;
        }
    }

    /**
     * 隐藏时间的方法
     */
    private void hideTime(){
        if (isShowTime){
            Logger.w("hideTime");
            ObjectAnimator.ofFloat(tvTime, "alpha", 1, 0).setDuration(300).start();
            isShowTime = false;
        }
    }

    private int getFirstVisibleItem(){
        return  manager.findFirstVisibleItemPosition();
    }


    /**
     * 跳转到设置界面
     */
    private void startAppSetting(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isToSettings){
            isToSettings = false;
            checkPermissionAndLoadImages();
        }
    }

    /**
     * 按返回键时,如果文件夹列表处于打开状态，则关闭
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && isOpenFolder) {
            closeFolder();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
