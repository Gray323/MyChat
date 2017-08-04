package com.rxd.mychat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Gray on 2017/7/31.
 * 裁剪的正方形白色边框
 */

public class ClipImageBorderView extends View{

    private int mBorderColor = Color.parseColor("#FFFFFF");//边框的颜色，默认为白色
    private int mBorderWidth = 1;//边框的宽度
    private Paint mPaint;
    private int width;
    private int height;
    //正方形的宽高长度的一半
    private int innerWidth = 400;

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height =  h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画布平移到中心点，绘制正中的正方形
        canvas.translate(width / 2, height / 2);
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(-innerWidth, -innerWidth, innerWidth, innerWidth, mPaint);

        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Paint.Style.FILL);
        //绘制左边的矩形
        canvas.translate(-width / 2, -height / 2);
        canvas.drawRect(0, 0, (getWidth() - innerWidth * 2) / 2, getHeight(), mPaint);
        //绘制右边的矩形
        canvas.translate(getWidth(), 0);
        canvas.drawRect(-(getWidth() - innerWidth * 2) / 2, 0, 0, getHeight(), mPaint);
        //绘制上边的矩形
        canvas.translate(-((getWidth() - innerWidth * 2) / 2 + innerWidth * 2) , 0);
        canvas.drawRect(0, 0, innerWidth * 2, (getHeight() - innerWidth * 2) / 2, mPaint);
        //绘制下边的矩形
        canvas.translate(0, getHeight());
        canvas.drawRect(0, -(getHeight() - innerWidth * 2) / 2, innerWidth * 2, 0 ,mPaint);

    }

}
