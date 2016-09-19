package com.example.ahmet.httpexample;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Ahmet on 19.09.2016.
 */
public class RequestParams {
    String method, baseURL;
    HashMap<String , String> params = new HashMap<>();

    public RequestParams(String method, String baseURL) {
        this.method = method;
        this.baseURL = baseURL;
    }

    public void addParam(String key, String value){
        params.put(key,value);
    }

    public String getEncodedParams(){
        StringBuilder stringBuilder = new StringBuilder();

        for (String key: params.keySet()) {

            try {
                String value = URLEncoder.encode(params.get(key), "UTF-8");
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("&");
                }
                    stringBuilder.append(key+"="+value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public String getEncodedURL(){
        return this.baseURL + "?" + getEncodedParams();
    }

    public HttpURLConnection setupConnection() throws IOException {
        if (method.equals("GET")){
            URL url = new URL(getEncodedURL());
            HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            return connection;
        }
        else // POST
        {
            URL url = new URL(this.baseURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(getEncodedParams());
            writer.flush();
            return connection;
        }
    }



}
