package com.example.ahsan.newsgateway;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AsyncTask1 extends AsyncTask<String, Void, String> {
    private static final String TAG = "AsyncTask1";
    private Services services;
    private String string ="";
    private String backValue;
    private String string1;
    private JSONArray array = new JSONArray();

    private final String newsSourceUrl = "https://newsapi.org/v1/sources?language=en&country=us&";
    private final String apikey = "c1724b4aa61c43d68bdcfa1860914dec";


    public AsyncTask1(Services services) {
        this.services = services;
    }

    private ArrayList<News> sendValue(String s) {
        if(s==null)
            return null;
        ArrayList<News> arrayList = new ArrayList<>();
        arrayList.clear();
        try {
            JSONObject jsonObject1 = new JSONObject(s);
            JSONArray jsonObject2 = jsonObject1.getJSONArray("sources");
            int i = 0;
            while (i < jsonObject2.length()) {
                JSONObject jsonObject = jsonObject2.getJSONObject(i);
                i++;
                String newsId = jsonObject.getString("id");
                String newsName = jsonObject.getString("name");
                String newsUrl = jsonObject.getString("url");
                String newsCategories = jsonObject.getString("category");
                arrayList.add(new News(newsId, newsName,newsUrl,newsCategories));
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Integer> listArray(JsonReader jsonReader) throws IOException {
        List<Integer> arrayList = new ArrayList<Integer>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            arrayList.add(jsonReader.nextInt());
        }
        jsonReader.endArray();
        return arrayList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {services.sendValue1(result);}

    @Override
    protected String doInBackground(String... params) {
        backValue = params[0];
        Uri.Builder urlBuilder = Uri.parse(newsSourceUrl).buildUpon();
        urlBuilder.appendQueryParameter("apiKey", apikey);
        if(backValue.length()>0)
            urlBuilder.appendQueryParameter("category", params[0]);
        String myUrl = urlBuilder.build().toString();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(myUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            int i = httpURLConnection.getResponseCode();
            if(i!=200)
            {
                return null;
            }
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s).append('\n');
            }
        } catch (Exception e) {
            return null;
        }
        sendValue(stringBuilder.toString().trim());
        return stringBuilder.toString();
    }
}
