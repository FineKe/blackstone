package ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/6 上午12:20
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        ButterKnife.bind(this);

        initView();

        initEvent();
    }


    private void initEvent() {
        
    }

    public void initView() {

    }

    protected abstract int setLayout();
}
