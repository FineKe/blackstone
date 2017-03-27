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
 */

public class JsonResolverList {
    private String family;
    private static JSONObject  jsonObject;
    private static List<JSONArray> jsonArrayList;
    public JsonResolverList(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;
    }

    public void Resolve()
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
                    Species species=new Species();
                    JSONObject object=array.getJSONObject(i);
                    species.setId(object.getInt("id"));
                    species.setChineseName(object.getString("chineseName"));
                    species.setLatinName(object.getString("latinName"));
                    species.setOrder(object.getString("order"));
                    Log.d("MAIN", "Resolve: "+object.getString("family"));
                    family=object.getString("family");
                    species.setFamily(family);
                    species.setMainPhoto(object.getString("mainPhoto"));
                    species.saveFast();

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
