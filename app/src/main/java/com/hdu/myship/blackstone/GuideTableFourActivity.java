package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class GuideTableFourActivity extends AppCompatActivity implements View.OnClickListener{
    private String[] title={"两栖纲动物的分类特征","爬行纲有鳞目动物的主要分类特征","野外鸟类的观察与识别"};
    private StickyListHeadersListView lv;
    private int i_id[]=new int[]{R.drawable.four1,R.drawable.four2,R.drawable.four3,R.drawable.four4,R.drawable.four5,R.drawable.four6,R.drawable.four7,R.drawable.four8,
            R.drawable.four9,R.drawable.four10,R.drawable.four11,R.drawable.four12};
    private int im_id[]=new int[]{R.drawable.four21,R.drawable.four22,R.drawable.four23,R.drawable.four24,R.drawable.four25,R.drawable.four26,R.drawable.four28,R.drawable.four29,R.drawable.four210,R.drawable.four212,R.drawable.four213};
    private HeadAdapter adapter;
    private List<ItemBeam> list;
    private ImageView i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12;
    private ImageView im1,im2,im3,im4,im5,im6,im7,im8,im9,im10,im11;
    private LinearLayout actiobBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_guide_table_four);
        lv = (StickyListHeadersListView) findViewById(R.id.activity_guide_four_sticky_list_view);
        list = new ArrayList<ItemBeam>();
        init();
        adapter = new HeadAdapter(this, list);
        lv.setAdapter(adapter);
        actiobBack= (LinearLayout) findViewById(R.id.activity_guide_table_four_linear_layout_action_back);
        actiobBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });
    }

    public void init(){
        ItemBeam itemBeam = new ItemBeam(title[0],0);
        list.add(itemBeam);
        ItemBeam itemBeam2 = new ItemBeam(title[1],1);
        list.add(itemBeam2);
        ItemBeam itemBeam3 = new ItemBeam(title[2],2);
        list.add(itemBeam3);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(GuideTableFourActivity.this,ShowImageActivity.class);
        switch (v.getId()){
            case R.id.i1:
                intent.putExtra("title",i_id[0]);break;
            case R.id.i2:
                intent.putExtra("title",i_id[1]);break;
            case R.id.i3:
                intent.putExtra("title",i_id[2]);break;
            case R.id.i4:
                intent.putExtra("title",i_id[3]);break;
            case R.id.i5:
                intent.putExtra("title",i_id[4]);break;
            case R.id.i6:
                intent.putExtra("title",i_id[5]);break;
            case R.id.i7:
                intent.putExtra("title",i_id[6]);break;
            case R.id.i8:
                intent.putExtra("title",i_id[7]);break;
            case R.id.i9:
                intent.putExtra("title",i_id[8]);break;
            case R.id.i10:
                intent.putExtra("title",i_id[9]);break;
            case R.id.i11:
                intent.putExtra("title",i_id[10]);break;
            case R.id.i12:
                intent.putExtra("title",i_id[11]);break;
            case R.id.im1:
                intent.putExtra("title",im_id[0]);break;
            case R.id.im2:
                intent.putExtra("title",im_id[1]);break;
            case R.id.im3:
                intent.putExtra("title",im_id[2]);break;
            case R.id.im4:
                intent.putExtra("title",im_id[3]);break;
            case R.id.im5:
                intent.putExtra("title",im_id[4]);break;
            case R.id.im6:
                intent.putExtra("title",im_id[5]);break;
            case R.id.im7:
                intent.putExtra("title",im_id[6]);break;
            case R.id.im8:
                intent.putExtra("title",im_id[7]);break;
            case R.id.im9:
                intent.putExtra("title",im_id[8]);break;
            case R.id.im10:
                intent.putExtra("title",im_id[9]);break;
            case R.id.im11:
                intent.putExtra("title",im_id[10]);
                break;
        }
        startActivity(intent);
    }

    public class HeadAdapter extends BaseAdapter implements StickyListHeadersAdapter
    {
        private Context ctx;
        private List<ItemBeam> list;

        public HeadAdapter(Context context , List<ItemBeam> list){
            this.ctx = context;
            this.list = list;
        }
        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent)
        {
            convertView= getLayoutInflater().inflate(R.layout.catalog,parent,false);
            TextView tv= (TextView) convertView.findViewById(R.id.s);
            tv.setText(list.get(position).getCatalog());
            return convertView;
        }
        @Override
        public long getHeaderId(int position)
        {
            return list.get(position).getId();
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public Object getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }
        @Override
        public View getView( int position, View convertView, ViewGroup parent)
        {
            if(position==0) {
                convertView = getLayoutInflater().inflate(R.layout.content4, parent, false);
                i1 = (ImageView) convertView.findViewById(R.id.i1);
                i1.setOnClickListener(GuideTableFourActivity.this);
                i2 = (ImageView) convertView.findViewById(R.id.i2);
                i2.setOnClickListener(GuideTableFourActivity.this);
                i3 = (ImageView) convertView.findViewById(R.id.i3);
                i3.setOnClickListener(GuideTableFourActivity.this);
                i4 = (ImageView) convertView.findViewById(R.id.i4);
                i4.setOnClickListener(GuideTableFourActivity.this);
                i5 = (ImageView) convertView.findViewById(R.id.i5);
                i5.setOnClickListener(GuideTableFourActivity.this);
                i6 = (ImageView) convertView.findViewById(R.id.i6);
                i6.setOnClickListener(GuideTableFourActivity.this);
                i7 = (ImageView) convertView.findViewById(R.id.i7);
                i7.setOnClickListener(GuideTableFourActivity.this);
                i8 = (ImageView) convertView.findViewById(R.id.i8);
                i8.setOnClickListener(GuideTableFourActivity.this);
                i9 = (ImageView) convertView.findViewById(R.id.i9);
                i9.setOnClickListener(GuideTableFourActivity.this);
                i10 = (ImageView) convertView.findViewById(R.id.i10);
                i10.setOnClickListener(GuideTableFourActivity.this);
                i11 = (ImageView) convertView.findViewById(R.id.i11);
                i11.setOnClickListener(GuideTableFourActivity.this);
                i12 = (ImageView) convertView.findViewById(R.id.i12);
                i12.setOnClickListener(GuideTableFourActivity.this);
            }
            if(position==1){
                convertView = getLayoutInflater().inflate(R.layout.content42, parent, false);
                im1 = (ImageView) convertView.findViewById(R.id.im1);
                im1.setOnClickListener(GuideTableFourActivity.this);
                im2 = (ImageView) convertView.findViewById(R.id.im2);
                im2.setOnClickListener(GuideTableFourActivity.this);
                im3 = (ImageView) convertView.findViewById(R.id.im3);
                im3.setOnClickListener(GuideTableFourActivity.this);
                im4 = (ImageView) convertView.findViewById(R.id.im4);
                im4.setOnClickListener(GuideTableFourActivity.this);
                im5 = (ImageView) convertView.findViewById(R.id.im5);
                im5.setOnClickListener(GuideTableFourActivity.this);
                im6 = (ImageView) convertView.findViewById(R.id.im6);
                im6.setOnClickListener(GuideTableFourActivity.this);
                im7 = (ImageView) convertView.findViewById(R.id.im7);
                im7.setOnClickListener(GuideTableFourActivity.this);
                im8 = (ImageView) convertView.findViewById(R.id.im8);
                im8.setOnClickListener(GuideTableFourActivity.this);
                im9 = (ImageView) convertView.findViewById(R.id.im9);
                im9.setOnClickListener(GuideTableFourActivity.this);
                im10 = (ImageView) convertView.findViewById(R.id.im10);
                im10.setOnClickListener(GuideTableFourActivity.this);
                im11 = (ImageView) convertView.findViewById(R.id.im11);
                im11.setOnClickListener(GuideTableFourActivity.this);
            }
            if(position==2){
                convertView = getLayoutInflater().inflate(R.layout.content43, parent, false);
            }
            return convertView;
        }
    }
    public class ItemBeam
    {
        private String catalog;
        private int id;
        public ItemBeam(String catalog,int id){
            this.catalog=catalog;
            this.id=id;
        }
        public String getCatalog(){
            return catalog;
        }
        public int getId()
        {
            return id;
        }
    }
    public class content{
        private String catalog;
        private int id;
        private String t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20;
        private int i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12;
        public content(String t1,int i1,String t2,int i2,String t3,int i3,String t4,String t5,int i4,
                       String t6,int i5,String t7,String t8,int i6,String t9,String t10,int i7,String t11,
                       int i8,String t12,String t13,int i9,String t14,String t15,int i10,String t16,String
                               t17,int i11,String t18,int i12,String t19){
            this.i1=i1;this.t1=t1;this.t13=t13;
            this.i2=i2;this.t2=t2;this.t14=t14;
            this.i3=i3;this.t3=t3;this.t15=t15;
            this.i4=i4;this.t4=t4;this.t16=t16;
            this.i5=i5;this.t5=t5;this.t17=t17;
            this.i6=i6;this.t6=t6;this.t18=t18;
            this.i7=i7;this.t7=t7;this.t19=t19;
            this.i8=i8;this.t8=t8;
            this.i9=i9;this.t9=t9;
            this.i10=i10;this.t10=t10;
            this.i11=i11;this.t11=t11;
            this.i12=i12;this.t12=t12;
        }

    }
}
