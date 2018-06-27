package com.kefan.blackstone.ui.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.kefan.blackstone.R;
import com.kefan.blackstone.common.SpeciesConstant;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.database.Amphibia;
import com.kefan.blackstone.database.Bird;
import com.kefan.blackstone.database.Insect;
import com.kefan.blackstone.database.Reptiles;
import com.kefan.blackstone.database.TestRecord;
import com.kefan.blackstone.model.Optional;
import com.kefan.blackstone.model.Question;
import com.kefan.blackstone.service.TestService;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.TestServiceImpl;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.activity.MainActivity;
import com.kefan.blackstone.util.ToastUtil;
import com.kefan.blackstone.vo.TestVO;
import com.kefan.blackstone.widget.HeaderBar;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/8 上午10:35
 */
public class ExecuteTestFragment extends BaseFragment {


    public static final int LOAD_QUESTION_COMPLETE = 1;

    public static final String TAG = ExecuteTestFragment.class.getName();

    public static final String ANSWER_CORRECT = "正确";

    public static final String ANSWER_INCORRECT = "错误";

    @BindView(R.id.iv_icon_execute_test_fragment)
    ImageView icon;

    @BindView(R.id.rg_anser_group_execute_test_fragment)
    RadioGroup answerGroup;

    @BindView(R.id.rb_anser_a_execute_test_fragment)
    RadioButton answerA;
    @BindView(R.id.rb_anser_b_execute_test_fragment)
    RadioButton answerB;
    @BindView(R.id.rb_anser_c_execute_test_fragment)
    RadioButton answerC;
    @BindView(R.id.rb_anser_d_execute_test_fragment)
    RadioButton answerD;

    @BindView(R.id.tv_answer_result_execute_test_fragment)
    TextView result;

    @BindView(R.id.ll_answer_result_and_next_execute_test_fragment)
    RelativeLayout next;


    private HeaderBar headerBar;

    private TextView title;

    private TextView rightText;

    private UserService userService;
    private TestService testService;

    //问题
    private Question question;


    private List<Bird> birdList;
    private List<Amphibia> amphibiaList;
    private List<Reptiles> reptilesList;
    private List<Insect> insectList;

    //命数
    private int life = 3;

    //游戏是否结束
    private boolean gameOver = false;

    //分数
    private int score = 0;

    //答题正确
    private int correctColor = Color.argb(255, 0, 255, 0);

    //答题错误
    private int incorrectClolor = Color.argb(255, 255, 0, 0);

    //正常颜色
    private int normalColor = Color.argb(255, 229, 228, 228);

    @Override
    protected void initData() {

        userService = new UserServiceImpl();
        testService = new TestServiceImpl();

        //加载 本地物种数据
        birdList = DataSupport.findAll(Bird.class);
        amphibiaList = DataSupport.findAll(Amphibia.class);
        reptilesList = DataSupport.findAll(Reptiles.class);
        insectList = DataSupport.findAll(Insect.class);

        nextQuestion();
    }

    @Override
    public void initView() {

        Logger.i(">>");

        //标题栏设置
        headerBar = headerBar == null ? ((MainActivity) getActivity()).headerBar : headerBar;
        title = title == null ? headerBar.getCenterTextView() : title;

        rightText = rightText == null ? headerBar.getRightTextView() : rightText;

        headerBar.getCenterPart().removeView(title);

        headerBar.getRightTextView().setVisibility(View.VISIBLE);

        rightText.setText("0");
        rightText.setTextSize(18);

        headerBar.getRightImageView().setVisibility(View.GONE);

        Glide.with(this).load(R.drawable.testing_bg).into(icon);

    }

    @Override
    public int setLayout() {
        return R.layout.fragment_execute_test;
    }

