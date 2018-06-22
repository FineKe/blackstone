package com.kefan.blackstone.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.kefan.blackstone.R;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.service.RecordService;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.RecordServiceImpl;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.fragment.BaseFragment;
import com.kefan.blackstone.util.ToastUtil;
import com.kefan.blackstone.widget.HeaderBar;
import com.kefan.blackstone.widget.ItemRemoveRecordRecycleView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;

public class ObserveRecordFragment extends BaseFragment {


    public static final int LOAD_RECORDS_COMPLETE=1;

    private HeaderBar headerBar;


    @BindView(R.id.item_remove_recycler_view_observe_record_fragment)
    ItemRemoveRecordRecycleView itemRemoveRecordRecycleView;

    private UserService userService;

    private RecordService recordService;

    private ItemRemoveRcordAdapter itemRemoveRcordAdapter;

    private List<Record> records;

    private ExecutorService executor= Executors.newSingleThreadExecutor();

    private LoadRecordRunner loadRecordRunner = new LoadRecordRunner();

    @Override
    public int setLayout() {
        return R.layout.fragment_observe_record;
    }

    @Override
    public void initView() {

        headerBar= ((MainActivity) getActivity()).headerBar;
        headerBar.getCenterTextView().setText("我的观察记录");
        headerBar.getRightPart().setVisibility(View.GONE);


        itemRemoveRecordRecycleView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        itemRemoveRecordRecycleView.setAdapter(itemRemoveRcordAdapter);

    }

    @Override
    protected void initData() {

        userService=new UserServiceImpl();
        recordService=new RecordServiceImpl();

        records=new ArrayList<>();
        itemRemoveRcordAdapter=new ItemRemoveRcordAdapter(getContext(),records);

        //加载数据
        loadRecords();
    }

    @Override
    public void initEvent() {

        itemRemoveRecordRecycleView.setOnItemClickListener(new OnItemRemoveRecordListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent=new Intent(getContext(),MyRecordTwoActivity.class);
                intent.putExtra("recordId",records.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {

                Record record = records.remove(position);

                if (record.delete() > 0) {
                    ToastUtil.showToast(getContext(),"删除成功");
                }

                itemRemoveRcordAdapter.notifyDataSetChanged();

            }

            @Override
            public void onUploadClick(int position) {

                records.get(position).setAddToObservedList(true);
                itemRemoveRcordAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onDestroyView() {

        headerBar.getCenterTextView().setText("");
        headerBar.getRightPart().setVisibility(View.VISIBLE);

        super.onDestroyView();
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_RECORDS_COMPLETE:

                    itemRemoveRcordAdapter.notifyDataSetChanged();

                    break;
            }
        }
    };

    private void loadRecords() {

        executor.submit(loadRecordRunner);

    }

    private class LoadRecordRunner implements Runnable {

        @Override
        public void run() {
            records.clear();
            records.addAll(recordService.list(userService.getUser().getId()));
            handler.sendEmptyMessage(LOAD_RECORDS_COMPLETE);
        }
    }



}
