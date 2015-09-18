package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yyxqsg.bsyduo229750.AdConfig;
import com.yyxqsg.bsyduo229750.Main;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Adapter.DailyArticleAdapter;
import ExamFramework_Adapter.DailyExamAdapter;
import ExamFramework_Data.DailyArticleData;

public class DailyArticle extends ListActivity {

    private ListView listView;
    DailyArticleAdapter adapter;
    private List<ExamFramework_Data.DailyArticleData> DailyArticleData;
    Exam_database db = new Exam_database(this);
    List<ExamFramework_Data.DailyArticleData> list = new ArrayList<ExamFramework_Data.DailyArticleData>();
    int scrolly = 0;
    int first = 0;
    public String ExamDate;
    TextView  txtNoArticle;
    private Main main;
    private com.yyxqsg.bsyduo229750.AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_article);

        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.actionbar_dailyarticle);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        ExamDate = bundle.getString("ExamDate");

       txtNoArticle = (TextView) this.findViewById(R.id.txtNoArticle);

        //Advertisement Start

        //Initialize Airpush
        main=new Main(this);


        adView=(com.yyxqsg.bsyduo229750.AdView) findViewById(R.id.myAdView);
        adView.setBannerType(com.yyxqsg.bsyduo229750.AdView.BANNER_TYPE_IN_APP_AD);
        adView.setBannerAnimation(com.yyxqsg.bsyduo229750.AdView.ANIMATION_TYPE_FADE);
        adView.showMRinInApp(false);
        //adView.setNewAdListener(adListener); //for passing a new listener for inline banner ads.
        adView.loadAd();
        //Advertisement End

        ListView myListView = (ListView) findViewById(android.R.id.list);
        list = db.getDailyArticle(ExamDate);
        int index = 0;
        int top = 0;

        if (!list.isEmpty()) {
            adapter = new DailyArticleAdapter(this, list);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (first == 0) {
                myListView.setSelection(scrolly);
            }

            if (first > 0) {
                //  myListView.setSelection(scrolly);
                myListView.setSelectionFromTop(index, top);
            }
            txtNoArticle.setVisibility(View.INVISIBLE)  ;
        }
        else
        {

            txtNoArticle.setVisibility(View.VISIBLE)  ;

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily_article, menu);
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
                Intent i = new Intent(DailyArticle.this, DailyExamQuestion.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
