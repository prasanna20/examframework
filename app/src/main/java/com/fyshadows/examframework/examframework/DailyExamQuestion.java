package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyxqsg.bsyduo229750.AdConfig;
import com.yyxqsg.bsyduo229750.Main;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Adapter.DailyExamAdapter;
import ExamFramework_AsyncTask.AsyncUpdateNewValues;

public class DailyExamQuestion  extends ListActivity {
    private ListView listView;
    DailyExamAdapter adapter;
    private List<String> ExamData;
    Exam_database db = new Exam_database(this);
    List<String> list = new ArrayList<String>();
    int scrolly = 0;
    int first = 0;
    TextView txtNoExam;
    private Main main;
    private com.yyxqsg.bsyduo229750.AdView adView;
    ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daily_exam_question);


        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.actionbar_dailyexam);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        txtNoExam=(TextView) this.findViewById(R.id.txtNoExam);

        refresh=(ImageView) this.findViewById(R.id.refresh);
        refresh = (ImageView) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masterdetails.isOnline(DailyExamQuestion.this)) {
                    Toast.makeText(getBaseContext(), "Refreshing", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new AsyncUpdateNewValues(DailyExamQuestion.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    else
                    {
                        new AsyncUpdateNewValues(DailyExamQuestion.this).execute();
                    }

                } else {
                    Toast.makeText(getBaseContext(), "Please Connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Advertisement Start
        AdConfig.setAppId(280371);  //setting appid.
        AdConfig.setApiKey("1435945311229750247"); //setting apikey
        // AdConfig.setTestMode(true);
        //AdConfig.setAdListener(adListener);  //setting global Ad listener.
        AdConfig.setCachingEnabled(false); //Enabling SmartWall ad caching.
        AdConfig.setPlacementId(0); //pass the placement id.

        //Initialize Airpush
        main=new Main(this);

        //for calling Smartwall ad
        //main.startInterstitialAd(AdConfig.AdType.smartwall);

        //for calling banner 360
       // main.start360BannerAd(this);

        //for calling Smartwall ad
        adView=(com.yyxqsg.bsyduo229750.AdView) findViewById(R.id.myAdView);
        adView.setBannerType(com.yyxqsg.bsyduo229750.AdView.BANNER_TYPE_IN_APP_AD);
        adView.setBannerAnimation(com.yyxqsg.bsyduo229750.AdView.ANIMATION_TYPE_FADE);
        adView.showMRinInApp(false);
        adView.loadAd();
        //Advertisement End

        ListView myListView = (ListView) findViewById(android.R.id.list);
        list = db.getDailyExamDate();
        int index = 0;
        int top = 0;

        if (!list.isEmpty()) {
            adapter = new DailyExamAdapter(this, list);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (first == 0) {
                myListView.setSelection(scrolly);
            }

            if (first > 0) {
                myListView.setSelectionFromTop(index, top);
            }

            txtNoExam.setVisibility(View.INVISIBLE);

        }
        else
        {
            txtNoExam.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_daily_exam_question, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                main.startInterstitialAd(AdConfig.AdType.smartwall);
                Intent i = new Intent(DailyExamQuestion.this, HomeActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class AsyncSendChatMessage extends AsyncTask<String, Void, String> {



        @Override
        protected void onPreExecute() {

        }


        @Override
        protected String doInBackground(String... Values) {
            try {
                adView=(com.yyxqsg.bsyduo229750.AdView) findViewById(R.id.myAdView);
                adView.setBannerType(com.yyxqsg.bsyduo229750.AdView.BANNER_TYPE_IN_APP_AD);
                adView.setBannerAnimation(com.yyxqsg.bsyduo229750.AdView.ANIMATION_TYPE_FADE);
                adView.showMRinInApp(false);
                adView.loadAd();

            } catch (Exception e) {

                Log.i("Analysis activity", "Error");
            }
            return null;
        }

    }
}
