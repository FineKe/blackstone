package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;

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

public class GuideTabOneActivity extends BaseActivity {
    private String[] title={"地理区位","自然条件"};
    private String[] text={"    中山大学黑石顶野外教学实习基地暨教育部“热带、亚热带森林生态实验中心”位于广东省封开县黑石顶省级自然保护区，地理坐标为23°25′~23°29′N，111°49′~111°54′W，北回归线横穿保护区中部。保护区地处云开山脉余脉，向南距离云开山脉最高峰茂名大雾岭约150 km，距海南岛约400 km；西南距离十万大山不到400 km；西北距离广西大瑶山约200 km；向北距离南岭最高峰广西猫儿山370 km。云开山脉北向联通南岭山地，西南则由云开山脉、六万山脉和十万山脉3条彼此相连的山脉组成1条山脉带，进入中南半岛。云开山脉对于中国华南地区生物多样性的形成和维持发挥了至关重要的关键作用。",
            "图1.1 黑石顶的地理区位",
            "    地质地貌：以泥盆纪花岗岩为主，南沙涌一带局部地区为页岩构成，保护区周边主要是西北及西向石灰岩地貌发育典型。保护区范围属低山山地地貌，地形起伏大，地势东南高而西北低，一般海拔高度为150 m至700 m，主峰黑石顶高达927 m。区内大小溪流众多，汇成黑石河和七星河。由于海拔落差较小，土壤层较厚，溪流大多流速较缓，并常形成小潭，湍急溪流较少；在雨季，缓溪会因水势突然变大而成临时性急流。\n" +
                    "土壤：主要是赤红壤和山地黄壤两大类，红壤和黄壤则以海拔750 m左右为界，因植被覆盖好，水土流失少，枯枝落叶多，腐殖质相当丰富；土壤层厚，发育良好。880 m以上是山地草甸土。\n" +
                    "气候：保护区年平均温19.6℃，最冷月均温10.6℃，无霜期297天，年降雨量1743.8 mm，降雨集中在4–9月，占全年的79%。相对湿度在80%以上。因此，该区属于南亚热带湿润季风气候。\n" +
                    "植被：黑石顶植被水平分布为南亚热带常绿阔叶林，在种类组成上，居优势的是樟科、壳斗科、金缕梅科和山茶科等亚热带阔叶林的表征科，同时，热带性质的科如紫金牛科、桑科和大戟科等的优势度也很高。群落内木质藤本植物较为常见。群落季相变化不明显，外貌常绿。多数植物种群趋于集群分布。保护区中、北部几乎全为次生的马尾松、岗松、桃金娘及单子叶禾草类灌丛；东南部山谷分布着较好的南亚热带常绿阔叶林。垂直分布因保护区内山体不高，垂直规律不明显，可划分成几个垂直带，海拔600 m以下，为南亚热带低地常绿阔叶林，以壳斗科的锥属和柯属，樟科的厚壳桂属、山茶科各属等为主；海拔600–650 m以上为低山常绿阔叶林，600–800 m带为山地常绿阔叶林，主要以金缕梅科、茜草科、冬青科、山矾科各属为代表；林上及林外常布满竹子，次生性很强。800 m以上山地常绿阔叶苔藓矮林和山顶灌草丛。主要有杜鹃属、乌饭树属、山矾科、菊科、莎草科及禾本科的植物。"};
    private int[] imageId=new int[]{R.drawable.one};
    private StickyListHeadersListView lv;
    private HeadAdapter adapter;
    private List<ItemBeam> list;
    private LinearLayout actionBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_guide_tab_one);
        lv = (StickyListHeadersListView) findViewById(R.id.activity_guide_one_sticky_list_view);
        list = new ArrayList<ItemBeam>();
        init();
        adapter = new HeadAdapter(this, list);
        lv.setAdapter(adapter);

        actionBack= (LinearLayout) findViewById(R.id.activity_guide_table_one_linear_layout_action_back);
        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void init(){

        ItemBeam itemBeam = new ItemBeam(title[0], text[0],imageId[0],text[1], 0);
        list.add(itemBeam);
        ItemBeam itemBeam2 = new ItemBeam(title[1], text[2], 1);
        list.add(itemBeam2);

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
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            convertView=getLayoutInflater().inflate(R.layout.content,parent,false);
            TextView textView= (TextView) convertView.findViewById(R.id.tx);
            textView.setText(list.get(position).getContent());
            TextView textView1= (TextView) convertView.findViewById(R.id.tx2);
            textView1.setText(list.get(position).getContent2());
            ImageView im1= (ImageView) convertView.findViewById(R.id.im1);
            im1.setImageResource(list.get(position).getImageid());
            im1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(GuideTabOneActivity.this,ShowImageActivity.class);
                    intent.putExtra("title",list.get(position).getImageid());
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
    public class ItemBeam
    {
        private String catalog;
        private String content;
        private int imageid;
        private int id;
        private String content2;
        public ItemBeam(String catalog,String content,int imageid,String content2,int id){
            this.catalog=catalog;
            this.content=content;
            this.imageid=imageid;
            this.id=id;
            this.content2=content2;
        }
        public ItemBeam(String catalog,String content,int id){
            this.catalog=catalog;
            this.content=content;
            this.id=id;
        }

        public  int getImageid(){
            return imageid;
        }
        public String getCatalog(){
            return catalog;
        }
        public String getContent(){
            return content;
        }
        public String getContent2(){
            return content2;
        }
        public int getId()
        {
            return id;
        }

    }
}
