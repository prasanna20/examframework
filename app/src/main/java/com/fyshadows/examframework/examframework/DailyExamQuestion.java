package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yyxqsg.bsyduo229750.AdConfig;
import com.yyxqsg.bsyduo229750.Main;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Adapter.DailyExamAdapter;
import ExamFramework_Data.DailyExam;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daily_exam_question);


        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.actionbar_dailyexam);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        txtNoExam=(TextView) this.findViewById(R.id.txtNoExam);

        //Advertisement Start

        //Initialize Airpush
        main=new Main(this);

        //for calling Smartwall ad
        main.startInterstitialAd(AdConfig.AdType.smartwall);

        //for calling banner 360
        main.start360BannerAd(this);

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
                Intent i = new Intent(DailyExamQuestion.this, HomeActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
