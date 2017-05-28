package com.example.ahsan.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private mAdapter adapter;
    private List<Fragment> fragmentList;
    private ViewPager pager;
    static final String SERVICE_DATA1 = "SERVICE_DATA1";
    static final String SERVICE_DATA2 = "SERVICE_DATA2";
    static final String SAMPLE_BROADCAST_TYPE_A = "SAMPLE_BROADCAST_TYPE_A";
    static final String SAMPLE_BROADCAST_TYPE_B = "SAMPLE_BROADCAST_TYPE_C";
    private mReceiver mReceiver;
    private String updatedNews;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<String> newsArrayList = new ArrayList<>();
    private ArrayList<News> newsArrayList1 = new ArrayList<>();
    private ArrayList<NewsDetails> newsArrayList2 = new ArrayList<>();

    public int networkCheck(){
        ConnectivityManager connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("News Cannot be updated without an Internet Connection");
            builder.setTitle("No Internet Connection");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return 0;
        }
        else {return 1;}
    }

    private void Item(int pos) {
        setTitle(newsArrayList.get(pos));
        updatedNews = newsArrayList1.get(pos).getId();
        Intent i = new Intent(MainActivity.this, Services.class);
        i.putExtra(MainActivity.SERVICE_DATA2, newsArrayList1.get(pos).getId());
        startService(i);
        drawerLayout.closeDrawer(listView);
    }

    public void updateNewsData(ArrayList<NewsDetails> detailsArrayList) {
        DrawerLayout layout =(DrawerLayout)findViewById(R.id.layout);
        layout.setBackgroundResource(0);
        if(detailsArrayList == null)return;
        if(detailsArrayList.size()>0){
            newsArrayList2.clear();
            newsArrayList2.addAll(detailsArrayList);
            for (int i = 0; i < adapter.getCount(); i++)
                adapter.notifyChangeInPosition(i);
            fragmentList.clear();
            for (int i = 0; i < newsArrayList2.size(); i++) {
                String str = new String(newsArrayList2.get(i)
                        .getNewsData().substring(0,
                                newsArrayList2.get(i)
                                        .getNewsData()
                                        .length()-1));
                str = str+",\"Count\":\""+(i+1)+" of "+ newsArrayList2.size()+"\"}";
                fragmentList.add(Fragments.newFragments(str,this));
            }
            adapter.notifyDataSetChanged();
            pager.setCurrentItem(0);
        }
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentArrayList = new ArrayList<Fragment>();
        return fragmentArrayList;
    }

    private class mAdapter extends FragmentPagerAdapter {
        private long anInt = 0;
        public mAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getItemPosition(Object o) {
            return POSITION_NONE;
        }
        @Override
        public long getItemId(int pos) {
            return anInt + pos;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void notifyChangeInPosition(int n) {
            anInt += getCount() + n;
        }
    }

    class mReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context con, Intent i) {
            switch (i.getAction()) {
                case SAMPLE_BROADCAST_TYPE_A:
                    String s = "";
                    if (i.hasExtra(SERVICE_DATA2))
                        s = i.getStringExtra(SERVICE_DATA2);
                    updateNewsData(parseValue2(s));
                    break;

                case SAMPLE_BROADCAST_TYPE_B:
                    String s1 = "";
                    if (i.hasExtra(SERVICE_DATA1))
                        s1 = i.getStringExtra(SERVICE_DATA1);
                    updateNews(parseValue(s1));
                    break;
            }
        }
    }

    ArrayList<News> parseValue(String string) {
        if(string==null)
            return null;
        ArrayList<News> newsArrayList = new ArrayList<>();
        newsArrayList.clear();
        try {
            JSONObject jsonObject1 = new JSONObject(string);
            JSONArray jsonObject2 = jsonObject1.getJSONArray("sources");
            int i = 0;
            while (i < jsonObject2.length()) {
                JSONObject newsObject = jsonObject2.getJSONObject(i);
                i++;
                String newsId = newsObject.getString("id");
                String newsName = newsObject.getString("name");
                String newsUrl = newsObject.getString("url");
                String newsCategories = newsObject.getString("category");
                newsArrayList.add(new News(newsId, newsName,newsUrl,newsCategories));
            }
            return newsArrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    ArrayList<NewsDetails> parseValue2(String string) {
        if(string==null)
            return null;
        ArrayList<NewsDetails> newsDetailsArrayList = new ArrayList<>();
        newsDetailsArrayList.clear();
        try {
            JSONObject jsonObject1 = new JSONObject(string);
            JSONArray jsonObject2 = jsonObject1.getJSONArray("articles");
            int i = 0;
            while (i < jsonObject2.length()) {
                JSONObject newsObject = jsonObject2.getJSONObject(i);
                i++;
                String newsAuthor = newsObject.getString("author");
                String newsTitle = newsObject.getString("title");
                String newsDescription = newsObject.getString("description");
                String ImageUrl = newsObject.getString("urlToImage");
                String newsDate = newsObject.getString("publishedAt");
                String newsData = newsObject.toString();
                newsDetailsArrayList.add(new NewsDetails(newsAuthor, newsTitle,newsDescription,ImageUrl,newsDate,newsData));
            }
            return newsDetailsArrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        updatedNews ="";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(networkCheck()==1) {
            Intent i = new Intent(MainActivity.this, Services.class);
            i.putExtra(MainActivity.SERVICE_DATA2, "all");
            startService(i);
            mReceiver = new mReceiver();
            IntentFilter intentFilter = new IntentFilter(SAMPLE_BROADCAST_TYPE_A);
            registerReceiver(mReceiver, intentFilter);
            IntentFilter intentFilter1 = new IntentFilter(SAMPLE_BROADCAST_TYPE_B);
            registerReceiver(mReceiver, intentFilter1);
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.layout);
        listView = (ListView) findViewById(R.id.left);
        listView.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_file, newsArrayList));
        listView.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> listener, View v, int position, long id) {
                        Item(position);
                    }
                }
        );
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout,R.string.drawer_open,R.string.drawer_close
        );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        fragmentList = getFragmentList();
        adapter = new mAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.view1);
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        Intent i = new Intent(MainActivity.this, Services.class);
        i.putExtra(MainActivity.SERVICE_DATA2, item.toString());
        startService(i);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        drawerToggle.onConfigurationChanged(configuration);
    }

    public void updateNews(ArrayList<News> newsArrayList3) {
        if(newsArrayList3==null)
            return;
        newsArrayList1.clear();
        if(newsArrayList3.size()>0) {
            newsArrayList.clear();
            newsArrayList1.addAll(newsArrayList3);
            ArrayList<String> strList = new ArrayList<>();
            strList.clear();
            for (int i = 0; i < newsArrayList1.size(); i++) {
                newsArrayList.add(newsArrayList1.get(i).getName());
                int i1=0,i2=1;
                while(i1<strList.size())
                {if(newsArrayList1.get(i).getCategory().equals(strList.get(i1))) {
                        i2 = 0;
                        break;}
                    i1++;}
                if(i2==1)
                {strList.add(newsArrayList1.get(i).getCategory());}}
            listView.setAdapter(new ArrayAdapter<>(this,
                    R.layout.drawer_file, newsArrayList));
            listView.setOnItemClickListener(
                    new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Item(position);
                        }
                    });}}

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        Intent i = new Intent(MainActivity.this, Services.class);
        stopService(i);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString("Update", updatedNews);
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updatedNews =savedInstanceState.getString("Update");
        if(!updatedNews.equals("")) {
            Intent i = new Intent(MainActivity.this, Services.class);
            i.putExtra(MainActivity.SERVICE_DATA2, updatedNews);
            startService(i);
        }
    }
}

