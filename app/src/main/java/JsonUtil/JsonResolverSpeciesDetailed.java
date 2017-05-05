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

    public Amphibia ResolveAmphibia(JSONObject jsonObject) {
        try {
            int code = jsonObject.getInt("code");
            if (code == 88) {
                JSONObject data = jsonObject.getJSONObject("data");
                int singl = data.getInt("id");
                String chineseName = data.getString("chineseName");
                String latinName = data.getString("latinName");
                String order = data.getString("order");
                String orderLatin = data.getString("orderLatin");
                String family = data.getString("family");
                String familyLatin = data.getString("familyLatin");
                String genus = data.getString("genus");
                String genusLatin = data.getString("genusLatin");
                String mainPhoto = data.getString("mainPhoto");
                JSONArray imgs = data.getJSONArray("imgs");

                JSONObject features = data.getJSONObject("features");
                String shape = features.getString("shape");
                String sub_color = features.getString("sub_color");
                String population_status = features.getString("population_status");
                String major_color = features.getString("major_color");
                String tweet = features.getString("tweet");
                String distribution = features.getString("distribution");
                String vocal_sac = features.getString("vocal_sac");
                String web = features.getString("web");
                String vague_feature = features.getString("vague_feature");
                String biotope = features.getString("biotope");
                String digital_formula = features.getString("digital_formula");
                String nuptial = features.getString("nuptial");

                String speciesType = data.getString("speciesType");

                Amphibia speciesDetailed = new Amphibia();

                speciesDetailed.setSingl(singl);
                speciesDetailed.setChineseName(chineseName);
                speciesDetailed.setLatinName(latinName);
                speciesDetailed.setOrder(order);
                speciesDetailed.setOrderLatin(orderLatin);
                speciesDetailed.setFamily(family);
                speciesDetailed.setFamilyLatin(familyLatin);
                speciesDetailed.setGenus(genus);
                speciesDetailed.setGenusLatin(genusLatin);
                speciesDetailed.setMainPhoto(mainPhoto);
                ArrayList<String> imgsList = new ArrayList<>();

                for (int i = 0; i < imgs.length(); i++) {
                    imgsList.add(imgs.getString(i));
                    Log.d("TAG", "onResponse: " + imgs.getString(i));
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

                return speciesDetailed;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bird ResolveBird(JSONObject jsonObject) {
        try {
            int code = jsonObject.getInt("code");
            if (code == 88) {
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
                bird.setMainPhoto(data.getString("mainPhoto"));
                JSONArray imgs = data.getJSONArray("imgs");
                ArrayList<String> imgsList = new ArrayList<>();
                for (int i = 0; i < imgs.length(); i++) {
                    imgsList.add(imgs.getString(i));
                    Log.d("TAG", "onResponse: " + imgs.getString(i));
                }
                bird.setImgs(imgsList);
                JSONObject features = data.getJSONObject("features");
                bird.setHabitat(features.getString("habitat"));
                bird.setShape(features.getString("shape"));
                bird.setBeak_shape(features.getString("beak_shape"));
                bird.setEating_pattern(features.getString("eating_pattern"));
                //bird.setSub_color(features.getString("sub_pattern"));
                bird.setMajor_color(features.getString("major_color"));
                bird.setTweet(features.getString("tweet"));
                bird.setFly_pattern(features.getString("fly_pattern"));
                bird.setSpeciesType(data.getString("speciesType"));
                return bird;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Insect ResolveInsect(JSONObject jsonObject) {
        try {
            int code = jsonObject.getInt("code");
            if (code == 88) {
                Insect insect = new Insect();
                JSONObject data = jsonObject.getJSONObject("data");
                insect.setSingal(data.getInt("id"));
                insect.setChineseName(data.getString("chineseName"));
                insect.setLatinName(data.getString("latinName"));
                insect.setOrder(data.getString("order"));
                insect.setOrderLatin(data.getString("orderLatin"));
                insect.setMainPhoto(data.getString("mainPhoto"));
                JSONArray imgs = data.getJSONArray("imgs");
                ArrayList<String> imgsList = new ArrayList<>();
                for (int i = 0; i < imgs.length(); i++) {
                    imgsList.add(imgs.getString(i));
                    Log.d("TAG", "onResponse: " + imgs.getString(i));
                }
                insect.setImgs(imgsList);
                insect.setDescription(data.getString("description"));
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
                return insect;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Reptiles ResolveRepties(JSONObject jsonObject) {
        try {
            int code = jsonObject.getInt("code");
            if (code == 88) {
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
                reptiles.setMainPhoto(data.getString("mainPhoto"));
                JSONArray imgs = data.getJSONArray("imgs");
                ArrayList<String> imgsList = new ArrayList<>();
                for (int i = 0; i < imgs.length(); i++) {
                    imgsList.add(imgs.getString(i));
                    Log.d("TAG", "onResponse: " + imgs.getString(i));
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
                reptiles.setSpeciesType(data.getString("speciesType"));
                return reptiles;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
