package com.rxd.mychat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.StaticLayout;
import android.util.AttributeSet;

import com.rxd.mychat.R;

/**
 * Created by Administrator on 2017/7/7.
 */

public class BottomRadioButton extends AppCompatRadioButton{

    private Paint mFocusPaint;
    private Paint mTextPaint;
    private Paint mDeFocusPaint;
    private Paint mPointPaint;

    private int iconWidth;
    private int iconPadding;
    private int iconHeight;

    private Bitmap mDefocusBitmap;
    private Bitmap mFocusBitmap;
    private Bitmap mPointBitmap;

    private int mAlpha;
    private int mColor;
    private float mFontHeight;
    private float mTextWidth;
    private boolean hasNewInfo = false;

    public BottomRadioButton(Context context) {
        this(context, null);
    }

    public BottomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BottomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs){
        //初始化画笔
        mFocusPaint = new Paint();
        mTextPaint = new Paint();
        mDeFocusPaint = new Paint();
        mPointPaint= new Paint();
        mFocusPaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);
        mDeFocusPaint.setAntiAlias(true);
        mPointPaint.setAntiAlias(true);

        //获取自定义元素
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomRadioButton);
        Drawable mFocusDrawable = a.getDrawable(R.styleable.BottomRadioButton_focus_icon);
        Drawable mDeFocusDrawable = a.getDrawable(R.styleable.BottomRadioButton_defocus_icon);
        mColor = a.getColor(R.styleable.BottomRadioButton_focus_color, Color.GREEN);
        a.recycle();

        //隐藏button的图片
        setButtonDrawable(null);


        if (mDeFocusDrawable != null){
            //在顶部设置图片
            setCompoundDrawablesWithIntrinsicBounds(null,mDeFocusDrawable,null,null);
            //获取在顶部的图片
            mDeFocusDrawable = getCompoundDrawables()[1];
        }

        //假如没有设置focus_icon或者defocus_icon，则抛出异常
        if (mDeFocusDrawable == null || mFocusDrawable == null){
            throw  new RuntimeException("'focus_icon' or 'defocus_icon' is not defined");
        }

        iconWidth = mDeFocusDrawable.getIntrinsicWidth();
        iconHeight = mDeFocusDrawable.getIntrinsicHeight();

        mDeFocusDrawable.setBounds(0, 0, iconWidth, iconHeight);
        mFocusDrawable.setBounds(0, 0, iconWidth, iconHeight);

        iconPadding = getCompoundDrawablePadding();

        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        //TODO 未理解
        mFontHeight = (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
        mTextWidth = StaticLayout.getDesiredWidth(getText(), getPaint());

        mDefocusBitmap = getBitmapFromDrawable(mDeFocusDrawable);
        mFocusBitmap = getBitmapFromDrawable(mFocusDrawable);
        mPointBitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.red_point);

        if (isChecked()){
            mAlpha = 255;
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable){
        Bitmap bitmap = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (drawable instanceof BitmapDrawable){
            drawable.draw(canvas);
            return bitmap;
        }else{
            throw  new RuntimeException("The Drawable must be an instance of BitmapDrawable");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDeFocusIcon(canvas);
        drawFocusIcon(canvas);
        drawDeFocusText(canvas);
        drawFocusText(canvas);
        if (hasNewInfo){
            drawRedPoint(canvas);
        }
    }

    private void drawRedPoint(Canvas canvas){
        mPointPaint.setColor(Color.RED);
        canvas.drawBitmap(mPointBitmap,(getWidth() - iconWidth) / 2 + iconWidth,getPaddingTop(), mPointPaint);
    }

    private void drawDeFocusIcon(Canvas canvas){
        mDeFocusPaint.setAlpha(255 - mAlpha);
        canvas.drawBitmap(mDefocusBitmap, (getWidth() - iconWidth) / 2, getPaddingTop(), mDeFocusPaint);
    }

    private void drawFocusIcon(Canvas canvas){
        mFocusPaint.setAlpha(mAlpha);
        canvas.drawBitmap(mFocusBitmap, (getWidth() - iconWidth) / 2, getPaddingTop(), mFocusPaint);
    }

    private void drawDeFocusText(Canvas canvas){
        mTextPaint.setColor(getCurrentTextColor());
        mTextPaint.setAlpha(255 - mAlpha);
        mTextPaint.setTextSize(getTextSize());
        canvas.drawText(getText().toString(), getWidth() / 2 - mTextWidth / 2,
                iconHeight + iconPadding + getPaddingTop() + mFontHeight, mTextPaint);
    }

    private void drawFocusText(Canvas canvas){
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(mAlpha);
        canvas.drawText(getText().toString(), getWidth() / 2 - mTextWidth / 2,
                iconHeight + iconPadding + getPaddingTop() + mFontHeight, mTextPaint);
    }

    /**
     * 更新alpha
     * @param alpha
     */
    public void updateAlpha(float alpha){
        mAlpha = (int) alpha;
        invalidate();
    }

    public void setRadioButtonChecked(boolean isChecked){
        setChecked(isChecked);
        if (isChecked){
            mAlpha = 255;
        }else{
            mAlpha = 0;
        }

        hasNewInfo = false;

        invalidate();
    }

    public void setHasNewInfo(boolean hasNewInfo){
        this.hasNewInfo = hasNewInfo;
    }

}
