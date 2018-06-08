package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kefan.blackstone.R;

import java.util.List;

/**
 * Created by MY SHIP on 2017/4/1.
 */

public class SliderBar extends LinearLayout {
    private Context mContext;
    private CharacterClickListener mListener;//创建一个接口
    private List<String> indexList;
    private List<Integer> positionList;

    public SliderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOrientation(VERTICAL);

    }

    private void initView(List<String> indexList, List<Integer> positionList) {
        int i = 0;
        for (String string : indexList) {
            TextView textView = buildTextLayout(string);//创建一个textview
            textView.setTag(positionList.get(i));
            addView(textView);//将textview添加至sliderbar中
            i++;
        }
    }

    /**
     * 该方法返回一个view
     * 参数 character 是用于显示到view上的字符
     *
     * @param character
     * @return
     */
    private TextView buildTextLayout(final String character) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);//主要是设置view在父布局的宽高
        TextView tv = new TextView(mContext);
        tv.setLayoutParams(layoutParams);
        tv.setGravity(Gravity.CENTER);
        tv.setClickable(true);
        tv.setTextColor(getResources().getColor(R.color.slider_bar_color));
        tv.setText(character);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//设置点击事件
                if (mListener != null) {
                    mListener.clickCharacter(v);
                }
            }
        });
        return tv;
    }


    public void setCharacterListener(CharacterClickListener listener) {
        mListener = listener;
    }

    public interface CharacterClickListener {
        void clickCharacter(View view);
    }


    public void setData(List<String> indexList, List<Integer> positionList) {
        this.indexList = indexList;
        this.positionList = positionList;
        this.removeAllViews();
        initView(indexList, positionList);

    }

}
