package com.fyshadows.examframework.examframework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.yyxqsg.bsyduo229750.AdConfig;
import com.yyxqsg.bsyduo229750.AdListener;
import com.yyxqsg.bsyduo229750.AdView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_AsyncTask.AsyncUpdateNewValues;


public class Configuringactivity extends ActionBarActivity implements AdListener {

    //Declaration
    SharedPreferences prefs;
    TextView welcome;
    String regId = "";
    Thread logoTimer;
    int check = 1;
    JSONParser jsonParser = new JSONParser();

    Exam_database db = new Exam_database(Configuringactivity.this);

    private com.yyxqsg.bsyduo229750.AdView adView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuringactivity);

       
//advertisement start

        AdConfig.setAppId(280371);  //setting appid.
        AdConfig.setApiKey("1435945311229750247"); //setting apikey
        AdConfig.setCachingEnabled(true); //Enabling SmartWall ad caching.
        AdConfig.setPlacementId(0); //pass the placement id.

       //for calling Smartwall ad
       //main.startInterstitialAd(AdConfig.AdType.smartwall);

        adView=(AdView) findViewById(R.id.myAdView);
        adView.setBannerType(AdView.BANNER_TYPE_IN_APP_AD);
        adView.setBannerAnimation(AdView.ANIMATION_TYPE_FADE);
        AdView.setAdListener(this);
        //adView.setNewAdListener(adListener); //for passing a new listener for inline banner ads.
        adView.loadAd();