    @Override
    public void initEvent() {

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //判断游戏是否结束
                if (gameOver) {
                    ToastUtil.showToast(getContext(), "游戏结束");
                    lockRadioButtion();
                    return;
                } else {

                    //清空结果显示
                    result.setText("");

                    //设置正常背景
                    next.setBackgroundColor(normalColor);

                    answerGroup.clearCheck();
                    unlockRadioButton();

                    nextQuestion();
                }

            }
        });

        answerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        answerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        answerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        answerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Logger.i(">>");

        //恢复标题栏
        headerBar.getCenterPart().addView(title);

        headerBar.getRightTextView().setText("");
        headerBar.getRightImageView().setVisibility(View.VISIBLE);

        //保存 测试记录
        TestRecord testRecord=new TestRecord();
        testRecord.setTime(System.currentTimeMillis());
        testRecord.setUserId(userService.getUser().getId());
        testRecord.setScore(score);
        testRecord.save();
    }

    /**
     * 检查答案是否正确
     */
    private void checkAnswer() {
        switch (answerGroup.getCheckedRadioButtonId()) {

            case R.id.rb_anser_a_execute_test_fragment:

                lockRadioButtion();
                if (question.getA().isTrue()) {

                    score++;
                    rightText.setText(score + "");
                    gameSubmit(true);
                    next.setBackgroundColor(correctColor);
                    result.setText(ANSWER_CORRECT);
                } else {
                    next.setBackgroundColor(incorrectClolor);
                    result.setText(ANSWER_INCORRECT);
                    gameSubmit(false);

                }

                break;
            case R.id.rb_anser_b_execute_test_fragment:

                lockRadioButtion();
                if (question.getB().isTrue()) {

                    score++;
                    rightText.setText(score + "");
                    gameSubmit(true);
                    next.setBackgroundColor(correctColor);
                    result.setText(ANSWER_CORRECT);
                } else {
                    next.setBackgroundColor(incorrectClolor);
                    result.setText(ANSWER_INCORRECT);
                    gameSubmit(false);


                }
                break;
            case R.id.rb_anser_c_execute_test_fragment:

                lockRadioButtion();
                if (question.getC().isTrue()) {

                    score++;
                    rightText.setText(score + "");
                    gameSubmit(true);
                    next.setBackgroundColor(correctColor);
                    result.setText(ANSWER_CORRECT);
                } else {
                    next.setBackgroundColor(incorrectClolor);
                    result.setText(ANSWER_INCORRECT);
                    gameSubmit(false);

                }

                break;
            case R.id.rb_anser_d_execute_test_fragment:

                lockRadioButtion();
                if (question.getD().isTrue()) {

                    score++;
                    rightText.setText(score + "");
                    next.setBackgroundColor(correctColor);
                    result.setText(ANSWER_CORRECT);
                    gameSubmit(true);
                } else {
                    next.setBackgroundColor(incorrectClolor);
                    result.setText(ANSWER_INCORRECT);
                    gameSubmit(false);


                }

                break;
        }
    }

    /**
     * 上报成绩
     */
    private void gameSubmit(boolean isTrue) {
        testService.gameSumbit(userService.getToken(),isTrue, new BaseResponseListener(Object.class) {
            @Override
            protected void onSuccess(Object data) {

                answerGroup.clearCheck();
                unlockRadioButton();

            }

            @Override
            protected void onFailed(int code, String message) {
                ToastUtil.showToast(getContext(), message);
            }
        }, new BaseErrorListener(getContext()) {
            @Override
            protected void onError(VolleyError volleyError) {

            }
        });


    }

    /**
     * 获取下一道题
     */
    private void nextQuestion() {

        testService.nextQuestion(userService.getToken(), new BaseResponseListener<TestVO>(TestVO.class) {
            @Override
            protected void onFailed(int code, String message) {
                ToastUtil.showToast(getContext(), "加载题目失败：" + message);

            }

            @Override
            protected void onSuccess(TestVO data) {


                switch (data.getSpeciesType()) {

                    case SpeciesConstant.BIRD:
                        question = CreateQuestionBird(data);
                        break;

                    case SpeciesConstant.AMPHIBIA:
                        question = CreateQuestionAmphibia(data);
                        break;

                    case SpeciesConstant.INSECT:
                        question = CreateQuestionInsect(data);
                        break;

                    case SpeciesConstant.REPTILES:
                        question = CreateQuestionReptiles(data);
                        break;
                }

                question.setImg(data.getImg() + "?imageslim");

                handler.sendEmptyMessage(LOAD_QUESTION_COMPLETE);

            }
        }, new BaseErrorListener(getContext()) {
            @Override
            protected void onError(VolleyError volleyError) {

            }
        });


    }


    /**
     * 设置不能在点击
     */
    private void lockRadioButtion() {

        answerA.setClickable(false);
        answerB.setClickable(false);
        answerC.setClickable(false);
        answerD.setClickable(false);

    }

    /**
     * 设置可以点击
     */
    private void unlockRadioButton() {

        answerA.setClickable(true);
        answerB.setClickable(true);
        answerC.setClickable(true);
        answerD.setClickable(true);

    }

    /**
     * 创建问题
     *
     * @param data
     * @return
     */
    private Question CreateQuestionBird(TestVO data) {

        int range = birdList.size() - 1;
        //正确答案的 实体
        Bird expected = null;

        Random random = new Random();

        Question question = new Question();

        List<Optional> optionals = new ArrayList<>(4);

        for (Bird bird : birdList) {

            if (bird.getSingal() == data.getSpeciesId()) {

                expected = bird;

                Optional optional = new Optional(bird.getChineseName(), bird.getImgs().get(0), true);

                optionals.add(optional);

                break;
            }

        }

        //先移除，防止生成相同选项
        birdList.remove(expected);

        for (int i = 0; i < 3; i++) {

            Bird bird = birdList.get(random.nextInt(range));

            Optional optional = new Optional(bird.getChineseName(), bird.getImgs().get(0), false);

            optionals.add(optional);

        }

        //再次放进去
        birdList.add(expected);

        disrupt(optionals, question);

        return question;
    }

    private Question CreateQuestionAmphibia(TestVO data) {

        int range = amphibiaList.size() - 1;
        //正确答案的 实体
        Amphibia expected = null;

        Random random = new Random();

        Question question = new Question();

        List<Optional> optionals = new ArrayList<>(4);

        for (Amphibia amphibia : amphibiaList) {

            if (amphibia.getSingal() == data.getSpeciesId()) {

                expected = amphibia;

                Optional optional = new Optional(amphibia.getChineseName(), amphibia.getImgs().get(0), true);

                optionals.add(optional);

                break;
            }

        }

        //先移除，防止生成相同选项
        amphibiaList.remove(expected);

        for (int i = 0; i < 3; i++) {

            Amphibia amphibia = amphibiaList.get(random.nextInt(range));

            Optional optional = new Optional(amphibia.getChineseName(), amphibia.getImgs().get(0), false);

            optionals.add(optional);

        }

        //再次放进去
        amphibiaList.add(expected);

        disrupt(optionals, question);

        return question;
    }


    private Question CreateQuestionInsect(TestVO data) {

        int range = insectList.size() - 1;
        //正确答案的 实体
        Insect expected = null;

        Random random = new Random();

        Question question = new Question();

        List<Optional> optionals = new ArrayList<>(4);

        for (Insect insect : insectList) {

            if (insect.getSingal() == data.getSpeciesId()) {

                expected = insect;

                Optional optional = new Optional(insect.getChineseName(), insect.getImgs().get(0), true);

                optionals.add(optional);

                break;
            }

        }

        //先移除，防止生成相同选项
        insectList.remove(expected);

        for (int i = 0; i < 3; i++) {

            Insect insect = insectList.get(random.nextInt(range));

            Optional optional = new Optional(insect.getChineseName(), insect.getImgs().get(0), false);

            optionals.add(optional);

        }

        //再次放进去
        insectList.add(expected);

        disrupt(optionals, question);

        return question;
    }


    private Question CreateQuestionReptiles(TestVO data) {

        int range = reptilesList.size() - 1;
        //正确答案的 实体
        Reptiles expected = null;

        Random random = new Random();

        Question question = new Question();

        List<Optional> optionals = new ArrayList<>(4);

        for (Reptiles reptiles : reptilesList) {

            if (reptiles.getSingal() == data.getSpeciesId()) {

                expected = reptiles;

                Optional optional = new Optional(reptiles.getChineseName(), reptiles.getImgs().get(0), true);

                optionals.add(optional);

                break;
            }

        }

        //先移除，防止生成相同选项
        reptilesList.remove(expected);

        for (int i = 0; i < 3; i++) {

            Reptiles reptiles = reptilesList.get(random.nextInt(range));

            Optional optional = new Optional(reptiles.getChineseName(), reptiles.getImgs().get(0), false);

            optionals.add(optional);

        }

        //再次放进去
        reptilesList.add(expected);

        disrupt(optionals, question);

        return question;
    }


    /**
     * 打乱顺序
     *
     * @param list
     */
    private void disrupt(List<Optional> list, Question question) {

        Random random = new Random(System.currentTimeMillis());


        int index = random.nextInt(4);

        Log.d(TAG, "disrupt: " + index);

        if (index == 0) {
            question.setA(list.get(0));
            question.setB(list.get(1));
            question.setC(list.get(2));
            question.setD(list.get(3));

        } else {

            Optional trueOptional = list.remove(0);
            list.add(index - 1, trueOptional);
            question.setA(list.get(0));
            question.setB(list.get(1));
            question.setC(list.get(2));
            question.setD(list.get(3));
        }


    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case LOAD_QUESTION_COMPLETE:

                    Glide.with(getContext()).load(question.getImg()).placeholder(R.mipmap.loading_big).into(icon);

                    answerA.setText("A  " + question.getA().getName());
                    answerB.setText("B  " + question.getB().getName());
                    answerC.setText("C  " + question.getC().getName());
                    answerD.setText("D  " + question.getD().getName());

                    break;
            }
        }
    };


}
