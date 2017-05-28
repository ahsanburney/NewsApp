package com.example.ahsan.newsgateway;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Services extends Service {

    private static final String TAG = "Services";
    private String[] newsChannel = new String[10];
    private boolean running = true;

    public void sendValue1(String s) {
        Intent i = new Intent();
        i.setAction(MainActivity.SAMPLE_BROADCAST_TYPE_B);
        i.putExtra(MainActivity.SERVICE_DATA1, s);
        sendBroadcast(i);
    }
    public void sendValue2(String s) {
        Intent i = new Intent();
        i.setAction(MainActivity.SAMPLE_BROADCAST_TYPE_A);
        i.putExtra(MainActivity.SERVICE_DATA2, s);
        sendBroadcast(i);
    }

    public Services() {
        newsChannel[0] = "all";
        newsChannel[1] ="science-and-nature";
        newsChannel[2] ="gaming";
        newsChannel[3] ="technology";
        newsChannel[4] ="business";
        newsChannel[5] ="politics";
        newsChannel[6] ="general";
        newsChannel[7] ="entertainment";
        newsChannel[8] ="sport";
        newsChannel[9] ="music";
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent i, int flag, int firstId) {

        String lastValue = "";
        if (i.hasExtra(MainActivity.SERVICE_DATA2))
            lastValue = i.getStringExtra(MainActivity.SERVICE_DATA2);

        int point=0,j;
        for(j=0;j<10;j++){
            if(lastValue.equals(newsChannel[j])){
                if(j==0)
                    lastValue="";
                point=1;
                break;
            }
        }
        if(point==1) {
            final String value = lastValue;
            final AsyncTask1 task = new AsyncTask1(this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    task.execute(value);
                }
            }).start();
        }
        else {
            final String value = lastValue;
            final AsyncTask2 loaderTask = new AsyncTask2(this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loaderTask.execute(value);
                }
            }).start();
        }
        return Service.START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }
}
