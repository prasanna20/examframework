package com.fyshadows.examframework.examframework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class SubjectSelection extends ActionBarActivity {
    Button startExam;
    CheckBox checkbox_Quantitative_Aptitude;
    CheckBox checkbox_Reasoning;
    CheckBox checkbox_General_Awareness;
    CheckBox checkbox_English_Language;
    CheckBox checkbox_Computer_Knowledge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_subjectselection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startExam = (Button) findViewById(R.id.StartExam);
        startExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionHomeActivity();
            }
        });

        checkbox_Quantitative_Aptitude=(CheckBox) findViewById(R.id.checkbox_Quantitative_Aptitude);
        checkbox_Reasoning=(CheckBox) findViewById(R.id.checkbox_Reasoning);
        checkbox_General_Awareness=(CheckBox) findViewById(R.id.checkbox_General_Awareness);
        checkbox_English_Language=(CheckBox) findViewById(R.id.checkbox_English_Language);
        checkbox_Computer_Knowledge=(CheckBox) findViewById(R.id.checkbox_Computer_Knowledge);
    }

    // user defined functions
    public void QuestionHomeActivity() {
        //Start : Insert Analysis
        //masterdetails.insertAnalysis(SubjectSelection.this, 9);
        //End : Insert Analysis

        int Quantitative_Aptitude=0;
        int Reasoning=0;
        int General_Awareness=0;
        int English_Language=0;
        int Computer_Knowledge=0;
        if(!checkbox_Quantitative_Aptitude.isChecked() && !checkbox_Computer_Knowledge.isChecked() && !checkbox_English_Language.isChecked() && !checkbox_General_Awareness.isChecked() && !checkbox_Reasoning.isChecked())
        {
            Toast.makeText(this,"Please select atleast one subject",Toast.LENGTH_SHORT).show();
            return;
        }

        if(checkbox_Quantitative_Aptitude.isChecked())
        {
            Quantitative_Aptitude=1;
        }
        if(checkbox_Computer_Knowledge.isChecked())
        {
            Computer_Knowledge=3;
        }
        if(checkbox_English_Language.isChecked())
        {
            English_Language=2;
        }
        if(checkbox_General_Awareness.isChecked())
        {
            General_Awareness=5;
        }
        if(checkbox_Reasoning.isChecked())
        {
            Reasoning=4;
        }

        Intent i = new Intent(SubjectSelection.this, Questionhome.class);
        Bundle bundle = new Bundle();
        bundle.putInt("FromScreen", 0);
        bundle.putString("Subject_selection",String.valueOf(Quantitative_Aptitude) + ',' + String.valueOf(Computer_Knowledge) + ',' + String.valueOf(English_Language) + ',' + String.valueOf(General_Awareness) + ',' + String.valueOf(Reasoning));
        /*bundle.putInt("Computer_Knowledge", Computer_Knowledge);
        bundle.putInt("English_Language", English_Language);
        bundle.putInt("General_Awareness", General_Awareness);
        bundle.putInt("Reasoning", Reasoning);*/
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i;
                 i = new Intent(SubjectSelection.this, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
