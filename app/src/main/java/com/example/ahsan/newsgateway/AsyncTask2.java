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

public class AsyncTask2 extends AsyncTask<String, Void, String> {
    private Services services;
    private static final String TAG = "AsyncTask2";
    private String string ="";
    private String backValue;
    private String string1;
    private JSONArray array = new JSONArray();

    private final String newsSourceUrl = "https://newsapi.org/v1/articles?";
    private final String apiKey = "c1724b4aa61c43d68bdcfa1860914dec";


    public AsyncTask2(Services services) {
        this.services = services;
    }

    private ArrayList<NewsDetails> sendValue(String s) {
        if(s==null)
            return null;
        ArrayList<NewsDetails> arrayList = new ArrayList<>();
        arrayList.clear();
        try {
            JSONObject jsonObject1 = new JSONObject(s);
            JSONArray jsonObject2 = jsonObject1.getJSONArray("articles");
            int i = 0;
            while (i < jsonObject2.length()) {
                JSONObject jsonObject = jsonObject2.getJSONObject(i);
                i++;
                String newsAuthor = jsonObject.getString("author");
                String newsTitle = jsonObject.getString("title");
                String newsDescription = jsonObject.getString("description");
                String ImageUrl = jsonObject.getString("urlToImage");
                String newsDate = jsonObject.getString("publishedAt");
                String newsData = jsonObject.toString();
                arrayList.add(new NewsDetails(newsAuthor, newsTitle,newsDescription,ImageUrl,newsDate,newsData));
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Integer> listArray(JsonReader reader) throws IOException {
        List<Integer> arrayList = new ArrayList<Integer>();
        reader.beginArray();
        while (reader.hasNext()) {
            arrayList.add(reader.nextInt());
        }
        reader.endArray();
        return arrayList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) { services.sendValue2(result);}

    @Override
    protected String doInBackground(String... params) {
        backValue = params[0];

        Uri.Builder urlBuilder = Uri.parse(newsSourceUrl).buildUpon();
        urlBuilder.appendQueryParameter("apiKey", apiKey);
        if(backValue.length()>0)
            urlBuilder.appendQueryParameter("source", params[0]);
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
