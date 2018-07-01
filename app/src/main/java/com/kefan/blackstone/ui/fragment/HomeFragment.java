package com.kefan.blackstone.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.kefan.blackstone.R;
import com.kefan.blackstone.ShapeUtil.GlideRoundTransform;
import com.kefan.blackstone.common.IntentFieldConstant;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.database.TestRecord;
import com.kefan.blackstone.service.HomeService;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.HomeServiceImpl;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.activity.CollectionHomeFragment;
import com.kefan.blackstone.ui.activity.MainActivity;
import com.kefan.blackstone.ui.activity.MyCollectionsTwoActivity;
import com.kefan.blackstone.ui.activity.SearchActivity;
import com.kefan.blackstone.ui.activity.SpeciesDeatailedActivity;
import com.kefan.blackstone.ui.adapter.HomeRecycleViewAdapter;
import com.kefan.blackstone.util.DensityUtil;
import com.kefan.blackstone.util.ToastUtil;
import com.kefan.blackstone.vo.MainVo;
import com.kefan.blackstone.widget.HeaderBar;
import com.kefan.blackstone.widget.PrecentCricleView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.kefan.blackstone.BlackStoneApplication.getContext;


/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/7 上午12:01
 */
public class HomeFragment extends BaseFragment {

    public static final int DATA_LOAD_COMPLETE = 1;

    private HomeService homeService;

    private UserService userService;

    @BindView(R.id.banner_home_fragment)
    Banner banner;

    @BindView(R.id.precent_view_correct_rate_home_fragment)
    PrecentCricleView correctRate;

    @BindView(R.id.precent_view_this_score_home_fragment)
    PrecentCricleView thisScore;

    @BindView(R.id.precent_view_last_score_home_fragment)
    PrecentCricleView lastScore;

    @BindView(R.id.prl_home_fragment)
    PullRefreshLayout refreshLayout;

    @BindView(R.id.recycle_view_species_query_home_fragment)
    RecyclerView recyclerView;

    @BindView(R.id.ll_testing_home_fragment)
    LinearLayout testing;

    @BindView(R.id.ll_start_test_home_fragment)
    LinearLayout testStart;

    @BindView(R.id.ll_collection_home_fragment)
    LinearLayout collectionView;

    @BindView(R.id.ll_check_all_collection)
    LinearLayout checkAllCollection;

    @BindView(R.id.ll_collection_icon_home_fragment)
    LinearLayout collectionIcon;

    private HeaderBar headerBar;

    private MainVo mainVo;

    private HomeRecycleViewAdapter adapter;

    private List<MainVo.CategoriesBean> categoriesBeans;

    private CollectionHomeFragment collectionHomeFragment;


    @Override
    public int setLayout() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initData() {

        userService = new UserServiceImpl();
        homeService = new HomeServiceImpl();
        categoriesBeans = new ArrayList<>();
        adapter = new HomeRecycleViewAdapter(getActivity(), categoriesBeans);

    }

    @Override
    public void initView() {


        headerBar = ((MainActivity) getActivity()).headerBar;

        headerBar.getCenterTextView().setText("黑石顶生物多样性野外实习");

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<TestRecord> testRecords = DataSupport.where("userId=?",String.valueOf(userService.getUser().getId())).order("time").find(TestRecord.class);

        //设置得分
        if (testRecords.size() >0) {
            int length = testRecords.size();
            //设置本次得分
            thisScore.setText(testRecords.get(length-1).getScore()+"");

            //设置历史得分
            if (length > 1) {
                lastScore.setText(testRecords.get(length-2).getScore()+"");
            }
        }


    }

