package com.example.ahmet.httpexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements GetTweetsAsyncTask.IData{
private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);


        new GetTweetsAsyncTask(this).execute("some URL");


        Button checkConnectionButton = (Button) findViewById(R.id.button);
        checkConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedOnline()) {
                    //new GetData().execute("http://rss.cnn.com/rss/cnn_tech.rss");
                    RequestParams requestParams = new RequestParams("GET", "http://liisp.uncc.edu/~mshehab/api/json/persons-v1.json");
                    requestParams.addParam("Param1","Parameter");
                    requestParams.addParam("Param2","Parameter is very nice");
                    new GetDataWithParams().execute(requestParams);
                }
            }

        });

        findViewById(R.id.getImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedOnline()){
                    new GetImage().execute("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
                }
            }
        });
    }


    public void setupData(LinkedList<String> data){
        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,android.R.id.text1, data);
        listView.setAdapter(adapter);
    }
    private class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Log.d("test", result);
            } else {
                Log.d("test", "No Data received");
            }
        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(params[0]);
                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    InputStream inputStream = connection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    return sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private class GetDataWithParams extends AsyncTask<RequestParams, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    JSONArray personsJSONArray = jsonObject.getJSONArray("persons");

                    for (int i=0; i<personsJSONArray.length(); i++){
                        JSONObject personsJSONObject = personsJSONArray.getJSONObject(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("test", jsonObject.toString());
            } else {
                Log.d("test", "No Data received");
            }
        }

        @Override
        protected String doInBackground(RequestParams... params) {
            BufferedReader bufferedReader;
            HttpURLConnection connection;
            InputStream inputStream;

            try {
                connection = params[0].setupConnection();
                inputStream = connection.getInputStream();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
              imageView.setImageBitmap(result);
            } else {
              Log.d("test", "No Image received");
            }
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(params[0]);
                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    Bitmap image = BitmapFactory.decodeStream(connection.getInputStream());

                    //InputStream inputStream = connection.getInputStream();
                    //bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    //StringBuilder sb = new StringBuilder();
                    //String line;
                    //while ((line = bufferedReader.readLine()) != null) {
                    //    sb.append(line + "\n");
                    //}
                    return image;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private boolean isConnectedOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.isConnected();
        }
        return false;
    }
}
