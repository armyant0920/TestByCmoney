package com.example.testbycmoney.task;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class CommonTask extends AsyncTask<String,Integer,String> {
    private final static String TAG ="CommonTask";
    private String url;

    public CommonTask(String url){
        this.url = url;
    }
    @Override
    protected String doInBackground(String... strings){
        return getRemoteData();
    }

    private String getRemoteData(){
        HttpURLConnection connection = null;
        StringBuilder inStr =new StringBuilder();

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.setChunkedStreamingMode(0);
            connection.setRequestProperty("charset", "UTF-8");
            Log.d(TAG, "URL:" + url);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    inStr.append(line);
                }
            } else {
                Log.d(TAG, "response code:" + responseCode);
            }
        }   catch (SocketTimeoutException e){
            Log.e(TAG,"SocketTimeoutException:"+e.toString());
        }   catch (IOException e){
            Log.e(TAG,e.toString());
        }   finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        Log.d(TAG, "input: " + inStr);
        return inStr.toString();
    }



}
