package com.fyshadows.examframework.examframework;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyxqsg.bsyduo229750.AdConfig;
import com.yyxqsg.bsyduo229750.Main;

import java.util.ArrayList;
import java.util.Random;

import ExamFramework_Data.DailyExam;
import ExamFramework_Data.MonthlyExamData;


public class Questionhome extends ActionBarActivity {

    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    public TextView txt_timer;
    private int startTime;
    private final long interval = 1 * 1000;
    RelativeLayout messagelayout;
    RelativeLayout questiondetails_layout;
    Button showanswer;
    RelativeLayout top_layout;
    Button Nextquestion;
    Exam_database ed = new Exam_database(this);
    ArrayList<storequestiondetails> sd;
    ArrayList<DailyExam> DailyExam;
    ArrayList<MonthlyExamData> MonthlyExamData;
    int rowcount = 0;
    public int disable = 0;
    public int answered = 0;
    boolean isRunning = false;
    public int FromScreen = 0;
    public String ExamDate;

    static int settimer = masterdetails.timer;


    //store correct answer and questionnumber
    public String Correctanswer;
    public int QuestionNumber;
    public int QuestionNumberDailyExam;
    public String Category;
    public Boolean course_completed_flag = false;

    public int totalquestions = 0;

    public TextView txtquestion;
    public TextView txtquestion_details;
    public Button txtanswer1;
    public Button txtanswer2;
    public Button txtanswer3;
    public Button txtanswer4;
    public Button txtanswer5;
    public TextView displaymessage;
    public TextView nextmessage;

    public TextView actionbar_queststat;
    public TextView actionbar_questcategory;
    public ImageView QuestionClose;
    public String[] myString;
    public Resources res;
    private static final Random rgenerator = new Random();
    Exam_database db = new Exam_database(Questionhome.this);

    private Main main; //Declare here
    private com.yyxqsg.bsyduo229750.AdView adView;

