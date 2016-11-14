package com.fyshadows.examframework.examframework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.yyxqsg.bsyduo229750.AdConfig;
import com.yyxqsg.bsyduo229750.Main;


public class scoreactivity extends ActionBarActivity {

    Exam_database db;
    TextView score;
    TextView average;
    TextView skippedCount;
    TextView quantitativescore;
    TextView English;
    TextView Reasoning;
    TextView General;
    TextView Computer;
    private Main main; //Declare here
    private com.yyxqsg.bsyduo229750.AdView adView;
    public int FromScreen = 0;
    public String ExamDate="";
    public String Subject_selection = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreactivity);

        //Start : Insert Analysis
        masterdetails masterdetails=new masterdetails(this);
        masterdetails.insertAnalysis(this, 3);
        //End : Insert Analysis

        //Advertisement Start
        AdConfig.setAppId(280371);  //setting appid.
        AdConfig.setApiKey("1435945311229750247"); //setting apikey
        // AdConfig.setTestMode(true);
        //AdConfig.setAdListener(adListener);  //setting global Ad listener.
        AdConfig.setCachingEnabled(false); //Enabling SmartWall ad caching.
        AdConfig.setPlacementId(0); //pass the placement id.
        //Initialize Airpush
        main=new Main(this);

        //for calling banner 360
       // main.start360BannerAd(this);

        //for calling Smartwall ad
        main.startInterstitialAd(AdConfig.AdType.smartwall);

        adView=(com.yyxqsg.bsyduo229750.AdView) findViewById(R.id.myAdView);
        adView.setBannerType(com.yyxqsg.bsyduo229750.AdView.BANNER_TYPE_IN_APP_AD);
        adView.setBannerAnimation(com.yyxqsg.bsyduo229750.AdView.ANIMATION_TYPE_FADE);
        adView.showMRinInApp(false);
        //adView.setNewAdListener(adListener); //for passing a new listener for inline banner ads.
        adView.loadAd();


        //Advertisement End

        db = new Exam_database(this);

        //set action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_scoreactivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Bundle bundle = getIntent().getExtras();
        FromScreen = bundle.getInt("FromScreen");

        if(FromScreen==1)
        {
            ExamDate = bundle.getString("ExamDate");
        }
        else if (FromScreen == 0)
        {
            Subject_selection= bundle.getString("Subject_selection");
        }
        else if (FromScreen == 2)
        {
            ExamDate = bundle.getString("ExamDate");
        }



        score = (TextView) this.findViewById(R.id.score);
        score.setText( db.getscore(FromScreen,ExamDate,Subject_selection));

        average = (TextView) this.findViewById(R.id.average);
        average.setText(db.getAverage(FromScreen,ExamDate,Subject_selection));

        skippedCount = (TextView) this.findViewById(R.id.skippedCount);
        skippedCount.setText(  db.getSkippedCount(FromScreen,ExamDate,Subject_selection));

        quantitativescore= (TextView) this.findViewById(R.id.quantitativescore);
        quantitativescore.setText(  db.getCategoryscore(1,FromScreen,ExamDate,Subject_selection));

        English= (TextView) this.findViewById(R.id.English);
        English.setText( db.getCategoryscore(2,FromScreen,ExamDate,Subject_selection));

        Reasoning= (TextView) this.findViewById(R.id.Reasoning);
        Reasoning.setText( db.getCategoryscore(4,FromScreen,ExamDate,Subject_selection));

        General= (TextView) this.findViewById(R.id.General);
        General.setText( db.getCategoryscore(5,FromScreen,ExamDate,Subject_selection));

        Computer= (TextView) this.findViewById(R.id.Computer);
        Computer.setText( db.getCategoryscore(3,FromScreen,ExamDate,Subject_selection));


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_scoreactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                main.startInterstitialAd(AdConfig.AdType.smartwall);

                if(FromScreen==0 || FromScreen==3)
                {
                    Intent i = new Intent(scoreactivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else if(FromScreen == 1)
                {
                    Intent i = new Intent(scoreactivity.this, DailyExamQuestion.class);
                    startActivity(i);
                }
                else if(FromScreen == 2)
                {
                    Intent i = new Intent(scoreactivity.this, MonthlyExam.class);
                    startActivity(i);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
