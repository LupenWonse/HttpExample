package com.example.ahmet.httpexample;

import android.os.AsyncTask;

import java.util.LinkedList;

/**
 * Created by Ahmet on 19.09.2016.
 */
public class GetTweetsAsyncTask extends AsyncTask<String, Void, LinkedList<String>> {
IData mainActivity;


    public GetTweetsAsyncTask(IData activity) {
        this.mainActivity = activity;
    }


    @Override
    protected LinkedList<String> doInBackground(String... params) {
        LinkedList<String> tweets = new LinkedList<>();
        tweets.add("Tweet 0");
        tweets.add("Tweet 1");
        tweets.add("Tweet 1");
        return tweets;
    }

    @Override
    protected void onPostExecute(LinkedList<String> strings) {
        super.onPostExecute(strings);
        mainActivity.setupData(strings);
    }

    public interface IData{
        void setupData(LinkedList<String> data);
    }
}
