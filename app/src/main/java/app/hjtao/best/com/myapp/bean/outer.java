package app.hjtao.best.com.myapp.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Outer {
    private boolean error;
    private List<Results> results;

    public Outer() {
    }

    public Outer(boolean error, List<Results> results) {
        this.error = error;
        this.results = results;
    }

    public static Outer parseJson(String json) {
        Outer outer = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            Log.d(TAG,json);
            boolean error = jsonObject.getBoolean("error");
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            List<Results> list = new ArrayList<Results>();
            for (int i = 0; i < jsonArray.length(); i++) {
                List<String> imagesList = new ArrayList<String>();
                imagesList = new ArrayList<String>();
                JSONObject jsonResults = (JSONObject) jsonArray.get(i);
                String _id = jsonResults.getString("_id");
                String createdAt = jsonResults.getString("createdAt");
                String desc = jsonResults.getString("desc");

                if (jsonResults.has("images")) {
                    JSONArray imagesArray = jsonResults.getJSONArray("images");
                    for (int j = 0; j < imagesArray.length(); j++) {
                        String images = (String) imagesArray.get(j);
                        Log.d(TAG, "onResponse: " + images);
                        imagesList.add(images);
                    }
                }
                String publishedAt = jsonResults.getString("publishedAt");
                String source = jsonResults.getString("source");
                String type = jsonResults.getString("type");
                String url = jsonResults.getString("url");
                Boolean used = jsonResults.getBoolean("used");
                String who = jsonResults.getString("who");
                Results results = new Results(_id, createdAt, desc, imagesList, publishedAt, source, type, url, used, who);
                list.add(results);
            }
            Log.d(TAG, list.toString());
            outer = new Outer();
            outer.setError(error);
            outer.setResults(list);
           // Log.d(TAG, "onResponse: " + outer.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return outer;
    }


    public boolean isError(){
        return error;
    }

    public List<Results>getResults(){
        return results;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Outer{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }



}

