package JsonUtil;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import database.SpeciesDetailed;

/**
 * Created by MY SHIP on 2017/4/29.
 */

public class JsonResolverSpeciesDetailed {
    private JSONObject jsonObject;

    public JsonResolverSpeciesDetailed(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    private void Resolve()
    {
        try {
            int code=jsonObject.getInt("code");
            if(code==88)
            {
                JSONObject data=jsonObject.getJSONObject("data");
                int singl=data.getInt("id");
                String chineseName=data.getString("chineseName");
                String latinName=data.getString("latinName");
                String family=data.getString("family");
                String genus=data.getString("genus");
                String mainPhoto=data.getString("mainPhoto");
                JSONArray imgs=data.getJSONArray("imgs");

                JSONObject features=data.getJSONObject("features");
                String shape=features.getString("shape");
                String sub_color=features.getString("sub_color");
                String population_status=features.getString("population_status");
                String major_color=features.getString("major_color");
                String tweet=features.getString("tweet");
                String distribution=features.getString("distribution");
                String vocal_sac=features.getString("vocal_sac");
                String web=features.getString("web");
                String vague_feature=features.getString("vague_feature");
                String biotope=features.getString("biotope");
                String digital_formula=features.getString("digital_formula");
                String nuptial=features.getString("nuptial");

                String speciesType=data.getString("speciesType");

                SpeciesDetailed speciesDetailed=new SpeciesDetailed();

                speciesDetailed.setSingl(singl);
                speciesDetailed.setChineseName(chineseName);
                speciesDetailed.setLatinName(latinName);
                speciesDetailed.setFamily(family);
                speciesDetailed.setGenus(genus);
                speciesDetailed.setMainPhoto(mainPhoto);
                ArrayList<String> imgsList=new ArrayList<>();

                for(int i=0;i<imgs.length();i++)
                {
                    imgsList.add(imgs.getString(i));
                    Log.d("TAG", "onResponse: "+imgs.getString(i));

                }
                speciesDetailed.setImgs(imgsList);
                speciesDetailed.setShape(shape);
                speciesDetailed.setSubColor(sub_color);
                speciesDetailed.setPopulationStatus(population_status);
                speciesDetailed.setMajorColor(major_color);
                speciesDetailed.setTweet(tweet);
                speciesDetailed.setDistribution(distribution);
                speciesDetailed.setVocalSac(vocal_sac);
                speciesDetailed.setBiotope(biotope);
                speciesDetailed.setDigitalFormula(digital_formula);
                speciesDetailed.setNuptial(nuptial);
                speciesDetailed.setSpeciesType(speciesType);
                speciesDetailed.saveFast();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
