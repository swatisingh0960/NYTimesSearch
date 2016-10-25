package com.swatisingh0960.github.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Swati on 10/19/2016.
 */
@Parcel
public class Article {

    String webUrl;
    String headline;
    String thumbNail;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public Article() {

    }

    public Article(JSONObject jsonObject){
        try{
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if(multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else{
                this.thumbNail="";
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    //Create an Array List of Articles
    // fromJSONArray to load items into the Array
    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> results = new ArrayList<>();
    // Iterate through the entire list of JSON Objects and convert each of them individually using the Article constructor
        for(int x=0; x<array.length(); x++){
            try{
            results.add(new Article(array.getJSONObject(x)));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
}
