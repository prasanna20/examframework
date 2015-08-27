package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daily_exam_question);


        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.actionbar_dailyexam);
        getActionBar().setDisplayHomeAsUpEnabled(true);


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
                //  myListView.setSelection(scrolly);
                myListView.setSelectionFromTop(index, top);
            }

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
