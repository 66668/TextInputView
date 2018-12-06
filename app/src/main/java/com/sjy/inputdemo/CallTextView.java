package com.sjy.inputdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

public class CallTextView extends TextView {
    private Paint mPaint;
    private Bitmap bitmap;

    public CallTextView(Context context) {
        super(context);
    }

    public CallTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CallTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CallTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //获取属性
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,R.styleable.BorderTextView,defStyleAttr,0);
        int n = ta.getIndexCount();
        for(int i = 0; i < n ;i++){
            int attr = ta.getIndex(i);
            switch (attr){
                case R.styleable.BorderTextView_borderColor:
                    boderColor = ta.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.BorderTextView_type:
                    boderType = ta.getString(attr);

                    break;
            }
        }
        ta.recycle();
        //默认矩形
        if(boderType == null){
            boderType = "rect";
        }
        mPaint = new Paint();
        mPaint.setColor(boderColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        mPaint.setStrokeWidth(4);

        // initView();
    }





    private void drawRoundRect(){
        bitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        RectF rect = new RectF(  0,
                0,
                getMeasuredWidth(),
                getMeasuredHeight());
        canvas.drawRoundRect(rect,
                30, 30,
                mPaint);
    }

    private void drawRect( ){
        bitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(
                0,
                0,
                getMeasuredWidth(),
                getMeasuredHeight(),
                mPaint);
    }

    private void drawOval(){
        RectF rel1 = new RectF(10,10,getMeasuredWidth()-10,getMeasuredHeight()-10);
        //绘制椭圆

        bitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawOval(rel1,mPaint);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = getPaint();
        float size = p.getTextSize();
        p.setTextSize(size/2);
        int x,y;
        Rect bound = new Rect();
        p.getTextBounds(getText().toString(),0,getText().toString().length(),bound);
        x = getMeasuredWidth()/2 - bound.width()/2;
        y = getMeasuredHeight()/2 + bound.height()/2;


        if(boderType.equals("rect")){
            drawRect();
        }else if(boderType.equals("roundrect")){
            drawRoundRect();
        }else if(boderType.equals("oval")){
            drawOval();
        }else{
            drawRect();
        }
        //drawRoundRect();

        canvas.drawBitmap(bitmap,0,0,null);
        canvas.save();
        // 绘制文字前平移10像素

        canvas.drawText(getText().toString(),x,y,p);
        // 父类完成的方法，即绘制文本
        // super.onDraw(canvas);
        canvas.restore();
    }

}
