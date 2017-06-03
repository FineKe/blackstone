package com.hdu.myship.blackstone;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class GuideTableTwoActivity extends AppCompatActivity {
    private String[] title={"两栖爬行动物区系特点","黑石顶鸟类区系特点"};
    private String[] text={"    特有现象显著。封开角蟾 Xenophrys acutus，黑石顶角蟾 X. obesus均为新种；此外，黑石顶刘树蛙 Liuixalus cf. ocellatus、海南臭蛙 Odorrana cf. hainanensis、细鳞树蜥 Pseudocalotes cf. microlepis等均与同属物种存在显著差异。\n" +
            "    中印半岛-中国南部热湿型物种所占比例较高。华南雨蛙 Hyla simplex、海南臭蛙、黑石顶刘树蛙、侧条跳树蛙 、地龟 Geoemyda spengleri、细鳞树蜥、北部湾蜓蜥、南滑蜥 Scincella reevesii、四线石龙子 Plestiodon quadrilineatus、海南棱蜥 Tropidophorus hainanus、中国棱蜥 Tropidophorus sinicus、南草蜥眼斑亚种 Takydromus sexlineatus ocellatus、缅甸钝头蛇 Pareas hamptoni、黄斑后棱蛇和越南烙铁头 Ovophis tonkinensis等共15种属中印半岛-中国南部热湿型物种，占所记录的两栖爬行类总数的20.5%。\n" +
            "    在两栖爬行动物物种组成方面，与相距70 km、面积12 km2、同属云开山脉、被北回线贯穿的鼎湖山自然保护区有较大差别。黎振昌等在鼎湖山实地调查的基础上，通过查阅相关文献，共记录两栖类23种，爬行类48种（Li et al., 2009）。物种数量与黑石顶基本持平，但在物种组成上，两地相差较大。两地相同的两栖类14种，占黑石顶两栖动物的60.8%。其中，鼎湖山所记录的版纳鱼螈 Ichthyophis bannanicus、弹琴蛙 Babina adenopleura、长趾纤蛙 Hylarana macrodactyla、昭觉林蛙 Rana chaochiaoensis（？）、花臭蛙 Odorrana schmackeri、小棘蛙 Quasipaa exilispinosa、尖舌浮蛙 Occidozyga lima、花狭口蛙 Kaloula pulchra和花细狭口蛙 Kalophrynus interlineatus在黑石顶没有记录，而黑石顶所记录的福建掌突蟾 Leptolalax liui、封开角蟾、黑石顶角蟾、林蛙 sp.、海南臭蛙、福建大头蛙 Limnonectes fujianensis、侧条跳树蛙、黑石顶刘树蛙和粗皮姬蛙 Microhyla butleri在鼎湖山没有记录。\n" +
            "    两地均有分布记录的爬行动物共30种，占黑石顶所记录爬行动物总数的60%，其中鼎湖山所记录的黑颈拟水龟 Chinemys nigricans、三线闭壳龟Cuora trifasciata、中国壁虎 Gekko chinensis、疣尾蜥虎 Hemidactylus frenatus、蓝尾石龙子 Plestiodon elegans、钩盲蛇 Ramphotyphlops braminus、蟒蛇 Python molurus、三索锦蛇 Coelognathus radiatus、铅色水蛇 Enhydris plumbea、紫棕小头蛇 Oligodon cinereus、台湾小头蛇 Oligodon formosanus、横纹后棱蛇 Opisthotropis balteata、紫灰锦蛇 Oreophis porphyraceus、黑眉锦蛇 Orthriophis taeniurus、灰鼠蛇 Ptyas korros、金环蛇 Bungarus fasciatus、白唇竹叶青 Trimeresurus albolabris和福建竹叶青 Trimeresurus stejnegeri共18种目前在黑石顶尚无分布记录，而黑石顶所记录的地龟、眼斑水龟 Sacalia bealei、大壁虎 Gekko gecko、云南半叶趾虎 Hemiphyllodactylus yunnanensis、类细鳞树蜥、台湾草蜥 Takydromus kuehnei、宁波滑蜥 Scincella modesta、海南棱蜥、棕脊蛇 Achalinus rufescens Boulenger, 1888、绞花林蛇 Boiga kraepelini、白眉腹链蛇 Amphiesma boulengeri、坡普腹链蛇 Amphiesma popei、菱斑小头蛇 Oligodon catenata、侧条后棱蛇 Opisthotropis lateralis、黄斑后棱蛇、缅甸钝头蛇、滑鼠蛇 Ptyas mucosus、眼镜王蛇 Ophiophagus hannah、原毛头蝮 Protobothrops mucrosquamatus和越南烙铁头共20种在鼎湖山尚无分布记录。\n" +
            "    黑石顶与相距约 200 km、面积2080 km2、属南岭山脉的广西大瑶山相比，物种组成的相似度稍高。黑石顶所记录的两栖动物至少15种亦记录于大瑶山，占黑石顶两栖类总数的65%；爬行动物至少34种亦记录于大瑶山，占黑石顶爬行动物总数的68%。\n" +
            "    综合大瑶山和鼎湖山的两栖爬行动物的物种记录，下列物种亦可能在黑石顶有分布：版纳鱼螈、花狭口蛙、钩盲蛇、三索锦蛇、紫灰锦蛇、铅色水蛇、紫棕小头蛇、台湾小头蛇、台湾钝头蛇 Pareas formosensis、山溪后棱蛇 Opisthotropis latouchii、黑眉锦蛇、灰鼠蛇、白唇竹叶青或福建竹叶青。\n" +
            "    黑石顶蜥蜴亚目物种丰富，尤其是石龙子科特别丰富。黑石顶共记录蜥蜴亚目4科18种，石龙子科4属10种；与其邻近的鼎湖山的蜥蜴亚目为4科14种，石龙子科4属9种（Li et al., 2009）；井冈山的蜥蜴亚目4科11种，石龙子科3属6种；广西大瑶山蜥蜴亚目比较丰富，共有7科21种，但石龙子科仅为3属7种（广西大瑶山自然保护区综合科学考察报告，2008）。",

            "    黑石顶保护区面积较小，且与周边地区自然条件差异较大，孤岛化倾向日益严重。鸟类具有较强迁移能力，能够主动选择适宜环境。因此，黑石顶鸟类物种多样性不高，目前仅记录到131种。但黑石顶鸟类群落具有鲜明特点：\n" +
                    "    优势种现象显著。灰眶雀鹛 Alcippe morrisonia、赤红山椒鸟 Pericrocotus flammeus、灰喉山椒鸟 Pericrocotus solaris、栗耳凤鹛 Yuhina castaniceps、红耳鹎 Pycnonotus jocosus、栗背短脚鹎 Hemixos castanonotus、蓝喉蜂虎 Merops philippinus、黄颊山雀 Parus spilonotus、黑眉拟啄木鸟 Megalaima oorti等为优势鸟种。\n" +
                    "    群落演替现象显著。2010年以前，黑石顶灰喉山椒鸟种群显著大于赤红山椒鸟，从2011年起，赤红山椒鸟种群数量开始激增，而灰喉山椒鸟种群开始变少，目前赤红山椒鸟已成为黑石顶最优势鸟种，与灰喉山椒鸟的比例已经超过10:1。蓝喉蜂虎在黑石顶的首次记录于2011年，至2013年，种群数量已经超过200只。2008年以前，白头鹎 Pycnonotus sinensis的种群大于红耳鹎，至2013年，白头鹎在黑石顶几近绝迹，而红耳鹎数量则处于显著增多趋势。\n" +
                    "    黑石顶鸟类群落季节性变得较大。冬季由于冬候鸟的加入，鸟类数量显著提升，且常常结群活动。夏季尤其是6–7月份，很多鸟类尚处于繁殖末期，常分散活动，结群现象减少。另外，由于大多植物果实尚未成熟，而昆虫则处于全年最鼎盛时期，此时黑石顶鸟类多为食虫鸟类或以昆虫为食鸟类，纯粹植食性鸟类较罕见。\n" +
                    "    除野猪 Sus scrofa和小麂 Muntiacus reevesi，中大型哺乳动物在黑石顶几近绝迹。在周边环境日益破碎和生态退化的背景下，在面积有限、日益孤岛化的黑石顶保护区，大型哺乳动物的种群维持变得十分艰难，因此，在黑石顶保护区目前所能见到的哺乳动物以翼手类和啮齿类最多，野猪和小麂尚有一定的种群数量，文献资料所记录的其他中大型哺乳动物尤其是食肉类已经踪迹全无。"};
    private StickyListHeadersListView lv;
    private HeadAdapter adapter;
    private List<ItemBeam> list;
    private LinearLayout actionBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_guide_table_two);
        lv = (StickyListHeadersListView) findViewById(R.id.activity_guide_two_sticky_list_view);
        list = new ArrayList<ItemBeam>();
        init();
        adapter = new HeadAdapter(this, list);
        lv.setAdapter(adapter);
        actionBack= (LinearLayout) findViewById(R.id.activity_guide_table_two_linear_layout_action_back);
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
        ItemBeam itemBeam2 = new ItemBeam(title[1], text[1], 1);
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
