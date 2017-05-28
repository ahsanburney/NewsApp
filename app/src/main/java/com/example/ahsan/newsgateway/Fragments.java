package com.example.ahsan.newsgateway;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.sephiroth.android.library.picasso.Picasso;


public class Fragments extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private String TAG="Fragments";
    private static MainActivity activity;
    ImageView imageView;
    private String url ;

    public int checkNetwork(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting())) {
            AlertDialog.Builder build = new AlertDialog.Builder(activity);
            build.setMessage("News Cannot be updated without an Internet Connection");
            build.setTitle("No Internet Connection");
            AlertDialog alertDialog = build.create();
            alertDialog.show();
            return 0;
        }
        else {
            return 1;
        }
    }
    private void receiveImage(final String imageURL) {
        if (imageURL.equals("")|| checkNetwork()!=1)
            return;
        {
        }
        Picasso picassoImage = new Picasso.Builder(activity)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso listener, Uri uri, Exception exception) {listener.load(R.drawable.loadingimage).into(imageView);
                    }
                })
                .build();
        picassoImage.load(imageURL).error(R.drawable.imagebroken).placeholder(R.drawable.loadingimage).into(imageView);
    }

    public static final Fragments newFragments(String s, MainActivity mainActivity)
    {
        activity = mainActivity;
        Fragments fragments = new Fragments();
        Bundle bundle = new Bundle(1);
        bundle.putString(EXTRA_MESSAGE, s);
        fragments.setArguments(bundle);
        return fragments;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup group,
                             Bundle bundle) {
        String s = getArguments().getString(EXTRA_MESSAGE);
        View view = layoutInflater.inflate(R.layout.myfragment, group, false);
        try{
            final JSONObject jsonObject = new JSONObject(s);
            imageView = (ImageView)view.findViewById(R.id.image);
            TextView title = (TextView)view.findViewById(R.id.title);
            title.setText(jsonObject.getString("title"));
            TextView  desc= (TextView)view.findViewById(R.id.descriptionblock);
            desc.setText(jsonObject.getString("description"));
            TextView auth = (TextView)view.findViewById(R.id.authorblock);
            auth.setText(jsonObject.getString("author"));
            TextView counter = (TextView)view.findViewById(R.id.pagecount);
            counter.setText(jsonObject.getString("Count"));
            receiveImage(jsonObject.getString("urlToImage"));
            url = jsonObject.getString("url");

            title.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        getUrl(jsonObject.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        getUrl(jsonObject.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            auth.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        getUrl(jsonObject.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            String dateFormat = jsonObject.getString("publishedAt");
            String replace = dateFormat.replace( "T" , " " );
            String replace2 = replace.replace("Z","");
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            try {

                Date date = dateFormat1.parse(replace2);
                dateFormat = dateFormat2.format(date);

            }  catch (ParseException e) {
                e.printStackTrace();
            }

            TextView dates = (TextView)view.findViewById(R.id.date);
            dates.setText(dateFormat);

            desc.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        getUrl(jsonObject.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return view;
    }
    public void getUrl(String getUrls)
    {
        if(checkNetwork()!=1)
        {
        }
        if(getUrls.equals(" "))
            return;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getUrls));
        startActivity(i);
    }
}