    //Category
   public String Subject_selection;
   /* public int Quantitative_Aptitude;
    public int Computer_Knowledge;
    public int English_Language;
    public int General_Awareness;
    public int Reasoning;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionhome);

//Advertisement Start

        //Initialize Airpush
        main = new Main(this);
        adView = (com.yyxqsg.bsyduo229750.AdView) findViewById(R.id.myAdView);
        adView.setBannerType(com.yyxqsg.bsyduo229750.AdView.BANNER_TYPE_IN_APP_AD);
        adView.setBannerAnimation(com.yyxqsg.bsyduo229750.AdView.ANIMATION_TYPE_FADE);
        adView.showMRinInApp(false);
        adView.loadAd();

 //Advertisement End

        //set action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_questionhome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        res = getResources();

        startTime = db.getTimer() * 1000;
//setting question
        txtquestion = (TextView) this.findViewById(R.id.txtquestion);
        txtquestion_details = (TextView) this.findViewById(R.id.question_details);
        txtanswer1 = (Button) this.findViewById(R.id.txtanswer1);
        txtanswer2 = (Button) this.findViewById(R.id.txtanswer2);
        txtanswer3 = (Button) this.findViewById(R.id.txtanswer3);
        txtanswer4 = (Button) this.findViewById(R.id.txtanswer4);
        txtanswer5 = (Button) this.findViewById(R.id.txtanswer5);
        displaymessage = (TextView) this.findViewById(R.id.displaymessage);
        actionbar_queststat = (TextView) this.findViewById(R.id.actionbar_queststat);
        actionbar_questcategory = (TextView) this.findViewById(R.id.actionbar_questcategory);

//setting the timer
        txt_timer = (TextView) this.findViewById(R.id.Countdowntimer);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        txt_timer.setText(txt_timer.getText() + String.valueOf(startTime / 1000));
        countDownTimer.start();


//setting the message layout invisible
        messagelayout = (RelativeLayout) findViewById(R.id.top_layout);
        messagelayout.setVisibility(View.INVISIBLE);

        questiondetails_layout = (RelativeLayout) findViewById(R.id.questiondetails_layout);
        questiondetails_layout.setVisibility(View.INVISIBLE);


// setting shw answer button
        showanswer = (Button) findViewById(R.id.showanswer);
        showanswer.setVisibility(View.VISIBLE);
        showanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countDownTimer.cancel();
                if (answered == 0) {
                    //to store the skip
                    db.updateScore(QuestionNumber, 3, Integer.parseInt(txt_timer.getText().toString()),FromScreen);
                }
                showanswer();
                showmessage(3);
            }
        });

//next textview clicked
        nextmessage = (TextView) findViewById(R.id.nextmessage);
        nextmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nexttap();

            }
        });

        //next question button clicked
        displaymessage = (TextView) findViewById(R.id.displaymessage);
        displaymessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nexttap();

            }
        });


        //next question button clicked
        top_layout = (RelativeLayout) findViewById(R.id.top_layout);
        top_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nexttap();

            }
        });


        txtquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("exam", "clicked");
                questiondetails_layout.setVisibility(View.VISIBLE);

            }
        });

        questiondetails_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questiondetails_layout.setVisibility(View.INVISIBLE);

            }
        });


        QuestionClose = (ImageView) findViewById(R.id.btnCancel);
        QuestionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questiondetails_layout.setVisibility(View.INVISIBLE);

            }
        });


        txtanswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countDownTimer.cancel();
                if (disable == 0) {

                    String FinalTimerValue=String.valueOf(startTime);

                    if(!txt_timer.getText().toString().contains("Time's up!"))
                    {
                        FinalTimerValue=txt_timer.getText().toString();
                    }

                    if (Correctanswer.equalsIgnoreCase(txtanswer1.getText().toString())) {
                        txtanswer1.setBackgroundColor(Color.GREEN);
                        showmessage(1);
                        db.updateScore(QuestionNumber, 1, Integer.parseInt(FinalTimerValue),FromScreen);
                    } else {
                        txtanswer1.setBackgroundColor(Color.RED);
                        showmessage(2);
                        db.updateScore(QuestionNumber, 2, Integer.parseInt(FinalTimerValue),FromScreen);
                        showanswer();
                    }
                    answered = 1;
                    disable = 1;
                }
            }
        });

        txtanswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countDownTimer.cancel();
                if (disable == 0) {

                    String FinalTimerValue=String.valueOf(startTime);

                    if(!txt_timer.getText().toString().contains("Time"))
                    {
                        FinalTimerValue=txt_timer.getText().toString();
                    }


                    if (Correctanswer.equalsIgnoreCase(txtanswer2.getText().toString())) {
                        showmessage(1);
                        txtanswer2.setBackgroundColor(Color.GREEN);
                        db.updateScore(QuestionNumber, 1, Integer.parseInt(FinalTimerValue),FromScreen);
                    } else {
                        txtanswer2.setBackgroundColor(Color.RED);
                        showmessage(2);
                        db.updateScore(QuestionNumber, 2, Integer.parseInt(FinalTimerValue),FromScreen);

                        showanswer();
                    }
                    disable = 1;
                    answered = 1;
                }
            }
        });

        txtanswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countDownTimer.cancel();
                if (disable == 0) {

                    String FinalTimerValue=String.valueOf(startTime);

                    if(!txt_timer.getText().toString().contains("Time"))
                    {
                        FinalTimerValue=txt_timer.getText().toString();
                    }

                    if (Correctanswer.equalsIgnoreCase(txtanswer3.getText().toString())) {
                        showmessage(1);
                        txtanswer3.setBackgroundColor(Color.GREEN);
                        db.updateScore(QuestionNumber, 1, Integer.parseInt(FinalTimerValue),FromScreen);
                    } else {
                        txtanswer3.setBackgroundColor(Color.RED);
                        showmessage(2);
                        showanswer();
                        db.updateScore(QuestionNumber, 2, Integer.parseInt(FinalTimerValue),FromScreen);
                    }
                    disable = 1;
                    answered = 1;
                }
            }
        });

        txtanswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countDownTimer.cancel();
                if (disable == 0) {

                    String FinalTimerValue=String.valueOf(startTime);

                    if(!txt_timer.getText().toString().contains("Time"))
                    {
                        FinalTimerValue=txt_timer.getText().toString();
                    }

                    if (Correctanswer.equalsIgnoreCase(txtanswer4.getText().toString())) {
                        showmessage(1);
                        txtanswer4.setBackgroundColor(Color.GREEN);
                        db.updateScore(QuestionNumber, 1, Integer.parseInt(FinalTimerValue),FromScreen);
                    } else {
                        txtanswer4.setBackgroundColor(Color.RED);
                        showmessage(2);
                        db.updateScore(QuestionNumber, 2, Integer.parseInt(FinalTimerValue),FromScreen);
                        showanswer();
                    }
                    disable = 1;
                    answered = 1;
                }
            }
        });

        txtanswer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String FinalTimerValue=String.valueOf(startTime);

                if(!txt_timer.getText().toString().contains("Time"))
                {
                    FinalTimerValue=txt_timer.getText().toString();
                }


                countDownTimer.cancel();
                if (disable == 0) {
                    if (Correctanswer.equalsIgnoreCase(txtanswer5.getText().toString())) {
                        txtanswer5.setBackgroundColor(Color.GREEN);
                        showmessage(1);
                        db.updateScore(QuestionNumber, 1, Integer.parseInt(FinalTimerValue),FromScreen);
                    } else {
                        txtanswer5.setBackgroundColor(Color.RED);
                        showmessage(2);
                        db.updateScore(QuestionNumber, 2, Integer.parseInt(FinalTimerValue),FromScreen);
                        showanswer();
                    }
                    answered = 1;
                    disable = 1;
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        FromScreen = bundle.getInt("FromScreen");

       /* Computer_Knowledge= bundle.getInt("Computer_Knowledge");
        English_Language= bundle.getInt("English_Language");
        General_Awareness= bundle.getInt("General_Awareness");
        Reasoning= bundle.getInt("Reasoning");*/

