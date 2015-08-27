package com.fyshadows.examframework.examframework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yyxqsg.bsyduo229750.AdConfig;
import com.yyxqsg.bsyduo229750.Main;


public class HomeActivity extends ActionBarActivity {

    Button start;
    Button score;
    Button reset;
    Button plus;
    Button minus;
    Button DailyExam;
    TextView timer;
    Exam_database db;
    SharedPreferences prefs;
    private Main main; //Declare here
    private com.yyxqsg.bsyduo229750.AdView adView;
    public int Timersaved=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Advertisement Start

        //Initialize Airpush
        main=new Main(this);

        //for calling Smartwall ad
        main.startInterstitialAd(AdConfig.AdType.smartwall);

        //for calling banner 360
        main.start360BannerAd(this);

        //for calling Smartwall ad
       // main.startInterstitialAd(AdConfig.AdType.smartwall);

        adView=(com.yyxqsg.bsyduo229750.AdView) findViewById(R.id.myAdView);
        adView.setBannerType(com.yyxqsg.bsyduo229750.AdView.BANNER_TYPE_IN_APP_AD);
        adView.setBannerAnimation(com.yyxqsg.bsyduo229750.AdView.ANIMATION_TYPE_FADE);
        adView.showMRinInApp(false);
        //adView.setNewAdListener(adListener); //for passing a new listener for inline banner ads.
        adView.loadAd();
        //Advertisement End


        adView=(com.yyxqsg.bsyduo229750.AdView) findViewById(R.id.myAdViewtop);
        adView.setBannerType(com.yyxqsg.bsyduo229750.AdView.BANNER_TYPE_IN_APP_AD);
        adView.setBannerAnimation(com.yyxqsg.bsyduo229750.AdView.ANIMATION_TYPE_FADE);
        adView.showMRinInApp(false);
        //adView.setNewAdListener(adListener); //for passing a new listener for inline banner ads.
        adView.loadAd();

        //set action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_homeactivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);



        db = new Exam_database(this);

        AppRater.app_launched(this);

        start = (Button) findViewById(R.id.Start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeactivity();
            }
        });


        // set the Timer view

        Timersaved= db.getTimer();
        if(Timersaved == 0)
        {
            Timersaved = 36;
        }
        timer=(TextView) findViewById(R.id.settimer);
        timer.setText(String.valueOf(Timersaved));

        score = (Button) findViewById(R.id.Score);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreactivity();
            }
        });

        reset = (Button) findViewById(R.id.Reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetexam();

            }
        });

        plus = (Button) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Timersaved=Timersaved+1;
                timer.setText(String.valueOf(Timersaved));
                db.updateTimer(Timersaved);
            }
        });

        minus = (Button) findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Timersaved=Timersaved-1;
                timer.setText(String.valueOf(Timersaved));
                db.updateTimer(Timersaved);
            }
        });

        DailyExam = (Button) findViewById(R.id.btnDailyExam);
        DailyExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, DailyExamQuestion.class);
                startActivity(i);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_homeactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.home) {
        //     finish();
        // }

        return super.onOptionsItemSelected(item);
    }

    // user defined functions
    public void homeactivity() {
        Intent i = new Intent(HomeActivity.this, Questionhome.class);
        startActivity(i);
    }

    public void scoreactivity() {
        Intent i = new Intent(HomeActivity.this, scoreactivity.class);
        startActivity(i);
    }

    public void resetexam() {
        Toast.makeText(getBaseContext(), "Exam reset successfull", Toast.LENGTH_LONG).show();
        db.Resetquestion();

    }
}
