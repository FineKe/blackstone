package ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdu.myship.blackstone.R;

import butterknife.BindView;
import widget.PrecentCricleView;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/8 上午12:42
 */
public class TestingFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.iv_icon_test_fragment)
    ImageView icon;

    @BindView(R.id.precent_view_correct_rate_testing_fragment)
    PrecentCricleView correctRate;

    @BindView(R.id.precent_view_this_score_testing_fragment)
    PrecentCricleView thisScore;

    @BindView(R.id.precent_view_last_score_testing_fragment)
    PrecentCricleView lastScore;

    @BindView(R.id.tv_start_testing_fragment)
    TextView start;

    @BindView(R.id.tv_rank_testing_fragment)
    TextView rank;

    @Override
    public int setLayout() {
        return R.layout.fragment_testing;
    }

    @Override
    public void initEvent() {



    }

    @Override
    public void onClick(View view) {

    }
}
