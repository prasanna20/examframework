package com.fyshadows.examframework.examframework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreactivity);

        //Advertisement Start

        //Initialize Airpush
        main=new Main(this);

        //for calling banner 360
        main.start360BannerAd(this);

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



        score = (TextView) this.findViewById(R.id.score);
        score.setText( db.getscore(FromScreen,ExamDate));

        average = (TextView) this.findViewById(R.id.average);
        average.setText(db.getAverage(FromScreen,ExamDate));

        skippedCount = (TextView) this.findViewById(R.id.skippedCount);
        skippedCount.setText(  db.getSkippedCount(FromScreen,ExamDate));

        quantitativescore= (TextView) this.findViewById(R.id.quantitativescore);
        quantitativescore.setText(  db.getCategoryscore("Quantitative apps",FromScreen,ExamDate));

        English= (TextView) this.findViewById(R.id.English);
        English.setText( db.getCategoryscore("English language",FromScreen,ExamDate));

        Reasoning= (TextView) this.findViewById(R.id.Reasoning);
        Reasoning.setText( db.getCategoryscore("Reasoning",FromScreen,ExamDate));

        General= (TextView) this.findViewById(R.id.General);
        General.setText( db.getCategoryscore("General awareness",FromScreen,ExamDate));

        Computer= (TextView) this.findViewById(R.id.Computer);
        Computer.setText( db.getCategoryscore("Computer knowledge",FromScreen,ExamDate));


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
                Intent i = new Intent(scoreactivity.this, HomeActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
