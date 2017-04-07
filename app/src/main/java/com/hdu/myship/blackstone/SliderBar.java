package com.hdu.myship.blackstone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by MY SHIP on 2017/4/1.
 */

public class SliderBar extends LinearLayout{
    private String[] data=getResources().getStringArray(R.array.sorting_by_order);
        private Context mContext;
        private CharacterClickListener mListener;

        public SliderBar(Context context, AttributeSet attrs) {
            super(context, attrs);
            mContext = context;
            setOrientation(VERTICAL);
            initView(data);
        }

        private void initView(String[] data) {
//            addView(buildImageLayout());

         /*  for (char i = 'A'; i <= 'Z'; i++) {
                final String character = i + "";
                TextView tv = buildTextLayout(character);

                addView(tv);
            }*/

//            addView(buildTextLayout("#"));

              for(String string:data)
            {
                TextView textView=buildTextLayout(string);
                addView(textView);
            }
        }

        private TextView buildTextLayout(final String character) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
            TextView tv = new TextView(mContext);
            tv.setLayoutParams(layoutParams);
            tv.setGravity(Gravity.CENTER);
            tv.setClickable(true);
            tv.setTextColor(getResources().getColor(R.color.slider_bar_color));
            tv.setText(character);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.clickCharacter(character);
                    }
                }
            });
            return tv;
        }

//        private ImageView buildImageLayout() {
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
//
//            ImageView iv = new ImageView(mContext);
//            iv.setLayoutParams(layoutParams);
//
//            iv.setBackgroundResource(R.mipmap.norrow);
//
//            iv.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        mListener.clickArrow();
//                    }
//                }
//            });
//            return iv;
//        }

        public void setCharacterListener(CharacterClickListener listener) {
            mListener = listener;
        }

        public interface CharacterClickListener {
            void clickCharacter(String character);
            void clickArrow();
        }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}
