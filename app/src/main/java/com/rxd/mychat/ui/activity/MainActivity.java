package com.rxd.mychat.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.rxd.mychat.R;
import com.rxd.mychat.base.BaseActivity;
import com.rxd.mychat.ui.fragment.AboutMeFragment;
import com.rxd.mychat.ui.fragment.ChatListFragment;
import com.rxd.mychat.ui.fragment.ContactFragment;
import com.rxd.mychat.ui.fragment.DiscoverFragment;
import com.rxd.mychat.utils.AppManager;
import com.rxd.mychat.utils.SharePreferencesUtils;
import com.rxd.mychat.utils.ToastUtils;
import com.rxd.mychat.widget.BottomRadioButton;
import com.rxd.mychat.widget.BottomRadioGroup;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.rxd.mychat.utils.Consts.IS_FIRST_RUN;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity{

    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.bottom_radio_group)
    BottomRadioGroup bottomRadioGroup;
    @BindView(R.id.main_viewpager)
    ViewPager viewPager;

    @BindView(R.id.brb_chat)
    BottomRadioButton brbChat;
    @BindView(R.id.brb_contact)
    BottomRadioButton brbContact;
    @BindView(R.id.brb_discover)
    BottomRadioButton brbDiscover;
    @BindView(R.id.brb_about_me)
    BottomRadioButton brbAboutme;


    @Override
    protected void initData() {
        //设置toolbar
        if (commonToolbar != null) {
            setTitle("聊天");
            setSupportActionBar(commonToolbar);
        }
    }

    @Override
    protected void initView() {
        boolean isFirst = SharePreferencesUtils.getShareBooleanDataTrue(IS_FIRST_RUN);
        Logger.d(isFirst);
        if (isFirst){
            SharePreferencesUtils.setShareBooleanData(IS_FIRST_RUN,false);
        }
        AppManager.getAppManager().addActivity(this);
        List<Fragment> list = new ArrayList<>();
        list.add(new ChatListFragment());
        list.add(ContactFragment.newInstance(isFirst));
        list.add(new DiscoverFragment());
        list.add(new AboutMeFragment());

        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), list));
        bottomRadioGroup.setViewPager(viewPager);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * 显示item中的图片；
     *
     * @param view
     * @param menu
     * @return
     */
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_toolbar_search:
                ToastUtils.showToast("搜索");
                break;
            case R.id.action_toolbar_chatroom:
                ToastUtils.showToast(getResources().getString(R.string.toolbar_chatroom));
                break;
            case R.id.action_bar_add_friend:
                ToastUtils.showToast(getResources().getString(R.string.toolbar_add_friend));
                break;
            case R.id.action_bar_qr:
                ToastUtils.showToast(getResources().getString(R.string.toolbar_qr));
                break;
            case R.id.action_bar_payment:
                ToastUtils.showToast(getResources().getString(R.string.toolbar_payment));
                break;
            case R.id.action_bar_help:
                ToastUtils.showToast(getResources().getString(R.string.toolbar_help));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class PageAdapter extends FragmentPagerAdapter{

        private List<Fragment> mData;

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        public PageAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            mData = data;
        }

        @Override
        public Fragment getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getCount() {
            return mData.size();
        }
    }

}