        if (FromScreen == 0) {
            Subject_selection= bundle.getString("Subject_selection");
            //total question count
            totalquestions = db.getQuestionCountCategoryWise(Subject_selection);
            //get question data from database
            sd = ed.getQuestionDetailsNew(Subject_selection);
            rowcount = sd.size();
            Log.i("check", String.valueOf(rowcount));
        } else if (FromScreen == 1) {
            //Start : Insert Analysis
            masterdetails masterdetails=new masterdetails(this);
            masterdetails.insertAnalysis(this, 7);
            //End : Insert Analysis
            ExamDate = bundle.getString("ExamDate");
            Log.i("Into Home Activity", ExamDate);
            //totalquestions = db.getQuestionCount();
            //get question data from database
            DailyExam = ed.getDailyExamQuestion(ExamDate);
            totalquestions=DailyExam.size();
            rowcount = DailyExam.size();
        }
        else if (FromScreen == 2) {
            //Start : Insert Analysis
            masterdetails masterdetails=new masterdetails(this);
            masterdetails.insertAnalysis(this, 7);
            //End : Insert Analysis
            ExamDate = bundle.getString("ExamDate");
            Log.i("Into Home Activity", ExamDate);
            //totalquestions = db.getQuestionCount();
            //get question data from database
            MonthlyExamData = ed.getMonthlyExamQuestion(ExamDate);
            totalquestions=ed.getMonthlyQuestionCount(ExamDate);
            Log.i("Prassy count", String.valueOf(totalquestions));
            rowcount = MonthlyExamData.size();
        }


//set values to text view
        if ((FromScreen == 0 && !sd.isEmpty()) || (FromScreen==1 && !DailyExam.isEmpty()) || (FromScreen==2 && !MonthlyExamData.isEmpty())) {
            setquestion(FromScreen);
        } else {
            txtquestion.setText("you competed the exam. Please reset it to start from first.");
            txtquestion_details.setText("you competed the exam. Please reset it to start from first.");
            messagelayout.setVisibility(View.INVISIBLE);
            showanswer.setVisibility(View.INVISIBLE);
            txtanswer1.setVisibility(View.INVISIBLE);
            txtanswer2.setVisibility(View.INVISIBLE);
            txtanswer3.setVisibility(View.INVISIBLE);
            txtanswer4.setVisibility(View.INVISIBLE);
            txtanswer5.setVisibility(View.INVISIBLE);
            txt_timer.setVisibility(View.INVISIBLE);
            actionbar_queststat.setText("Congrats");

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_questionhome, menu);
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
                Intent i;
                if(FromScreen==0) {
                     i = new Intent(Questionhome.this, HomeActivity.class);
                }
                else if(FromScreen==1){
                     i = new Intent(Questionhome.this, DailyExamQuestion.class);
                }
                else {
                    i = new Intent(Questionhome.this, MonthlyExam.class);
                }

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            isRunning = false;
            txt_timer.setText("Time's up!");
            db.updateScore(QuestionNumber, 3, settimer,FromScreen);
            showanswer();
            showmessage(4);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            isRunning = true;

