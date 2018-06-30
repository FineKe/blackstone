package com.kefan.blackstone.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kefan.blackstone.R;
import com.kefan.blackstone.listener.AlertPromptOnClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @note: 提示弹窗
 * @author: fine
 * @time: 2018/6/18 下午10:17
 */
public class AlertPrompt extends Dialog{


    //布局id
    private int layout;

    private Context context;

    @BindView(R.id.tv_title_alert_prompt_dialog)
    TextView title;

    @BindView(R.id.tv_content_alert_prompt_dialog)
    TextView content;

    @BindView(R.id.tv_left_alert_prompt_dialog)
    TextView left;

    @BindView(R.id.tv_right_alert_prompt_dialog)
    TextView right;

    private AlertPromptOnClickListener listener;


    private AlertPrompt(Context context,int layout) {
        this(context,0,0);
    }

    private AlertPrompt(Context context,int themeResId,int layout) {
        super(context, themeResId);
        this.context=context;
        this.layout=layout;
    }

    public static AlertPrompt create(Context context) {

        AlertPrompt alertPrompt=new AlertPrompt(context,R.style.LoginDialog,R.layout.dialog_alert_prompt);
        alertPrompt.setCancelable(false);

        return alertPrompt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }



    private void initView() {

        title= (TextView) findViewById(R.id.tv_title_alert_prompt_dialog);

    }

    private void initEvent() {

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.leftOnClick(view);
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.rightOnClick(view);
                }
            }
        });
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setContent(String content) {
        this.content.setText(content);
    }

    /**
     * 设置 按钮名 及监听事件
     * @param left
     * @param right
     * @param listener
     */
    public void setOnClickListener(String left,String right,AlertPromptOnClickListener listener) {
        this.left.setText(left);
        this.right.setText(right);
        this.listener=listener;
    }

}
