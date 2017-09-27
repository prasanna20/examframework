package com.fyshadows.examframework.examframework;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanna on 16-03-2015.
 */
public class masterdetails {

    private Context context;
    Exam_database db;
    int Code = 0;


    public masterdetails(Context context) {
        this.context = context.getApplicationContext();
        db = new Exam_database(context);
    }

    public static String ServerAddress ="http://collegemateapp.com/ExamFrameWork/";

    JSONParser jsonParser = new JSONParser();
    public static String sender_id = "830753459977";
    public static int timer = 36;
    public static String registeruser = ServerAddress + "GCMregistration.php";
    public static String getMasterQuestion = ServerAddress + "getExamQuestions.php";
    public static String getMasterQuestionNew = ServerAddress + "getExamQuestionsNew.php";
    public static String checkForupdate = ServerAddress + "CheckForNewQuestions.php";
    public static String getNewQuestions = ServerAddress + "getNewQuestions.php";
    public static String getDailyTestQuestions = ServerAddress + "getDailyTestQuestions.php";
    public static String getDailyArticle = ServerAddress + "getDailyArticle.php";
    public static String getnews = ServerAddress + "getnews.php";
    public static String InsertAnalysis = ServerAddress + "IbpsAnalysis.php";
    public static String UpdateUserName = ServerAddress + "UpdateUserName.php";
    public static String AddChatRoom = ServerAddress + "AddChatRoom.php";
    public static String getChatRoom = ServerAddress + "getChatRoom.php";
    public static String TriggerUpdate = ServerAddress + "TriggerUpdate.php";
    public static String TriggerDeleteRoom = ServerAddress + "TriggerDeleteRoom.php";
    public static String AddChatMessage = ServerAddress + "AddChatMessage.php";
    public static String getChatMessage = ServerAddress + "getChatMessage.php";
    public static String sendChatMessage = ServerAddress + "SendChatNotification.php";
    public static String getMonthlyTestQuestions = ServerAddress + "getMonthlyTestQuestions.php";
    //http://collegemateapp.com/ExamFrameWork/SendChatNotification.php?RoomId=15&ChatMessage=%22hinow%22&Email=%22watitis%22&UserName=%22that%22

    /*
Screen	                  Code
Home screen	               1
On click of daily exam	   2
On click of score	       3
On click of news	       4
On click of notification   5
On click of article	       6
on click of Exam date	   7
update values through Notification 8
on click of chatroom 9
on DeleteRoom 10
on chatWindow 11
     */

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //Start :- Insert into analysis
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void insertAnalysis(Context context, int code) {
        try {
            if (isOnline(context)) {
                Code = code;
                new insertAnalysisAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {

            Log.i("Analysis activity", "Error");
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
                params.add(new BasicNameValuePair("email", db.GetEmailDetails(context)));
                params.add(new BasicNameValuePair("code", String.valueOf(Code)));

                JSONObject json = jsonParser.makeHttpRequest(
                        masterdetails.InsertAnalysis, "GET", params);

            } catch (Exception e) {

                Log.i("Analysis activity", "Error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
    //End :- Insert into analysis

    //Formating Datetime
    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        java.util.Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            try {
                parsed = df_input.parse(inputDate);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            outputDate = df_output.format(parsed);
            Log.i("date", outputDate);

        } catch (ParseException e) {
            Log.i("date", "ParseException - dateFormat");
        }

        return outputDate;

    }
}