///advertisement End
        welcome = (TextView) findViewById(R.id.Welcomemessage);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        logoTimer = new Thread() {
            public void run() {
                try {
                    int logoTimer = 0;
                    while (logoTimer < 1200) {
                        sleep(10);
                        logoTimer = logoTimer + 300;
                        if (logoTimer >= 1200 && check == 1) {
                            logoTimer = logoTimer - 300;
                        }
                    }

                    homeactivity();


                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };


        if (!prefs.getBoolean("Firsttimeactivity", false)) {
            // This block is used for first time activity

           db.InsertTimer(36);


            welcome.setText(R.string.welcome);

            if (masterdetails.isOnline(this)) {
                Log.i("Exam", "starting async task");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new asynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                else
                {
                    new asynctask().execute();
                }

            } else {
                Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_LONG);
            }

        } else {

            // This block is used for the already installed activity
            welcome.setText(R.string.welcomeback);
            if (masterdetails.isOnline(this)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                   // new asynccheckfornewques().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new AsyncUpdateNewValues(Configuringactivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                else
                {
                    //new asynccheckfornewques().execute();
                    new AsyncUpdateNewValues(Configuringactivity.this).execute();
                }

            } else {
                Toast.makeText(this, "Please connect to Internet to receive updates", Toast.LENGTH_LONG);
            }
            check = 2;


        }

        //Start : Get ChatRoom
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncUpdateChatRoom(Configuringactivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            new AsyncUpdateChatRoom(Configuringactivity.this).execute();
        }
        //End : Chat Room

        //Start : Get Chat Message
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncUpdateChatMessage(Configuringactivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            new AsyncUpdateChatMessage(Configuringactivity.this).execute();
        }
        //End : Chat Message
*/


        logoTimer.start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuringactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    // user defined functions
    public void homeactivity() {
        Intent i = new Intent(Configuringactivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void gcmregistration() {
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        regId = GCMRegistrar.getRegistrationId(this);
        Log.i("Exam", "Device registered: regId = " + regId);
        // Check if regid already presents
        if (regId.equals("")) {
            Log.i("Exam", "Device registered: regId = registering");
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, masterdetails.sender_id);
        }
    }

    @Override
    public void onError(ErrorType errorType, String s) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdLoaded() {

    }

    @Override
    public void onAdExpanded() {

    }

    @Override
    public void onAdClicked() {

    }

    @Override
    public void onAdClosed() {

    }

    @Override
    public void onAdCached(AdConfig.AdType adType) {

    }

    public class asynctask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            try {

                Log.i("Prasanna Exam", "into async task");
                gcmregistration();

                if (!prefs.getBoolean("Firsttimeactivity", false)) {
                    // This block is used for first time activity
                    Log.i("Prasanna Exam", "Delete master table");
                    db.deletemastertable();

                }

                // To get question details
                int success;
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                JSONObject json = jsonParser.makeHttpRequest(
                        masterdetails.getMasterQuestionNew, "GET", params);
                Log.i("Prasanna Exam", "Got Json");
                if (json != null) {
                    if (json.length() > 0) {
                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {
                            Log.i("Prasanna Exam", "Sucess");
                            // successfully received details
                            JSONArray QuestionObj = json.getJSONArray("MasterQuestion"); // JSON
                            // Array
                            for (int i = 0; i < QuestionObj.length(); i++) {

                                int QuestionNo = 0;
                                String Question = "";
                                String Choice1 = "";
                                String Choice2 = "";
                                String Choice3 = "";
                                String Choice4 = "";
                                String Choice5 = "";
                                int Correct_Ans = 0;
                                int Category = 0;

                                JSONObject md = QuestionObj.getJSONObject(i);

                                QuestionNo = md.getInt("QuestionNo");
                                Question = md.getString("Question");
                                Choice1 = md.getString("Choice1");
                                Choice2 = md.getString("Choice2");
                                Choice3 = md.getString("Choice3");
                                Choice4 = md.getString("Choice4");
                                Choice5 = md.getString("Choice5");
                                Correct_Ans = md.getInt("Correct_Ans");
                                Category = md.getInt("Category");

                                if (Question.trim() == "") {
                                    Question = "NA";
                                }
                                if (Choice1.trim() == "") {
                                    Choice1 = "NA";
                                }
                                if (Choice2.trim() == "") {
                                    Choice2 = "NA";
                                }
                                if (Choice3.trim() == "") {
                                    Choice3 = "NA";
                                }
                                if (Choice4.trim() == "") {
                                    Choice4 = "NA";
                                }
                                if (Choice5.trim() == "") {
                                    Choice5 = "NA";
                                }


                                db.InsertQuestionDetails(QuestionNo, Question, Choice1, Choice2, Choice3, Choice4, Choice5, Correct_Ans, Category);

                                //End of getting question details
                            }
                        }
                    }
                }

//Start :  Get Daily Question-----------------------------------------------------------------------
                params.clear();
                params.add(new BasicNameValuePair("lastquestionnumber", String.valueOf("0")));

                json = jsonParser.makeHttpRequest(
                        masterdetails.getDailyTestQuestions, "GET", params);
                if (json.length() > 0) {
                    // json success tag
                    success = json.getInt("success");
                    if (success == 1) {
                        // successfully received product details
                        JSONArray QuestionObj = json.getJSONArray("MasterDailyQuestion"); // JSON
                        // Array
                        for (int i = 0; i < QuestionObj.length(); i++) {
                         JSONObject md = QuestionObj.getJSONObject(i);

                         db.InsertDailyQuestionDetails(md.getInt("Id"), md.getString("ExamDate"), md.getInt("QuestionNo"),md.getString("QuestionText"), md.getString("Choice1"), md.getString("Choice2"),md.getString("Choice3"),md.getString("Choice4"),md.getString("Choice5") ,md.getInt("Answer"),md.getString("Category") );

                            //End of getting question details
                        }
                        check = 2;
                    }
                }
//End  : Get Daily Question--------------------------------------------------------------------------

                //Start :  Get Daily Article details-----------------------------------------------------------------------
                params.clear();
                params.add(new BasicNameValuePair("lastArticleNumber", String.valueOf("0")));

                json = jsonParser.makeHttpRequest(
                        masterdetails.getDailyArticle, "GET", params);
                if (json.length() > 0) {
                    // json success tag
                    success = json.getInt("success");
                    if (success == 1) {
                        // successfully received product details
                        JSONArray QuestionObj = json.getJSONArray("MasterDailyArticle"); // JSON
                        // Array
                        for (int i = 0; i < QuestionObj.length(); i++) {
                            JSONObject md = QuestionObj.getJSONObject(i);

                            db.InsertDailyArticle(md.getInt("ArticleNo"), md.getString("ArticleDate"), md.getString("Topic"), md.getString("ArticleDesc") );

                            //End of getting Article details
                        }
                    }
                }
//End  : Get Daily Question--------------------------------------------------------------------------

                //Start :  Get Monthly Question-----------------------------------------------------------------------
                params.clear();
                params.add(new BasicNameValuePair("lastquestionnumber", String.valueOf("0")));

                json = jsonParser.makeHttpRequest(
                        masterdetails.getMonthlyTestQuestions, "GET", params);
                if (json.length() > 0) {
                    // json success tag
                    success = json.getInt("success");
                    if (success == 1) {
                        // successfully received product details
                        JSONArray QuestionObj = json.getJSONArray("MasterDailyQuestion"); // JSON
                        // Array
                        for (int i = 0; i < QuestionObj.length(); i++) {
                            JSONObject md = QuestionObj.getJSONObject(i);

                            db.InsertMonthlyQuestionDetails(md.getInt("Id"), md.getString("Month"), md.getInt("QuestionNo"),md.getString("QuestionText"), md.getString("Choice1"), md.getString("Choice2"),md.getString("Choice3"),md.getString("Choice4"),md.getString("Choice5") ,md.getInt("Answer"),md.getString("Category") );

                            //End of getting question details
                        }
                    }
                }
//End  : Get Monthly Question--------------------------------------------------------------------------


            } catch (Exception e) {
                check = 1;
               Log.i("Configuring activity","Error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Configuringactivity.this, "In post execute", Toast.LENGTH_LONG);
            //once the configuration is done set it as true to indicate this is not a first time activity
           // Toast.makeText(Configuringactivity.this, "i am in first block", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("Firsttimeactivity", true);
            editor.commit();

            //setting the intial timer to 36


            check = 2;


        }
    }


    //to check for updates in questions
    public class asynccheckfornewques extends AsyncTask<String, Void, String> {
        JSONParser jsonParser = new JSONParser();
        int MainDBLastQuestion;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            try {

                // To get question details
                int success;
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                JSONObject json = jsonParser.makeHttpRequest(
                        masterdetails.checkForupdate, "GET", params);
                if(json != null) {
                    if (json.length() > 0) {
                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {

                            // successfully received product details
                            JSONArray QuestionObj = json.getJSONArray("LastQuestion"); // JSON
                            // Array
                            JSONObject md = QuestionObj.getJSONObject(0);


                            MainDBLastQuestion = md.getInt("maxQuestionNo");
                            if (MainDBLastQuestion > db.getmaxquestionnumber()) {
                                try {

                                    // To get question details

                                    params.clear();
                                    params.add(new BasicNameValuePair("lastquestionnumber", String.valueOf(db.getmaxquestionnumber())));

                                    json = jsonParser.makeHttpRequest(
                                            masterdetails.getNewQuestions, "GET", params);
                                    if (json.length() > 0) {
                                        // json success tag
                                        success = json.getInt("success");
                                        if (success == 1) {
                                            // successfully received product details
                                            QuestionObj = json.getJSONArray("MasterQuestion"); // JSON
                                            // Array
                                            for (int i = 0; i < QuestionObj.length(); i++) {

                                                int QuestionNo = 0;
                                                String Question = "";
                                                String Choice1 = "";
                                                String Choice2 = "";
                                                String Choice3 = "";
                                                String Choice4 = "";
                                                String Choice5 = "";
                                                int Correct_Ans = 0;
                                                int Category = 0;

                                                md = QuestionObj.getJSONObject(i);

                                                QuestionNo = md.getInt("QuestionNo");
                                                Question = md.getString("Question");
                                                Choice1 = md.getString("Choice1");
                                                Choice2 = md.getString("Choice2");
                                                Choice3 = md.getString("Choice3");
                                                Choice4 = md.getString("Choice4");
                                                Choice5 = md.getString("Choice5");
                                                Correct_Ans = md.getInt("Correct_Ans");
                                                Category = md.getInt("Category");

                                                if (Question.trim() == "") {
                                                    Question = "NA";
                                                }
                                                if (Choice1.trim() == "") {
                                                    Choice1 = "NA";
                                                }
                                                if (Choice2.trim() == "") {
                                                    Choice2 = "NA";
                                                }
                                                if (Choice3.trim() == "") {
                                                    Choice3 = "NA";
                                                }
                                                if (Choice4.trim() == "") {
                                                    Choice4 = "NA";
                                                }
                                                if (Choice5.trim() == "") {
                                                    Choice5 = "NA";
                                                }

                                                db.InsertQuestionDetails(QuestionNo, Question, Choice1, Choice2, Choice3, Choice4, Choice5, Correct_Ans, Category);

                                                //End of getting question details
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.i("exam", "we have not got any new set of question");
                                }
                                //download new question to database ends
                            } else {
                                Log.i("exam", "we have not got any new set of question");
                                //download new questions to database start

                            }

                            //End of getting question detail
                            // //Start :  Get Daily Question-----------------------------------------------------------------------
                            params.clear();
                            params.add(new BasicNameValuePair("lastquestionnumber", String.valueOf(db.getMaxDailyQuestionNumber())));

                            json = jsonParser.makeHttpRequest(
                                    masterdetails.getDailyTestQuestions, "GET", params);
                            if (json.length() > 0) {
                                // json success tag
                                success = json.getInt("success");
                                if (success == 1) {

                                    // successfully received product details
                                    QuestionObj = json.getJSONArray("MasterDailyQuestion"); // JSON
                                    // Array
                                    for (int i = 0; i < QuestionObj.length(); i++) {
                                        md = QuestionObj.getJSONObject(i);

                                        db.InsertDailyQuestionDetails(md.getInt("Id"), md.getString("ExamDate"), md.getInt("QuestionNo"), md.getString("QuestionText"), md.getString("Choice1"), md.getString("Choice2"), md.getString("Choice3"), md.getString("Choice4"), md.getString("Choice5"), md.getInt("Answer"), md.getString("Category"));

                                        //End of getting question details
                                    }
                                }
                            }
//End  : Get Daily Question--------------------------------------------------------------------------

                            //Start :  Get Daily Article details-----------------------------------------------------------------------
                            params.clear();
                            params.add(new BasicNameValuePair("lastArticleNumber", String.valueOf(db.getMaxDailyArticle())));

                            json = jsonParser.makeHttpRequest(
                                    masterdetails.getDailyArticle, "GET", params);
                            if (json.length() > 0) {
                                // json success tag
                                success = json.getInt("success");
                                if (success == 1) {
                                    // successfully received product details
                                    QuestionObj = json.getJSONArray("MasterDailyArticle"); // JSON
                                    // Array
                                    for (int i = 0; i < QuestionObj.length(); i++) {
                                        md = QuestionObj.getJSONObject(i);

                                        db.InsertDailyArticle(md.getInt("ArticleNo"), md.getString("ArticleDate"), md.getString("Topic"), md.getString("ArticleDesc"));

                                        //End of getting Article details
                                    }
                                }
                            }
//End  : Get Daily Question--------------------------------------------------------------------------s

                            // //Start :  Get Monthly Question-----------------------------------------------------------------------
                            params.clear();
                            params.add(new BasicNameValuePair("lastquestionnumber", String.valueOf(db.getMaxMonthlyQuestionNumber())));

                            json = jsonParser.makeHttpRequest(
                                    masterdetails.getMonthlyTestQuestions, "GET", params);
                            Log.i("maximum Exam monthly", String.valueOf(db.getMaxMonthlyQuestionNumber()));
                            if (json.length() > 0) {
                                // json success tag
                                success = json.getInt("success");
                                if (success == 1) {
                                    Log.i("Exam", "Check_suc");
                                    // successfully received product details
                                    QuestionObj = json.getJSONArray("MasterDailyQuestion"); // JSON
                                    // Array
                                    for (int i = 0; i < QuestionObj.length(); i++) {
                                        md = QuestionObj.getJSONObject(i);

                                        db.InsertMonthlyQuestionDetails(md.getInt("Id"), md.getString("Month"), md.getInt("QuestionNo"), md.getString("QuestionText"), md.getString("Choice1"), md.getString("Choice2"), md.getString("Choice3"), md.getString("Choice4"), md.getString("Choice5"), md.getInt("Answer"), md.getString("Category"));

                                        //End of getting question details
                                    }
                                }
                            }
//End  : Get Daily Question--------------------------------------------------------------------------


                        }
                    }
                }
            } catch (Exception e) {

              Log.i("error","eror in async");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }

}
