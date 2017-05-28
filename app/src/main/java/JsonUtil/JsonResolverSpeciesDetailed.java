package JsonUtil;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import database.Amphibia;
import database.Bird;
import database.Insect;
import database.Reptiles;

/**
 * Created by MY SHIP on 2017/4/29.
 */

public class JsonResolverSpeciesDetailed {
    private JSONObject jsonObject;
    public JsonResolverSpeciesDetailed(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
    public void ResolveSpeciesDetailed(){
        try {
            int code=jsonObject.getInt("code");
            if(code==88)
            {
                JSONObject data=jsonObject.getJSONObject("data");
             switch (data.getString("speciesType"))
                {
                    case "reptiles":ResolveRepties(jsonObject);
                        break;
                    case "bird":ResolveBird(jsonObject);
                        break;
                    case "amphibia":ResolveAmphibia(jsonObject);
                        break;
                    case "insect":ResolveInsect(jsonObject);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ResolveAmphibia(JSONObject jsonObject) {
        try {
            Amphibia amphibia = new Amphibia();
            JSONObject data = jsonObject.getJSONObject("data");
            amphibia.setSingal(data.getInt("id"));
            amphibia.setChineseName(data.getString("chineseName"));
            amphibia.setLatinName(data.getString("latinName"));
            amphibia.setOrder(data.getString("order"));
            amphibia.setOrderLatin(data.getString("orderLatin"));
            amphibia.setFamily(data.getString("family"));
            amphibia.setFamilyLatin(data.getString("familyLatin"));
            amphibia.setGenus(data.getString("genus"));
            amphibia.setGenusLatin(data.getString("genusLatin"));
            JSONArray imgs = data.getJSONArray("imgs");
            ArrayList<String> imgsList = new ArrayList<>();
            for (int i = 0; i < imgs.length(); i++) {
                imgsList.add(imgs.getJSONObject(i).getString("url"));
            }
            amphibia.setImgs(imgsList);
            JSONObject features=data.getJSONObject("features");
            if(features.has("resolution_feature"))
            {
                amphibia.setResolution_feature(features.getString("resolution_feature"));
            }else
            {
                amphibia.setResolution_feature("");
            }

            amphibia.setShape(features.getString("shape"));
            amphibia.setTone(features.getString("tone"));
            if (features.has("vocal_sac"))
            {
                amphibia.setVocal_sac(features.getString("vocal_sac"));
            }
            else
            {
                amphibia.setVocal_sac("");
            }

            if(features.has("nuptial"))
            {
                amphibia.setNuptial(features.getString("nuptial"));
            }
            else
            {
                amphibia.setNuptial("");
            }
            if(features.has("digital_formula"))
            {
                amphibia.setDigital_formula(features.getString("digital_formula"));
            }else
            {
                amphibia.setDigital_formula("");
            }
            amphibia.setWeb(features.getString("web"));
            amphibia.setVague_feature(features.getString("vague_feature"));
            amphibia.setBiotope(features.getString("biotope"));
            amphibia.setDistribution(features.getString("distribution"));
            amphibia.setPopulation_status(features.getString("population_status"));
            amphibia.setSpeciesType(data.getString("speciesType"));
            amphibia.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void ResolveBird(JSONObject jsonObject) {
        try {

                Bird bird = new Bird();
                JSONObject data = jsonObject.getJSONObject("data");
                bird.setSingal(data.getInt("id"));
                bird.setChineseName(data.getString("chineseName"));
                bird.setEnglishName(data.getString("englishName"));
                bird.setLatinName(data.getString("latinName"));
                bird.setOrder(data.getString("order"));
                bird.setOrderLatin(data.getString("orderLatin"));
                bird.setFamily(data.getString("family"));
                bird.setFamilyLatin(data.getString("familyLatin"));
                bird.setGenusLatin(data.getString("genusLatin"));
                JSONArray imgs = data.getJSONArray("imgs");
                ArrayList<String> imgsList = new ArrayList<>();
                for (int i = 0; i < imgs.length(); i++) {
                    imgsList.add(imgs.getJSONObject(i).getString("url"));
                }
                bird.setImgs(imgsList);
                JSONObject features = data.getJSONObject("features");
                bird.setTail_shape(features.getString("tail_shape"));
                bird.setHabitat(features.getString("habitat"));
                bird.setBody_long(features.getString("body_long"));
                bird.setShape(features.getString("shape"));
                bird.setBeak_shape(features.getString("beak_shape"));
                bird.setEating_pattern(features.getString("eating_pattern"));
                bird.setTone(features.getString("tone"));
                bird.setTweet_name(features.getString("tweet_name"));
                bird.setVague_feature(features.getString("vague_feature"));
                bird.setTweet(features.getString("tweet"));
                bird.setFly_pattern(features.getString("fly_pattern"));
                bird.setSpeciesType(data.getString("speciesType"));
                bird.setAudio(features.getString("tweet_sound"));
                bird.setAudioPicture(features.getString("tweet_image"));
                bird.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void ResolveInsect(JSONObject jsonObject) {
        try {

                Insect insect = new Insect();
                JSONObject data = jsonObject.getJSONObject("data");
                insect.setSingal(data.getInt("id"));
                insect.setChineseName(data.getString("chineseName"));
                insect.setLatinName(data.getString("latinName"));
                insect.setOrder(data.getString("order"));
                insect.setOrderLatin(data.getString("orderLatin"));
                JSONArray imgs = data.getJSONArray("imgs");
                ArrayList<String> imgsList = new ArrayList<>();
                for (int i = 0; i < imgs.length(); i++) {
                    imgsList.add(imgs.getJSONObject(i).getString("url"));
                }
                insect.setImgs(imgsList);
                JSONObject features = data.getJSONObject("features");
                insect.setRough_feature(features.getString("rough_feature"));
                insect.setTentacle(features.getString("tentacle"));
                insect.setLife_cycle(features.getString("life_cycle"));
                insect.setMouthparts(features.getString("mouthparts"));
                insect.setCommon_family(features.getString("common_family"));
                insect.setOgnathus(features.getString("ognathus"));
                insect.setWing(features.getString("wing"));
                insect.setYoung_feature(features.getString("young_feature"));
                insect.setLeg(features.getString("leg"));
                insect.setSpeciesType(data.getString("speciesType"));
                insect.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void ResolveRepties(JSONObject jsonObject) {
        try {

                Reptiles reptiles = new Reptiles();
                JSONObject data = jsonObject.getJSONObject("data");
                reptiles.setSingal(data.getInt("id"));
                reptiles.setChineseName(data.getString("chineseName"));
                reptiles.setLatinName(data.getString("latinName"));
                reptiles.setOrder(data.getString("order"));
                reptiles.setOrderLatin(data.getString("orderLatin"));
                reptiles.setFamily(data.getString("family"));
                reptiles.setFamilyLatin(data.getString("familyLatin"));
                reptiles.setGenus(data.getString("genus"));
                reptiles.setGenusLatin(data.getString("genusLatin"));
                JSONArray imgs = data.getJSONArray("imgs");
                ArrayList<String> imgsList = new ArrayList<>();
                for (int i = 0; i < imgs.length(); i++) {
                    imgsList.add(imgs.getJSONObject(i).getString("url"));
                }
                reptiles.setImgs(imgsList);
                JSONObject features = data.getJSONObject("features");
                reptiles.setHas_bigscale(features.getString("has_bigscale"));
                reptiles.setHabitat(features.getString("habitat"));
                reptiles.setShape(features.getString("shape"));
                reptiles.setResolution_feature(features.getString("resolution_feature"));
                reptiles.setVague_feature(features.getString("vague_feature"));
                reptiles.setSub_color(features.getString("sub_color"));
                reptiles.setEating_pattern(features.getString("eating_pattern"));
                reptiles.setMajor_color(features.getString("major_color"));
                reptiles.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
