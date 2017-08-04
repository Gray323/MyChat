package com.rxd.mychat.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

/**
 * Created by Administrator on 2017/7/7.
 */

public class BottomRadioGroup extends RadioGroup implements ViewPager.OnPageChangeListener{

    private ViewPager mViewPager;

    public BottomRadioGroup(Context context) {
        super(context);
    }

    public BottomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置viewpager
     * @param viewPager
     */
    public void setViewPager(ViewPager viewPager){
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++){
            final int position = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setClickedViewChecked(position);
                    if (mViewPager != null){
                        mViewPager.setCurrentItem(position, false);
                    }
                }
            });
        }
    }

    private void setClickedViewChecked(int position){
        for (int i = 0; i < getChildCount(); i++){
            ((BottomRadioButton)getChildAt(i)).setRadioButtonChecked(false);
        }
        ((BottomRadioButton)getChildAt(position)).setRadioButtonChecked(true);
    }

    private void setSelectedViewChecked(int position){
        for (int i = 0; i < getChildCount(); i++){
            ((BottomRadioButton)getChildAt(i)).setChecked(false);
        }
        ((BottomRadioButton)getChildAt(position)).setChecked(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        updateGradient(position, positionOffset);
    }

    private void updateGradient(int position, float offset){
        if (offset > 0){
            ((BottomRadioButton)getChildAt(position)).updateAlpha(255 * (1 - offset));
            ((BottomRadioButton)getChildAt(position + 1)).updateAlpha(255 * offset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
