package com.chunhuitech.reader.component;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class ArrowTextView extends android.support.v7.widget.AppCompatTextView {

    public ArrowTextView(Context context) {
        super(context);
    }

    public ArrowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArrowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);   //设置画笔抗锯齿
        paint.setStrokeWidth(2);    //设置线宽
        paint.setColor(Color.parseColor("#C0666666")); //设置线的颜色


        int width = getWidth();     //获取View的宽度
        int padding = 15;
        int height = getHeight();   //获取View的高度
        //框定文本显示的区域
        canvas.drawRoundRect(new RectF(getPaddingLeft() - padding,getPaddingTop() - 5,width - getPaddingRight() + padding,height - getPaddingBottom() + 10),10,10, paint);
        Path path = new Path();
        //以下是绘制文本的那个箭头
        path.moveTo(width / 2, height);// 三角形顶点
        path.lineTo(width / 2 - padding, height - getPaddingBottom() + 9);   //三角形左边的点
        path.lineTo(width / 2 + padding, height - getPaddingBottom() + 9);   //三角形右边的点
        path.close();
        canvas.drawPath(path, paint);
        super.onDraw(canvas);
    }
}
