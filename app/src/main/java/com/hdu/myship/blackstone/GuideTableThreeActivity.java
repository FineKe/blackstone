package com.hdu.myship.blackstone;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class GuideTableThreeActivity extends AppCompatActivity {

    private String[] title={"黑石顶昆虫物种多样性概述"};
    private String[] text={"    黑石顶自然保护区的昆虫资源丰富。早于1989年发表的中山大学师生黑石顶综合考察报告便记录了988种，随后陆续发表了封开匙同蝽 Elasmucha fengkainica、黑条卵翅蝗 Caryanda nigrolineata、隆背澳汉蚱 Austrohancockia gibba、长背台蚱 Formosatettix longidorsalis、斑角镰蚱 Falconius annuliconus等新种。香港嘉道理农场暨植物园于1997年和2002年组织的调查增加了99种（不含淡水无脊椎动物未定种），2012年中山大学硕士研究生童博的毕业论文新增蛾类记录461种。至此，黑石顶自然保护区昆虫记录已超1550种。连续多年的黑石顶昆虫多样性本底调查和生物学野外教学实习，累积了上万张高质量的野外昆虫照片，为本app的编写打下了良好基础。受篇幅所限，基于野外教学实习的特点和基本要求，本app仅收录了实习期间较常见的并能体现黑石顶昆虫区系特点的类群，以目为单位，进行介绍。以为引玉之砖，激发认识自然、探索自然的兴趣，引导并帮助读者进一步了解、认识、研究黑石顶昆虫。"};
    private StickyListHeadersListView lv;
    private HeadAdapter adapter;
    private List<ItemBeam> list;

    private LinearLayout actionBack;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_table_three);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
        lv = (StickyListHeadersListView) findViewById(R.id.activity_guide_three_sticky_list_view);
        list = new ArrayList<ItemBeam>();
        init();
        adapter = new HeadAdapter(this, list);
        lv.setAdapter(adapter);
        actionBack= (LinearLayout) findViewById(R.id.activity_guide_table_three_linear_layout_action_back);
        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void init(){
        ItemBeam itemBeam = new ItemBeam(title[0], text[0], 0);
        list.add(itemBeam);

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
            Spannable spn = new SpannableString(list.get(position).getContent());
            String te=list.get(position).getContent();
            for(int i=0;i<te.length();i++){
                char item=te.charAt(i);
                if((item>='a'&&item<='z')||(item>='A'&&item<='Z')){
                    StyleSpan ss = new StyleSpan(Typeface.ITALIC);
                    spn.setSpan(ss, i, i+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }//英文斜体
            TextView textView= (TextView) convertView.findViewById(R.id.tx);
            textView.setText(spn);
            return convertView;
        }
    }
    public class ItemBeam
    {
        private String catalog;
        private String content;
        private int id;
        public ItemBeam(String catalog,String content,int id){
            this.catalog=catalog;
            this.content=content;
            this.id=id;
        }
        public String getCatalog(){
            return catalog;
        }
        public String getContent(){
            return content;
        }
        public int getId()
        {
            return id;
        }

    }
}
