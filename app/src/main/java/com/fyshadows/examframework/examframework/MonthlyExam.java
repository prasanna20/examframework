package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyxqsg.bsyduo229750.AdConfig;
import com.yyxqsg.bsyduo229750.Main;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Adapter.MonthlyExamAdapter;
import ExamFramework_AsyncTask.AsyncUpdateNewValues;

public class MonthlyExam   extends ListActivity {
    private ListView listView;
    MonthlyExamAdapter adapter;
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
        setContentView(R.layout.activity_monthly_exam);

        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.actionbar_monthlyexam);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        txtNoExam=(TextView) this.findViewById(R.id.txtNoExam);

        refresh=(ImageView) this.findViewById(R.id.refresh);
        refresh = (ImageView) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masterdetails.isOnline(MonthlyExam.this)) {
                    Toast.makeText(getBaseContext(), "Refreshing", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new AsyncUpdateNewValues(MonthlyExam.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    else
                    {
                        new AsyncUpdateNewValues(MonthlyExam.this).execute();
                    }

                } else {
                    Toast.makeText(getBaseContext(), "Please Connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ListView myListView = (ListView) findViewById(android.R.id.list);
        list = db.getMonthlyExamDate();
        int index = 0;
        int top = 0;

        if (!list.isEmpty()) {
            adapter = new MonthlyExamAdapter(this, list);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(MonthlyExam.this, HomeActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
