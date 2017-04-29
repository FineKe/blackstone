package JsonUtil;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import database.Species;

/**
 * Created by MY SHIP on 2017/3/24.
 * 主要用解析物种清单的json数据
 */

public class JsonResolverList {
    private static JSONObject  jsonObject;//创建一个json对象
    private static List<JSONArray> jsonArrayList;//创建一个json对象数组的集合list
    public JsonResolverList(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;
    }//构造方法

    public void Resolve()//数据解析过程//详情去csdn博客上看看json的解析
    {
        jsonArrayList=new ArrayList<>();
        try {
            jsonArrayList.add(jsonObject.getJSONArray("reptiles"));
            jsonArrayList.add(jsonObject.getJSONArray("bird"));
            jsonArrayList.add(jsonObject.getJSONArray("insect"));
            jsonArrayList.add(jsonObject.getJSONArray("amphibia"));

            for(JSONArray array:jsonArrayList)
            {

                for(int i=0;i<array.length();i++)
                {
                    /**
                     * 讲json中数据解析出来并存入到数据库中
                     */
                    Species species=new Species();
                    JSONObject object=array.getJSONObject(i);
                    species.setSingl(object.getInt("id"));
                    species.setChineseName(object.getString("chineseName"));
                    species.setLatinName(object.getString("latinName"));
                    /**
                     * 该段由于order，family字段没有值所以暂时不解析
                     */
                   // species.setOrder(object.getString("order"));
                   // Log.d("MAIN", "Resolve: "+object.getString("family"));
                   // family=object.getString("family");
                    //species.setFamily(family);
                    species.setSpeciesType(object.getString("speciesType"));
                    species.setMainPhoto(object.getString("mainPhoto"));
                    species.saveFast();

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
