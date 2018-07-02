package com.kefan.blackstone.JsonUtil;

import com.kefan.blackstone.database.Species;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MY SHIP on 2017/3/24.
 * 主要用解析物种清单的json数据
 */

public class JsonResolverList {
    private static JSONObject jsonObject;//创建一个json对象
    private static List<JSONArray> jsonArrayList;//创建一个json对象数组的集合list

    public JsonResolverList(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }//构造方法

    public void Resolve()//数据解析过程//详情去csdn博客上看看json的解析
    {
        /**
         *解析reptiles bird amphibia 物种数据
         */
        jsonArrayList = new ArrayList<>();
        try {
            jsonArrayList.add(jsonObject.getJSONArray("reptiles"));
            jsonArrayList.add(jsonObject.getJSONArray("bird"));
            jsonArrayList.add(jsonObject.getJSONArray("amphibia"));
            jsonArrayList.add(jsonObject.getJSONArray("mamal"));

            for (JSONArray array : jsonArrayList) {
                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    Species species = new Species();
                    species.setSingal(object.getInt("id"));
                    species.setChineseName(object.getString("chineseName"));

                    if (object.has("latinName")) {

                        species.setLatinName(object.getString("latinName"));
                    }
                    if (object.has("englishName")) {
                        species.setEnglishName(object.getString("englishName"));
                    }
                    species.setOrder(object.getString("order"));
                    species.setFamily(object.getString("family"));
                    species.setLatinOrder(object.getString("orderLatin"));
                    species.setLatinFamily(object.getString("familyLatin"));
                    species.setSpeciesType(object.getString("speciesType"));
                    species.setMainPhoto(object.getString("mainPhoto"));
                    species.save();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * * 单独解析insect类，因为inset类没有fiamily字段
         */
        try {
            JSONArray insect = jsonObject.getJSONArray("insect");
            for (int i = 0; i < insect.length(); i++) {

                JSONObject object = insect.getJSONObject(i);
                Species species = new Species();
                species.setSingal(object.getInt("id"));
                species.setChineseName(object.getString("chineseName"));
                species.setLatinName(object.getString("latinName"));
                species.setOrder(object.getString("order"));
                species.setLatinOrder(object.getString("orderLatin"));
                species.setSpeciesType(object.getString("speciesType"));
                species.setMainPhoto(object.getString("mainPhoto"));
                species.save();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
