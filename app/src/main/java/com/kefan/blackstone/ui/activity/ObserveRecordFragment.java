package com.kefan.blackstone.ui.activity;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.ui.fragment.BaseFragment;
import com.kefan.blackstone.widget.HeaderBar;
import com.kefan.blackstone.widget.ItemRemoveRecordRecycleView;

import java.util.List;

import butterknife.BindView;

public class ObserveRecordFragment extends BaseFragment {


    private HeaderBar headerBar;


    @BindView(R.id.item_remove_recycler_view_observe_record_fragment)
    ItemRemoveRecordRecycleView itemRemoveRecordRecycleView;


    @Override
    public int setLayout() {
        return R.layout.fragment_observe_record;
    }

    @Override
    public void initView() {

        headerBar= ((MainActivity) getActivity()).headerBar;
        headerBar.getCenterTextView().setText("我的观察记录");
        headerBar.getRightPart().setVisibility(View.GONE);

    }

    @Override
    public void onDestroyView() {

        headerBar.getCenterTextView().setText("");
        headerBar.getRightPart().setVisibility(View.VISIBLE);

        super.onDestroyView();
    }



    public class Record {
        private int id;//记录id
        private int userId;//用户id
        private Long time;//时间：毫秒数
        private List<NoteCounts> noteCountses;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }
//
        public List<NoteCounts> getNoteCountses() {
            return noteCountses;
        }

        public void setNoteCountses(List<NoteCounts> noteCountses) {
            this.noteCountses = noteCountses;
        }
    }
    public class NoteCounts
    {
        String speciesType;
        int count;

        public String getSpeciesType() {
            return speciesType;
        }

        public void setSpeciesType(String speciesType) {
            this.speciesType = speciesType;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
