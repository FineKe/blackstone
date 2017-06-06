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

public class GuideTableFourActivity extends AppCompatActivity{
    private String[] title={"野外鸟类的观察与识别"};
    private String[] text={"    鸟类学研究有诸多的优点和便利之处，因此在野外观察鸟类，记录鸟类的种类、数量和行为是一项基本的训练。观鸟，是指在自然环境中，以不影响野生鸟类正常生活为前提，通过肉眼或借助望远镜等设备，观察鸟类的形态、行为和生活习性的活动，是鸟类学研究的一项基本功。\n" +
            "    如何在野外观察鸟类呢？鸟类的多样性虽然较高、数量较大，但鸟类的动作非常敏捷和迅速，使观察的时间很有限。因此观察者需要在掌握鸟类形态特征的基础之上，通过鸟类的形态、羽色、行为、鸣声、栖息地等诸多信息做出快速、准确的判断。以下介绍一些常用的观鸟技巧。\n" +
            "\n" +
            "3.1 通过形态特征识别\n" +
            "3.1.1 生态类群。我国有鸟类1400余种，依照形态和习性，可分为陆禽、游禽、涉禽、攀禽、猛禽和鸣禽六大类。陆禽如雉类和鸠鸽类，腿粗短而善走；游禽如雁鸭类、潜鸟类等，脚趾间具蹼（蹼有多种），善于游泳和潜水；涉禽如鹭类和秧鸡等，外形具有“三长”特征，即喙（嘴）长、颈长、后肢（腿和脚）长，喜欢在湿地的浅水地带行走觅食；攀禽如啄木鸟、杜鹃、翠鸟等，足（脚）趾发生多种变化，适于在岩壁、石壁、土壁、树干等地方攀缘生活；猛禽如鹰、隼和鸮类，喙和爪强壮发达，末端具弯钩而锋利；所有的雀形目鸟类都属于鸣禽，鸣管较为发达，善于鸣唱。\n" +
            "\n" +
            "3.1.2体型。大小或体长，是个相对的概念，一般可以选取一些华南地区的常见鸟类作为基准，将观察对象跟它进行比较，估计出观察对象的体型大小，作为鸟种判断的依据。在黑石顶地区，我们可以选择以下常见鸟作为参照：暗绿绣眼鸟(10cm)、麻雀(15cm)、红耳鹎(20cm)、乌鸫(25cm)、珠颈斑鸠(30cm)、灰树鹊(38cm)、喜鹊(45cm)、白鹭(60cm)、苍鹭(100cm)。需要注意的是，同种鸟的体型大小存在着个体、年龄和性别差异，所以在野外观察识别时还要结合其他特征。\n" +
            "\n" +
            "3.1.3 羽色。鸟类身体大部分都被羽毛所覆盖，也是我们所直接观察到的部分。体羽颜色是鸟种辨识最直接、最重要的依据，很多鸟类都具有独特的羽色和花纹特别是一些鸟类在繁殖期更有装饰性的羽毛称为“婚羽”。比如鹭类胸前的蓑羽，一些雁鸭类繁殖期雄鸟头部的羽冠、斑块等。通常在看清楚鸟的体态和体型大小后，就能判断出其所属的科目，之后再迅速观察并记住其身体主要部位的颜色和花纹，翻阅图鉴后就能找到它是什么鸟。这里给出一些主要类群的颜色，比如黑白色的鸟（燕尾类、白鹡鸰、鹊鸲、喜鹊等），蓝紫色为主的鸟（翠鸟类、紫啸鸫、仙鹟类），红色为主的鸟（山椒鸟类、朱雀类），黄褐色为主的鸟类（鹛类、鹀类、鸫类、鹨类），绿色为主的鸟类（鹎类、柳莺类、叶鹎类、拟啄木鸟类、绣眼鸟类）。\n" +
            "对于某些相似种，则应观察其细节特征，包括头部纹路、羽缘颜色和整体色彩等，再比照图鉴查找出相应的鸟种。但是需要注意的是，同种鸟不同个体之间的羽色也会有差别。有些鸟类两性羽色有着较大的差异，如雉、鸭、红尾鸲和鹀类等；有些差异较小，如啄木鸟等；有些则几乎没有差异，如鹛、莺、鹨和云雀等。从雏鸟到性成熟，鸟类要经历数次换羽，幼鸟羽色往往和成鸟有异，在野外观察时需多留意。\n" +
            "\n" +
            "3.1.4 喙。俗称嘴，是鸟类的取食器官，鸟喙的形状和鸟类的食性有着密切关系。了解鸟类的喙部形态不仅有助于物种的鉴别，还能初步判断鸟类的食性。如戴胜的喙，极细长而下弯，适合夹取缝隙中的昆虫；猛禽的喙，强壮而带锋利的弯钩，用来撕碎食物；啄木鸟的喙，粗壮而厚实，可以用来啄洞；鸫类的喙，长且较粗，便于在地面上翻动寻找食物；鹟类的喙，通常较宽且胡须发达，便于在飞行中捕捉昆虫；山雀类的喙，较为细小而锋利，擅长搜寻树叶表面和树干内的小型昆虫。\n" +
            "\n" +
            "3.1.5 翼型。在鸟类飞行时，可根据翼型来辨识，尤其对于鹰隼类猛禽。通常来说，隼科鸟类翅膀狭长，末端较尖，无翼指；鹰科鸟类翼型浑圆，有翼指。同样的，鸟类的翅膀形状可以判断他们的迁徙能力，如很多鹎类翅膀短圆，它们通常是留鸟或者短距离迁徙鸟类。黑石顶的冬候鸟、旅鸟如白腹鸫、蓝歌鸲则翅膀较为尖长，这类翅膀适合长距离迁徙。\n" +
            "\n" +
            "3.1.6 尾型。鸟类尾部形状可分为平尾、圆尾、凸尾、凹尾、尖尾、楔尾和叉尾等，尾型也是重要的辨识特征。比如黑石顶常见的家燕、金腰燕、白冠燕尾具有叉形尾，黑卷尾和发冠卷尾具有凹尾，很多鸫类都是平尾，而黄腹鹪莺和褐头鹪莺以及很多雀类、鹀类为凹尾。\n" +
            "\n" +
            "3.1.7 虹膜和腿。有些近似鸟种的虹膜和腿色会有差异，如鹰科鸟类两性的虹膜颜色有差异，可以作为识别依据。鹎类中很多种类的腿部颜色都很暗淡，但是黑鹎的腿颜色为鲜红。\n" +
            "\n" +
            "3.2 生境和居留型\n" +
            "鸟类因生活习性的不同，对栖息地类型有着各自的要求，它们需要在特定的生境中觅食和繁殖，完成其生活史的一部分或者全部。\n" +
            "\n" +
            "3.2.1 生境。有些鸟类如麻雀、白头鹎则分布在多样的生境中，多伴人生活，在黑石顶保护区多出现在村庄周围和低海拔地区，而栗背短脚鹎、黑短脚鹎等则喜欢植被较好的中高海拔密林山区。又如鸫类和鹛类等，多在地面和底灌层中取食，行动隐蔽，不易观察，柳莺类、鹎类和鸦科鸟类则较为活跃，多在树冠的中上层活动。从上述例子说明不同的鸟类具有不同的生境偏好,因此有时可以根据生境类型来判断可能出现的种类。\n" +
            "\n" +
            "3.2.2 居留型。是指相对于一个地区而言，鸟类在该地居留的季节性。很多鸟类有迁徙的习性，如黄眉柳莺、树鹨在黑石顶保护区为冬候鸟，只有11-3月可以见到；鹎类、鹛类、山雀类为黑石顶的留鸟，终年可以见到；鹟类、蜂虎类、杜鹃类等食虫鸟为夏候鸟，集中出现在5-9月；部分鸲类、鹨类等仅仅在春季和秋季迁徙经过黑石顶，称为旅鸟。生境和居留型判断并不绝对，某些鸟在受天气或其他原因影响时，会出现远离分布区的情况，称为迷鸟。\n" +
            "\n" +
            "3.3 通过行为识鸟\n" +
            "3.3.1 听声辨鸟。声音是鸟类交流的通讯手段。有些类别的鸟，如鹭类，不同种间叫声差异并不大；但对于鸮、鹃和绝大多数雀形目鸟类来说，其叫声尤其是繁殖期鸣唱通常是独特的。熟悉各种鸟声，对于发现隐蔽较好的和夜间活动的鸟种，或区别近似种有重要意义。如在黑石顶广泛分布的黑眉拟啄木鸟，常躲藏在茂密的树冠层而难以发现，可以通过它极具节奏的连续单音来定位；多闻其声不见其影的灰胸竹鸡叫声近似“地主婆”；红翅凤头鹃常发出闹铃般“滴滴，滴滴”的鸣叫。多数鸟鸣在野外调查时可作为识别依据，但有一些鸟类擅长学习其他鸟类的叫声，如画眉可模仿仙八色鸫，黑喉噪鹛可模仿鹰鹃，这些如果不注意都容易引起误判。听声识鸟需要较长时间的学习和实践，才能快速辨别出鸟种。\n" +
            "\n" +
            "3.3.2 行为辨鸟\n" +
            "通过一些鸟类的典型行为，可以快速地识别鸟类。如很多鸟类的飞行路线不是直线，啄木鸟的飞行就如同很深的“波浪状”。类似地，白鹡鸰飞行时也呈“小波浪”。又如，很多鸟在静止的时候，尾部常常会摆动，如鹊鸲的尾巴常上下摆动，但是棕背伯劳则是左右摆动。上述所说这些行为都能帮助我们识别种类，初学者可以通过自己的观察慢慢积累这些知识。"};
    private StickyListHeadersListView lv;
    private HeadAdapter adapter;
    private List<ItemBeam> list;
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
