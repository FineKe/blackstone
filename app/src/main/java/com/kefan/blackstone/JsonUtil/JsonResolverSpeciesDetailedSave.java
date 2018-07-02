package com.kefan.blackstone.JsonUtil;

import com.kefan.blackstone.database.Amphibia;
import com.kefan.blackstone.database.Bird;
import com.kefan.blackstone.database.Insect;
import com.kefan.blackstone.database.Mamal;
import com.kefan.blackstone.database.Reptiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by MY SHIP on 2017/4/29.
 */

public class JsonResolverSpeciesDetailedSave {
    private JSONObject jsonObject;
    private Object resultObject;

    public JsonResolverSpeciesDetailedSave(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Object getResultObject() {
        return resultObject;
    }

    public void setResultObject(Object resultObject) {
        this.resultObject = resultObject;
    }

    public void ResolveSpeciesDetailed() {
        try {
            int code = jsonObject.getInt("code");
            if (code == 88) {
                JSONObject data = jsonObject.getJSONObject("data");
                switch (data.getString("speciesType")) {
                    case "reptiles":
                        resultObject = ResolveRepties(jsonObject);
                        break;
                    case "bird":
                        resultObject = ResolveBird(jsonObject);
                        break;
                    case "amphibia":
                        resultObject = ResolveAmphibia(jsonObject);
                        break;
                    case "insect":
                        resultObject = ResolveInsect(jsonObject);
                        break;
                    case "mamal":
                        resultObject = ResolveMamal(jsonObject);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Amphibia ResolveAmphibia(JSONObject jsonObject) {
        Amphibia amphibia = new Amphibia();
        try {
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
            JSONObject features = data.getJSONObject("features");
            if (features.has("resolution_feature")) {
                amphibia.setResolution_feature(features.getString("resolution_feature"));
            } else {
                amphibia.setResolution_feature("");
            }

            amphibia.setShape(features.getString("shape"));
            amphibia.setTone(features.getString("tone"));
            if (features.has("vocal_sac")) {
                amphibia.setVocal_sac(features.getString("vocal_sac"));
            } else {
                amphibia.setVocal_sac("");
            }

            if (features.has("nuptial")) {
                amphibia.setNuptial(features.getString("nuptial"));
            } else {
                amphibia.setNuptial("");
            }
            if (features.has("digital_formula")) {
                amphibia.setDigital_formula(features.getString("digital_formula"));
            } else {
                amphibia.setDigital_formula("");
            }
            amphibia.setWeb(features.getString("web"));
            amphibia.setVague_feature(features.getString("vague_feature"));
            amphibia.setBiotope(features.getString("biotope"));
            amphibia.setDistribution(features.getString("distribution"));
            amphibia.setPopulation_status(features.getString("population_status"));
            amphibia.setSpeciesType(data.getString("speciesType"));
            if (data.has("collected")) {
                amphibia.setCollected(data.getBoolean("collected"));
            } else {
                amphibia.setCollected(false);
            }
            amphibia.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return amphibia;
    }

    private Bird ResolveBird(JSONObject jsonObject) {
        Bird bird = new Bird();
        try {


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
            if (data.has("collected")) {
                bird.setCollected(data.getBoolean("collected"));
            } else {
                bird.setCollected(false);
            }
            bird.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bird;
    }

    private Insect ResolveInsect(JSONObject jsonObject) {
        Insect insect = new Insect();
        try {


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
            if (data.has("collected")) {
                insect.setCollected(data.getBoolean("collected"));
            } else {
                insect.setCollected(false);
            }
            insect.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return insect;
    }

    private Reptiles ResolveRepties(JSONObject jsonObject) {
        Reptiles reptiles = new Reptiles();
        try {
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
            if (data.has("collected")) {
                reptiles.setCollected(data.getBoolean("collected"));
            } else {
                reptiles.setCollected(false);
            }
            reptiles.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reptiles;
    }

    private Mamal ResolveMamal(JSONObject jsonObject) {


            Mamal mamal = new Mamal();
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                mamal.setSingal(data.getInt("id"));
                mamal.setChineseName(data.getString("chineseName"));

                mamal.setOrder(data.getString("order"));
                mamal.setOrderLatin(data.getString("orderLatin"));
                mamal.setFamily(data.getString("family"));
                mamal.setFamilyLatin(data.getString("familyLatin"));
                JSONArray imgs = data.getJSONArray("imgs");
                ArrayList<String> imgsList = new ArrayList<>();
                for (int i = 0; i < imgs.length(); i++) {
                    imgsList.add(imgs.getJSONObject(i).getString("url"));
                }
                mamal.setImgs(imgsList);
                JSONObject features = data.getJSONObject("features");
                mamal.setMain_color(features.getString("main_color"));
                mamal.setHabits(features.getString("habits"));
                mamal.setHabitat(features.getString("habitat"));
                mamal.setShape(features.getString("shape"));
                mamal.setTail(features.getString("tail"));
                mamal.setLength(features.getString("length"));
                mamal.setSub_color(features.getString("sub_color"));
                mamal.setPots(features.getString("pots"));
                mamal.setDistribution(features.getString("distribution"));
                mamal.setProtect_level(features.getString("protect_level"));


                if (data.has("collected")) {
                    mamal.setCollected(data.getBoolean("collected"));
                } else {
                    mamal.setCollected(false);
                }
                mamal.save();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mamal;



    }

}