    @Override
    public void initEvent() {


        headerBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }

        });

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadData();

            }
        });

        testStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity())
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content_main_activity,new TestingFragment())
                        .commit();

            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onDestroyView() {
        headerBar.getCenterTextView().setText("");
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    private void loadData() {
        homeService.home(userService.getToken()
                , new BaseResponseListener<MainVo>(MainVo.class) {

                    @Override
                    protected void onSuccess(MainVo data) {

                        mainVo = data;

                        handler.sendEmptyMessage(DATA_LOAD_COMPLETE);

                    }

                    @Override
                    protected void onFailed(int code, String message) {
                        ToastUtil.showToast(getContext(), message);
                    }
                }
                , new BaseErrorListener(getContext()) {
                    @Override
                    protected void onError(VolleyError volleyError) {
                        super.onError(volleyError);
                    }
                });
    }

    /**
     * 加载 banner
     * @param urls
     */
    private void initBanner(ArrayList<String> urls) {

        banner.setImages(urls).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load((String) path+"?imageslim").into(imageView);
            }
        }).start();
    }

    /**
     * 加载 种类检索
     * @param list
     */
    private void initRecycleView(List<MainVo.CategoriesBean> list) {
        categoriesBeans.clear();
        categoriesBeans.addAll(list);
        adapter.notifyDataSetChanged();
    }

    /**
     * 加载 小测试视图
     * @param mainVo
     */
    private void initTestingView(MainVo mainVo) {

        if (mainVo.getTest() != null) {

            MainVo.Test test = mainVo.getTest();

            System.out.println(test.toString());

            BigDecimal bigDecimal=BigDecimal.valueOf(test.getRatio()*100);
            float ratio = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();

            ((MainActivity) getActivity()).ratio=ratio;

            correctRate.setSweepAngle(ratio*3.6f);
            correctRate.setText(""+ratio+"%");
            testing.setVisibility(View.VISIBLE);

        } else {

            testing.setVisibility(View.GONE);

        }

    }

    /**
     * 加载 收藏 视图
     * @param mainVo
     */
    private void initCollectionView(MainVo mainVo) {


        List<MainVo.CollectionsBean> collectionsBeans = mainVo.getCollections();

        //判断收藏是否为空 或者数据是否为空
        if (collectionsBeans != null && collectionsBeans.size() > 0) {

            //显示 收藏这块视图
            collectionView.setVisibility(View.VISIBLE);
            collectionIcon.removeAllViews();

            for (int i = 0; i < collectionsBeans.size(); i++) {

                RoundedImageView imageView = (RoundedImageView) LayoutInflater.from(collectionView.getContext()).inflate(R.layout.icon_collection_home_fragment, null, false);

                //设置自动调整大小
                imageView.setAdjustViewBounds(true);

                //加载图片
                Glide.with(getContext()).load(collectionsBeans.get(i).getMainPhoto()).transform(new GlideRoundTransform(getContext(), 8)).into(imageView);

                //添加至视图中
                collectionIcon.addView(imageView, i);

                imageView.setTag(collectionsBeans.get(i));

                //设置 间距
                if (i == 0) {
                    //第一个视图左边间距16dp
                    ((LinearLayout.LayoutParams) imageView.getLayoutParams())
                            .setMargins(DensityUtil.dip2px(getContext(), 16)
                                    , DensityUtil.dip2px(getContext(), 10)
                                    , DensityUtil.dip2px(getContext(), 10)
                                    , DensityUtil.dip2px(getContext(), 10));
                } else {

                    ((LinearLayout.LayoutParams) imageView.getLayoutParams())
                            .setMargins(DensityUtil.dip2px(getContext(), 10)
                                    , DensityUtil.dip2px(getContext(), 10)
                                    , DensityUtil.dip2px(getContext(), 10)
                                    , DensityUtil.dip2px(getContext(), 10));
                }

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getContext(),SpeciesDeatailedActivity.class);

                        MainVo.CollectionsBean collectionsBean= (MainVo.CollectionsBean) view.getTag();

                        intent.putExtra(IntentFieldConstant.SPECIES_TYPE,collectionsBean.getSpeciesType());
                        intent.putExtra(IntentFieldConstant.SPECIES_ID,collectionsBean.getId());

                        getActivity().startActivity(intent);
                    }
                });



            }

            checkAllCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    collectionHomeFragment=collectionHomeFragment==null?new CollectionHomeFragment():collectionHomeFragment;

                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_content_main_activity,collectionHomeFragment)
                            .commit();
                }
            });

        } else {

            //隐藏视图
            collectionView.setVisibility(View.GONE);

        }

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            switch (msg.what) {
                case DATA_LOAD_COMPLETE:

                    //绘制视图
                    initBanner(mainVo.getBannerImg());

                    initRecycleView(mainVo.getCategories());

                    initTestingView(mainVo);

                    initCollectionView(mainVo);


                    //刷新完成
                    refreshLayout.setRefreshing(false);

                    break;
            }
        }
    };
}
