package com.hdu.myship.blackstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyRecordsActivity extends AppCompatActivity {

    private ItemRemoveRecordRecycle removeRecordRecycleView;
    private List<Record> recordList;
    private ItemRemoveRcordAdapter itemRemoveRcordAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_records);
        initData();
        initView();
    }

    private void initData() {
        recordList=new ArrayList<>();
        initRecords();
        itemRemoveRcordAdapter=new ItemRemoveRcordAdapter(recordList);

    }

    private void initView() {
        removeRecordRecycleView= (ItemRemoveRecordRecycle) findViewById(R.id.activity_my_records_item_remove_recycler_view);
        removeRecordRecycleView.setLayoutManager(new LinearLayoutManager(this));
        removeRecordRecycleView.setAdapter(itemRemoveRcordAdapter);
    }

    public class Record
    {
        private String date;
        private String amphibia;
        private String reptile;
        private String bird;
        private String insect;

        public Record(String date, String amphibia, String reptile, String bird, String insect) {
            this.date = date;
            this.amphibia = amphibia;
            this.reptile = reptile;
            this.bird = bird;
            this.insect = insect;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAmphibia() {
            return amphibia;
        }

        public void setAmphibia(String amphibia) {
            this.amphibia = amphibia;
        }

        public String getReptile() {
            return reptile;
        }

        public void setReptile(String reptile) {
            this.reptile = reptile;
        }

        public String getBird() {
            return bird;
        }

        public void setBird(String bird) {
            this.bird = bird;
        }

        public String getInsect() {
            return insect;
        }

        public void setInsect(String insect) {
            this.insect = insect;
        }
    }

    private void initRecords() {
        Record apple = new Record("2017年7月4日","两栖类2种","爬行类2种","鸟类7种","昆虫1个目");
        recordList.add(apple);
        Record two = new Record("2017年7月2日","两栖类1种","爬行类3种","鸟类24种","昆虫1个目");
        recordList.add(two);
        Record three = new Record("2017年7月1日","两栖类1种","爬行类2种","鸟类3种","昆虫4个目");
        recordList.add(three);
        Record four = new Record("2017年6月30日","两栖类3种","爬行类5种","鸟类7种","昆虫1个目");
        recordList.add(four);

    }
}
