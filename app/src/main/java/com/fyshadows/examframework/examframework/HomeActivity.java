package com.fyshadows.examframework.examframework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yyxqsg.bsyduo229750.AdConfig;
import com.yyxqsg.bsyduo229750.AdListener;
import com.yyxqsg.bsyduo229750.AdView;
import com.yyxqsg.bsyduo229750.Main;


//Advertisement

public class HomeActivity extends ActionBarActivity implements AdListener {

    //Main Home Button
    Button start;
    Button score;
    Button MonthlyExam;
    Button DailyExam;

    //Adjust Question Time
    Button plus;
    Button minus;
    TextView timer;

    //Menu Button
    ImageButton vn;
    ImageButton chat;

    //Database Declaration
    Exam_database db;

//Start : Advertisement Goes Here


    private com.yyxqsg.bsyduo229750.AdView adView;
    private Main main;
    public int Timersaved = 0;

//End :   Advertisement Goes Here

    //Analytics Section
    masterdetails masterdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //set action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_homeactivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //Start : Insert Analysis
        masterdetails = new masterdetails(this);
        try {
            masterdetails.insertAnalysis(this, 1);
        } catch (Exception ex) {
            Log.i("Error", "Error in inserting analysis");
        }
        //End : Insert Analysis

        //Database Intialization
        db = new Exam_database(this);

        AppRater.app_launched(this);

        start = (Button) findViewById(R.id.Start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeactivity();
            }
        });

//Start Advertisement

        AdConfig.setAppId(280371);  //setting appid.
        AdConfig.setApiKey("1435945311229750247"); //setting apikey
        AdConfig.setCachingEnabled(true); //Enabling SmartWall ad caching.
        AdConfig.setPlacementId(0); //pass the placement id.

        adView=(AdView) findViewById(R.id.myAdView);
        adView.setBannerType(AdView.BANNER_TYPE_IN_APP_AD);
        adView.setBannerAnimation(AdView.ANIMATION_TYPE_FADE);
        AdView.setAdListener(this);

        //Initialize Airpush
        main=new Main(this, this);

        //for calling banner 360
        main.startInterstitialAd(AdConfig.AdType.smartwall,this);

        //for calling Smartwall ad
        //main.startInterstitialAd(AdConfig.AdType.smartwall, this);

//END   Advertisement

// Start : Set Timer

        Timersaved = db.getTimer();
        if (Timersaved == 0) {
            Timersaved = 36;
        }
        timer = (TextView) findViewById(R.id.settimer);
        timer.setText(String.valueOf(Timersaved));

        plus = (Button) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Timersaved = Timersaved + 1;
                timer.setText(String.valueOf(Timersaved));
                db.updateTimer(Timersaved);
            }
        });

        minus = (Button) findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Timersaved = Timersaved - 1;
                timer.setText(String.valueOf(Timersaved));
                db.updateTimer(Timersaved);
            }
        });

        // END : Set Timer

        score = (Button) findViewById(R.id.Score);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreactivity();
            }
        });

        MonthlyExam = (Button) findViewById(R.id.MonthlyExam);
        MonthlyExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthlyExamActivity();

            }
        });

        DailyExam = (Button) findViewById(R.id.btnDailyExam);
        DailyExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start : Insert Analysis
                masterdetails.insertAnalysis(HomeActivity.this, 2);
                //End : Insert Analysis
                Intent i = new Intent(HomeActivity.this, DailyExamQuestion.class);
                startActivity(i);
            }
        });

        vn = (ImageButton) findViewById(R.id.vn);
        vn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start : Insert Analysis
                masterdetails.insertAnalysis(HomeActivity.this, 4);
                //End : Insert Analysis
                Intent i = new Intent(HomeActivity.this, ViewNotification.class);
                startActivity(i);

            }
        });

        chat = (ImageButton) findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ChatRoom.class);
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

    @Override
    public void onAdClosed() {
        //This will get called when an ad is closing/resizing from an expanded state.
    }

    @Override
    public void onAdCached(AdConfig.AdType adType) {
        //This will get called when an ad is cached.

    }

    @Override
    public void onError(AdListener.ErrorType errorCode, String errorMessage) {
        /* This will get called when any error has occurred. This will also get called if the SDK notices any integration mistakes.
         You can check the ErrorType to know the error type. */
    }


    @Override
    public void onAdLoading() {
        //This will get called when a rich media ad is loading.
    }

    @Override
    public void onAdLoaded() {
        //This will get called when an ad has loaded.
    }


    @Override
    public void onAdExpanded() {
        //This will get called when an ad is showing on a user's screen. This may cover the whole UI.
    }

    @Override
    public void onAdClicked() {
        //This will get called when ad is clicked.
    }

    // user defined functions
    public void homeactivity() {
        //Start : Insert Analysis
        masterdetails.insertAnalysis(HomeActivity.this, 9);
        //End : Insert Analysis
        Intent i = new Intent(HomeActivity.this, SubjectSelection.class);
        Bundle bundle = new Bundle();
        bundle.putInt("FromScreen", 0);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void scoreactivity() {

        Intent i = new Intent(HomeActivity.this, scoreactivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("FromScreen", 3);
        i.putExtras(bundle);
        startActivity(i);
    }


    public void MonthlyExamActivity() {

        Intent i = new Intent(HomeActivity.this, MonthlyExam.class);
        startActivity(i);
    }


    public void resetexam() {
        //Start : Insert Analysis
        masterdetails.insertAnalysis(HomeActivity.this, 8);
        //End : Insert Analysis
        Toast.makeText(getBaseContext(), "Exam reset successfull", Toast.LENGTH_LONG).show();
        db.Resetquestion();

    }


}
