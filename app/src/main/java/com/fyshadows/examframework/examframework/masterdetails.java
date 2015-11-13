package com.fyshadows.examframework.examframework;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanna on 16-03-2015.
 */
public class masterdetails {

    private Context context;
    Exam_database db;
    int Code=0;

    public masterdetails(Context context){
        this.context=context.getApplicationContext();
         db=new Exam_database(context);
    }

    JSONParser jsonParser = new JSONParser();
    static String sender_id="830753459977";
    static int timer=36;
    static String registeruser ="http://collegemateapp.com/ExamFrameWork/GCMregistration.php";
    static String getMasterQuestion="http://collegemateapp.com/ExamFrameWork/getExamQuestions.php";
    static String checkForupdate="http://collegemateapp.com/ExamFrameWork/CheckForNewQuestions.php";
    static String getNewQuestions="http://collegemateapp.com/ExamFrameWork/getNewQuestions.php";
    static String getDailyTestQuestions="http://collegemateapp.com/ExamFrameWork/getDailyTestQuestions.php";
    static String getDailyArticle="http://collegemateapp.com/ExamFrameWork/getDailyArticle.php";
    public static String getnews="http://collegemateapp.com/ExamFrameWork/getnews.php";
    public static String InsertAnalysis="http://collegemateapp.com/ExamFrameWork/IbpsAnalysis.php";

    /*
Screen	                  Code
Home screen	               1
On click of daily exam	   2
On click of score	       3
On click of news	       4
On click of notification   5
On click of article	       6
on click of Exam date	   7
     */

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    //Start :- Insert into analysis
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public  void insertAnalysis(Context context,int code)
    {
        if(isOnline(context))
        {
            Code=code;
           new insertAnalysisAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public class insertAnalysisAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected String doInBackground(String... urls) {
            try {


                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.clear();
                params.add(new BasicNameValuePair("email",db.GetEmailDetails(context)));
                params.add(new BasicNameValuePair("code", String.valueOf(Code)));

                JSONObject  json = jsonParser.makeHttpRequest(
                        masterdetails.InsertAnalysis, "GET", params);

            } catch (Exception e) {

                Log.i("Analysis activity","Error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
    //End :- Insert into analysis
}