            txt_timer.setText("" + millisUntilFinished / 1000);
        }

    }



    public void setquestion(int Fromflag) {

        disable = 0;
        answered = 0;
        txtanswer1.setBackgroundColor(Color.parseColor("#ffffff"));
        txtanswer2.setBackgroundColor(Color.parseColor("#D1D1D1"));
        txtanswer3.setBackgroundColor(Color.parseColor("#ffffff"));
        txtanswer4.setBackgroundColor(Color.parseColor("#D1D1D1"));
        txtanswer5.setBackgroundColor(Color.parseColor("#ffffff"));



        //Fraud text to increase count
        //actionbar_queststat.setText(QuestionNumber + " of " + totalquestions);
        if (Fromflag == 0)

        {
            QuestionNumber = sd.get(0).getQuestionNo();
            QuestionNumberDailyExam= sd.get(0).getQuestionNo();
          //  actionbar_queststat.setText(QuestionNumber + " of 1000");
            actionbar_queststat.setVisibility(View.INVISIBLE);
            txtquestion.setText(Html.fromHtml(sd.get(0).getQuestion()));
            txtquestion_details.setText(Html.fromHtml(sd.get(0).getQuestion()));
            txtanswer1.setText(sd.get(0).getChoice1());
            txtanswer2.setText(sd.get(0).getChoice2());
            txtanswer3.setText(sd.get(0).getChoice3());
            txtanswer4.setText(sd.get(0).getChoice4());
            txtanswer5.setText(sd.get(0).getChoice5());
            Category = sd.get(0).getCategory();
            actionbar_questcategory.setText(Category);
            Correctanswer = sd.get(0).getCorrect_Ans();
        } else if (Fromflag == 1) {
            QuestionNumber = DailyExam.get(0).getid();
            QuestionNumberDailyExam= DailyExam.get(0).getQuesNo();
            actionbar_queststat.setText( QuestionNumberDailyExam + " of " + totalquestions);
            txtquestion.setText(Html.fromHtml(DailyExam.get(0).getQues()));
            txtquestion_details.setText(Html.fromHtml(DailyExam.get(0).getQues()));
            txtanswer1.setText(DailyExam.get(0).getChoice1());
            txtanswer2.setText(DailyExam.get(0).getChoice2());
            txtanswer3.setText(DailyExam.get(0).getChoice3());
            txtanswer4.setText(DailyExam.get(0).getChoice4());
            txtanswer5.setText(DailyExam.get(0).getChoice5());
            Category = DailyExam.get(0).getCategory();
            actionbar_questcategory.setText(Category);

         switch (DailyExam.get(0).getCorrectAns())
            {
                case 1:
                    Correctanswer=DailyExam.get(0).getChoice1();
                    break;
                case 2:
                    Correctanswer=DailyExam.get(0).getChoice2();
                    break;
                case 3:
                    Correctanswer=DailyExam.get(0).getChoice3();
                    break;
                case 4:
                    Correctanswer=DailyExam.get(0).getChoice4();
                    break;
                case 5:
                    Correctanswer=DailyExam.get(0).getChoice5();
                    break;
            }
        }
        else if (Fromflag == 2) {
            QuestionNumber =MonthlyExamData.get(0).getid();
            QuestionNumberDailyExam= MonthlyExamData.get(0).getQuesNo();
            actionbar_queststat.setText( QuestionNumberDailyExam + " of " + totalquestions);
            txtquestion.setText(Html.fromHtml(MonthlyExamData.get(0).getQues()));
            txtquestion_details.setText(Html.fromHtml(MonthlyExamData.get(0).getQues()));
            txtanswer1.setText(MonthlyExamData.get(0).getChoice1());
            txtanswer2.setText(MonthlyExamData.get(0).getChoice2());
            txtanswer3.setText(MonthlyExamData.get(0).getChoice3());
            txtanswer4.setText(MonthlyExamData.get(0).getChoice4());
            txtanswer5.setText(MonthlyExamData.get(0).getChoice5());
            Category = MonthlyExamData.get(0).getCategory();
            actionbar_questcategory.setText(Category);

            switch (MonthlyExamData.get(0).getCorrectAns())
            {
                case 1:
                    Correctanswer=MonthlyExamData.get(0).getChoice1();
                    break;
                case 2:
                    Correctanswer=MonthlyExamData.get(0).getChoice2();
                    break;
                case 3:
                    Correctanswer=MonthlyExamData.get(0).getChoice3();
                    break;
                case 4:
                    Correctanswer=MonthlyExamData.get(0).getChoice4();
                    break;
                case 5:
                    Correctanswer=MonthlyExamData.get(0).getChoice5();
                    break;
            }
        }
    }

    public void showanswer() {
        if(Correctanswer != null) {
            if (Correctanswer.equalsIgnoreCase(txtanswer1.getText().toString())) {
                txtanswer1.setBackgroundColor(Color.GREEN);
            } else if (Correctanswer.equalsIgnoreCase(txtanswer2.getText().toString())) {
                txtanswer2.setBackgroundColor(Color.GREEN);
            } else if (Correctanswer.equalsIgnoreCase(txtanswer3.getText().toString())) {
                txtanswer3.setBackgroundColor(Color.GREEN);
            } else if (Correctanswer.equalsIgnoreCase(txtanswer4.getText().toString())) {
                txtanswer4.setBackgroundColor(Color.GREEN);
            } else if (Correctanswer.equalsIgnoreCase(txtanswer5.getText().toString())) {
                txtanswer5.setBackgroundColor(Color.GREEN);
            }
        }
    }

    public void showmessage(int indicator) {
        try {
            messagelayout.setVisibility(View.VISIBLE);
            showanswer.setVisibility(View.INVISIBLE);
            if (indicator == 1) {

                myString = res.getStringArray(R.array.correct);

                String q = myString[rgenerator.nextInt(myString.length)];

                displaymessage.setText(q);
            } else if (indicator == 2) {
                myString = res.getStringArray(R.array.wrong);

                String q = myString[rgenerator.nextInt(myString.length)];

                displaymessage.setText(q);

            } else if (indicator == 3) {
                displaymessage.setText("Learn it and Rock it");
            } else if (indicator == 4) {
                displaymessage.setText("Time Never Wait!");
            }
        }
        catch(Exception ex)
        {
            Log.i("Error","Error in question home");
        }
    }

    public void scoreactivity() {
        Intent i = new Intent(Questionhome.this, scoreactivity.class);
        Bundle bundle = new Bundle();
        if(FromScreen==0) {
            bundle.putInt("FromScreen", FromScreen);
            bundle.putString("Subject_selection", Subject_selection);
        }
        else {
            bundle.putInt("FromScreen", FromScreen);
            bundle.putString("ExamDate",ExamDate);
        }
        i.putExtras(bundle);
        startActivity(i);
    }

    public void nexttap() {

        if (!course_completed_flag) {
            if (isRunning) {
                countDownTimer.cancel();
            }
            if(FromScreen==0)
            {

                sd.remove(0);
            }else if (FromScreen==1)
            {
                DailyExam.remove(0);
            }
            else if (FromScreen==2)
            {
                MonthlyExamData.remove(0);
            }

            if ((FromScreen == 0 && !sd.isEmpty()) || (FromScreen==1 && !DailyExam.isEmpty()) || (FromScreen==2 && !MonthlyExamData.isEmpty())) {
                setquestion(FromScreen);
                messagelayout.setVisibility(View.INVISIBLE);
                showanswer.setVisibility(View.VISIBLE);
                countDownTimer.start();
            } else {
                countDownTimer.cancel();
                messagelayout.setVisibility(View.VISIBLE);
                showanswer.setVisibility(View.INVISIBLE);
                displaymessage.setText("Awesome ! You completed the course !");
                course_completed_flag = true;
            }
        } else {
            main.startInterstitialAd(AdConfig.AdType.smartwall);
            scoreactivity();
        }
    }




}


