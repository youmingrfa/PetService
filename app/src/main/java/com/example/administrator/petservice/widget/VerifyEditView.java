package com.example.administrator.petservice.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

import com.example.administrator.petservice.R;


/**
 * Created by hejiyang on 2016/4/22.
 * 用作忘记密码中的验证码输入的控件
 */
public class VerifyEditView extends EditText {
    private int mBorderColor;
    private int mBorderWidth;
    private int mBorderRadius;

    private int mCodeLength;
    private int mCodeColor;
    private int mCodeSize;

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private RectF mRectF;

    private String mTextContent;
    private int mTextLength;
    public VerifyEditView(Context context) {
        this(context,null);
    }

    public VerifyEditView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VerifyEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VerifyEditView,defStyleAttr,0);
        mBorderColor = a.getColor(R.styleable.VerifyEditView_VerifyBorderColor, Color.BLACK);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.VerifyEditView_VerifyBorderWidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,getResources().getDisplayMetrics()));
        mBorderRadius = a.getDimensionPixelSize(R.styleable.VerifyEditView_VerifyBorderRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,getResources().getDisplayMetrics()));

        mCodeColor = a.getColor(R.styleable.VerifyEditView_VerifyCodeTextColor,Color.BLACK);
        mCodeLength = a.getInt(R.styleable.VerifyEditView_VerifyCodeLength,6);
        mCodeSize = a.getDimensionPixelSize(R.styleable.VerifyEditView_VerifyCodeTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,24,getResources().getDisplayMetrics()));
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        mWidth = getWidth();
        mHeight = getHeight();
        mRectF = new RectF(0,0,mWidth,mHeight);
        //圆角矩形
        //mPaint.setStyle(Paint.Style.STROKE);
        //mPaint.setColor(mBorderColor);
        //mPaint.setStrokeWidth(mBorderWidth);
        //canvas.drawRoundRect(mRectF,mBorderRadius,mBorderRadius,mPaint);

        //分割线
        float offset = mRectF.width() / mCodeLength;
        float lineX;
        mPaint.setStrokeWidth(1);
        for (int i=1;i<mCodeLength;i++){
            lineX = mRectF.left + offset * i;
            canvas.drawLine(lineX,0,lineX,mRectF.height(),mPaint);
        }

        mPaint.setColor(mCodeColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mCodeSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float baseLine = (mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top)/2;
        float codeX;
        //更新数字
        for (int i=0;i<mTextLength;i++){
            codeX = mRectF.left + offset * i + offset / 2;
            canvas.drawText(mTextContent.substring(i,i+1),codeX,baseLine,mPaint);
        }

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.mTextContent = text.toString();
        this.mTextLength = text.toString().length();
        invalidate();
    }
}
