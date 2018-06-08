package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kefan.blackstone.R;


/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/6 上午12:28
 */
public class HeaderBar extends RelativeLayout{

    private static final int DEFAULT_COLOR=0xFFFFFFFF;
    private static final int DEFAULT_TEXT_SIZE=14;
    private static final int DEFAULT_MARGIN=0;

    private boolean showLeftText;
    private boolean showLeftIcon;
    private boolean showCenterText;
    private boolean showRightText;
    private boolean showRightIcon;

    private int leftTextColor;
    private int centerTextColor;
    private int rightTextColor;

    private String leftText;
    private String centerText;
    private String rightText;

    private float leftTextSize;
    private float centerTextSize;
    private float rightTextSize;

    private Drawable leftIcon;
    private Drawable rightIcon;

    private LinearLayout left;
    private LinearLayout center;
    private LinearLayout right;

    private TextView leftTextView;
    private TextView centerTextView;
    private TextView rightTextView;

    private ImageView leftImageView;
    private ImageView rightImageView;

    private int leftMargin;
    private int rightMargin;




    public HeaderBar(Context context) {
        this(context,null);
    }

    public HeaderBar(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public HeaderBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.HeaderBar);

        showLeftText=typedArray.getBoolean(R.styleable.HeaderBar_showLeftText,true);
        showLeftIcon=typedArray.getBoolean(R.styleable.HeaderBar_showLeftIcon,true);
        showCenterText=typedArray.getBoolean(R.styleable.HeaderBar_showCenterText,true);
        showRightText=typedArray.getBoolean(R.styleable.HeaderBar_showRightText,true);
        showRightIcon=typedArray.getBoolean(R.styleable.HeaderBar_showRightIcon,true);

        leftTextColor=typedArray.getColor(R.styleable.HeaderBar_leftTextColor,DEFAULT_COLOR);
        centerTextColor=typedArray.getColor(R.styleable.HeaderBar_centerTextColor,DEFAULT_COLOR);
        rightTextColor=typedArray.getColor(R.styleable.HeaderBar_rightTextColor,DEFAULT_COLOR);

        leftText=typedArray.getString(R.styleable.HeaderBar_leftText);
        centerText=typedArray.getString(R.styleable.HeaderBar_centerText);
        rightText=typedArray.getString(R.styleable.HeaderBar_rightText);

        leftTextSize=typedArray.getDimensionPixelSize(R.styleable.HeaderBar_leftTextSize,DEFAULT_TEXT_SIZE);
        centerTextSize=typedArray.getDimensionPixelSize(R.styleable.HeaderBar_centerTextSize,DEFAULT_TEXT_SIZE);
        rightTextSize=typedArray.getDimensionPixelSize(R.styleable.HeaderBar_rightTextSize,DEFAULT_TEXT_SIZE);

        leftIcon=typedArray.getDrawable(R.styleable.HeaderBar_leftIcon);
        rightIcon=typedArray.getDrawable(R.styleable.HeaderBar_rightIcon);

        leftMargin=typedArray.getLayoutDimension(R.styleable.HeaderBar_left_margin,DEFAULT_MARGIN);
        rightMargin=typedArray.getLayoutDimension(R.styleable.HeaderBar_right_margin,DEFAULT_MARGIN);

        typedArray.recycle();

        initView(context);

    }


    private void initView(Context context) {

        View view=View.inflate(context,R.layout.header_bar,this);

        left= (LinearLayout) view.findViewById(R.id.ll_left_header_bar);
        center= (LinearLayout) view.findViewById(R.id.ll_center_header_bar);
        right= (LinearLayout) view.findViewById(R.id.ll_right_header_bar);

        leftTextView= (TextView) view.findViewById(R.id.tv_left_header_bar);
        centerTextView= (TextView) view.findViewById(R.id.tv_center_header_bar);
        rightTextView= (TextView) view.findViewById(R.id.tv_right_header_bar);

        leftImageView= (ImageView) view.findViewById(R.id.iv_left_header_bar);
        rightImageView= (ImageView) view.findViewById(R.id.iv_right_header_bar);

        if (!showLeftText) {
            leftTextView.setVisibility(GONE);
        }
        if (!showLeftIcon) {
            leftImageView.setVisibility(GONE);
        }
        if (!showCenterText) {
            centerTextView.setVisibility(GONE);
        }
        if (!showRightText) {
            rightTextView.setVisibility(GONE);
        }
        if (!showRightIcon) {
            rightImageView.setVisibility(GONE);
        }

        leftTextView.setText(leftText);
        leftTextView.setTextColor(leftTextColor);
        leftTextView.setTextSize(leftTextSize);

        centerTextView.setText(centerText);
        centerTextView.setTextColor(centerTextColor);
        centerTextView.setTextSize(centerTextSize);

        rightTextView.setText(rightText);
        rightTextView.setTextColor(rightTextColor);
        rightTextView.setTextSize(rightTextSize);

        leftImageView.setBackground(leftIcon);
        rightImageView.setBackground(rightIcon);

        this.setPadding(leftMargin,0,rightMargin,0);
    }


    public LinearLayout getLeftPart() {

        return left;
    }

    public LinearLayout getCenterPart() {

        return center;

    }


    public LinearLayout getRightPart() {

        return right;

    }

    public ImageView getLeftImageView() {

        return leftImageView;

    }

    public ImageView getRightImageView() {

        return rightImageView;

    }

    public TextView getLeftTextView() {

        return leftTextView;

    }

    public TextView getCenterTextView() {

        return centerTextView;

    }

    public TextView getRightTextView() {

        return rightTextView;

    }
}
