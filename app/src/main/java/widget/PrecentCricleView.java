package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hdu.myship.blackstone.R;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/7 上午11:00
 */
public class PrecentCricleView extends View {

    private static final int DEFAULT_SIZE = 100;

    //圆弧厚度
    private int thickness = 20;

    //默认圆环颜色 灰色
    private int circleBackgroundColor = Color.GRAY;

    //圆弧 默认蓝色
    private int arcBackgroundColor = Color.BLUE;

    //文本颜色
    private int textColor = Color.BLACK;

    //文本字体大小
    private float textSize = 20;


    //圆弧起点
    private float startAngdle = 90;

    //圆弧长度
    private float sweepAngle = 90;


    private String text=null;

    private Paint circilePaint = new Paint();
    private Paint arcPaint = new Paint();
    private Paint textPaint = new Paint();



    public PrecentCricleView(Context context) {
        this(context, null);
    }

    public PrecentCricleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrecentCricleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.PrecentCricleView);

        thickness = typedArray.getInteger(R.styleable.PrecentCricleView_arcThickness,20);

        circleBackgroundColor = typedArray.getColor(R.styleable.PrecentCricleView_circleBackgroundColor,circleBackgroundColor);
        arcBackgroundColor = typedArray.getColor(R.styleable.PrecentCricleView_arcBackgroundColor,arcBackgroundColor);
        textColor = typedArray.getColor(R.styleable.PrecentCricleView_textColor,textColor);

        textSize = typedArray.getDimensionPixelSize(R.styleable.PrecentCricleView_textSize,20);
        startAngdle = typedArray.getFloat(R.styleable.PrecentCricleView_startAngdle,90);
        sweepAngle = typedArray.getFloat(R.styleable.PrecentCricleView_sweepAngle,0);

        text = typedArray.getString(R.styleable.PrecentCricleView_text);

        //回收
        typedArray.recycle();

        //初始化画笔
        initPaint();
    }

    private void initPaint() {

        circilePaint.setStrokeWidth(thickness);
        circilePaint.setStyle(Paint.Style.STROKE);
        circilePaint.setColor(circleBackgroundColor);
        circilePaint.setAntiAlias(true);

        arcPaint.setStrokeWidth(thickness);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setColor(arcBackgroundColor);
        arcPaint.setAntiAlias(true);

        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width=getSize(DEFAULT_SIZE,widthMeasureSpec);
        int height=getSize(DEFAULT_SIZE,heightMeasureSpec);

        int size = width>height?height:width;

        setMeasuredDimension(size,size);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆环
        canvas.drawCircle(getWidth()/2.0f,getWidth()/2.0f,(getWidth()- thickness)/2.0f,circilePaint);


        //画圆弧
        RectF rectF=new RectF(0+ thickness /2,0+ thickness /2,getWidth()- thickness /2,getHeight()- thickness /2);
        canvas.drawArc(rectF,startAngdle,sweepAngle,false,arcPaint);



        //画文字
        if (text != null && text.length() != 0) {

            Rect rect=new Rect();
            textPaint.getTextBounds(text,0,text.length(),rect);
            canvas.drawText(text,getWidth()/2.0f-rect.width()/2,getWidth()/2.0f+rect.height()/2,textPaint);

        }

    }

    private int getSize(int defaultSize, int measureSpec) {

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        int actualSize = defaultSize;

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                actualSize = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                actualSize = size;
                break;
            case MeasureSpec.EXACTLY:
                actualSize = size;
                break;
        }

        return actualSize;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
        invalidate();
    }

    public void setCircleBackgroundColor(int circleBackgroundColor) {
        this.circleBackgroundColor = circleBackgroundColor;
        invalidate();
    }

    public void setArcBackgroundColor(int arcBackgroundColor) {
        this.arcBackgroundColor = arcBackgroundColor;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setStartAngdle(float startAngdle) {
        this.startAngdle = startAngdle;
        invalidate();
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }
}
