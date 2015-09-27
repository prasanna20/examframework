package com.fyshadows.examframework.examframework;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ExamFramework_Data.DailyArticleData;
import ExamFramework_Data.DailyExam;
import ExamFramework_Data.notificationtable;

/**
 * Created by Prasanna on 16-03-2015.
 */

public class Exam_database extends SQLiteOpenHelper {


    JSONParser jsonParser = new JSONParser();

    public Exam_database(Context context) {

        super(context, "exam_database", null, 3);// version number is given at

    }

    private boolean doesDatabaseExist(ContextWrapper context) {
        File dbFile = context.getDatabasePath("exam_database");
        return dbFile.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase myDB) {

        myDB.execSQL("CREATE TABLE if not exists EF_mob_UserDetails(mail_id varchar(250),GCM_registration_id TEXT );");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_MasterQuestion(Questionno int,Question text,Choice1 varchar(350),Choice2 varchar(350),Choice3 varchar(350),Choice4 varchar(350),Choice5 varchar(350),Correct_ans varchar(350),Category,answered_flag int,time_taken int);");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_QuestionTimer(Timervalue int);");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_DailyQues(id  int,quesDate date,QuesNo int ,Ques text,Choice1 varchar(350),Choice2 varchar(350),Choice3 varchar(350),Choice4 varchar(350),Choice5 varchar(350),CorrectAns varchar(350),Category varchar(250),answeredFlag int,timeTaken int,Rank int);");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_DailyArticle(ArticleNo int, ArticleDate Date,Topic varchar(300),ArticleDesc text);");

       /*
       Answered_flag
       0   -    Not attempted yet
       1   -    correct answer
       2   -    wrong answer
       3   -    Skip answer
       */

    }


    @Override
    public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion) {

        myDB.execSQL("CREATE TABLE if not exists EF_mob_QuestionTimer(Timervalue int);");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_DailyQues(id  int,quesDate date,QuesNo int ,Ques text,Choice1 varchar(350),Choice2 varchar(350),Choice3 varchar(350),Choice4 varchar(350),Choice5 varchar(350),CorrectAns varchar(350),Category varchar(250),answeredFlag int,timeTaken int,Rank int);");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_DailyArticle(ArticleNo int, ArticleDate Date,Topic varchar(300),ArticleDesc text);");

        Log.i("table created", "table created");
    }

    // Register admin
    public void StoreUserDetails(String email, String gcm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mail_id", email);
        values.put("GCM_registration_id", gcm);

        // Inserting Row
        db.insert("EF_mob_UserDetails", null, values);

        // Closing database connection
        if (db.isOpen()) {
            db.close();
        }
    }

    //insert question details
    public void InsertQuestionDetails(int QuestionNo, String Question, String Choice1, String Choice2, String Choice3, String Choice4, String Choice5, String Correct_Ans, String Category) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete("PP_mob_PPmasterdetails", null, null);
        ContentValues values = new ContentValues();

        values.put("QuestionNo", QuestionNo);
        values.put("Question", Question);
        values.put("Choice1", Choice1);
        values.put("Choice2", Choice2);
        values.put("Choice3", Choice3);
        values.put("Choice4", Choice4);
        values.put("Choice5", Choice5);
        values.put("Correct_Ans", Correct_Ans);
        values.put("Category", Category);
        values.put("answered_flag", 0);
        values.put("time_taken", 0);

        db.insert("EF_mob_MasterQuestion", null, values);

        if (db.isOpen()) {
            db.close();
        }
    }

    //Insert Daily Question details
    public void InsertDailyQuestionDetails(
            int id, String quesDate, int QuesNo, String Ques, String Choice1, String Choice2, String Choice3, String Choice4, String Choice5, int CorrectAns, String Category) {
        SQLiteDatabase db = this.getWritableDatabase();
        //int answeredFlag ,int timeTaken ,int Rank
        ContentValues values = new ContentValues();

        values.put("id", id);
        values.put("quesDate", quesDate);
        values.put("QuesNo", QuesNo);
        values.put("Ques", Ques);
        values.put("Choice1", Choice1);
        values.put("Choice2", Choice2);
        values.put("Choice3", Choice3);
        values.put("Choice4", Choice4);
        values.put("Choice5", Choice5);
        values.put("CorrectAns", CorrectAns);
        values.put("Category", Category);
        values.put("answeredFlag", 0);
        values.put("timeTaken", 0);
        values.put("Rank", 0);

        db.insert("EF_mob_DailyQues", null, values);

        if (db.isOpen()) {
            db.close();
        }
    }


    //Insert Daily Question details
    public void InsertDailyArticle(int ArticleNo, String ArticleDate, String Topic, String ArticleDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        //int answeredFlag ,int timeTaken ,int Rank
        ContentValues values = new ContentValues();

        values.put("ArticleNo", ArticleNo);
        values.put("ArticleDate", ArticleDate);
        values.put("Topic", Topic);
        values.put("ArticleDesc", ArticleDesc);

        db.insert("EF_mob_DailyArticle", null, values);

        if (db.isOpen()) {
            db.close();
        }
    }

    //Timer end
    public void InsertTimer(int Timervalue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Timervalue", Timervalue);

        db.insert("EF_mob_QuestionTimer", null, values);

        if (db.isOpen()) {
            db.close();
        }
    }

    public int getTimer() {
        int Timer = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  Timervalue FROM EF_mob_QuestionTimer";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _Timervalue = cursor.getColumnIndex("Timervalue");


            if (cursor.moveToFirst()) {
                Timer = cursor.getInt(_Timervalue);
            } else {
                Timer = 36;
            }
        } else {
            InsertTimer(36);
            Timer = 36;
        }

        return Timer;
    }

    public void updateTimer(int Timervalue) {
        SQLiteDatabase db = this.getWritableDatabase();


        String sql = "UPDATE EF_mob_QuestionTimer  SET Timervalue =" + Timervalue;
        db.execSQL(sql);


        if (db.isOpen()) {
            db.close();
        }
    }

    //Timer End

    public void deletemastertable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("EF_mob_MasterQuestion", null, null);
        if (db.isOpen()) {
            db.close();
        }
    }

    //Get Exam Dates
    public ArrayList<String> getDailyExamDate() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  distinct quesDate FROM EF_mob_DailyQues where quesDate < date('now') order by quesDate desc";
        Log.i("DateQuery",selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _Date = cursor.getColumnIndex("quesDate");


            if (cursor.moveToFirst()) {
                do {
                    String Date = cursor.getString(_Date);

                    list.add(Date);

                } while (cursor.moveToNext());


            }
        }
        return list;

    }


    //get questions
    public ArrayList<storequestiondetails> getQuestionDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<storequestiondetails> list = new ArrayList<storequestiondetails>();

        String selectQuery = "SELECT  Questionno, Question, Choice1, Choice2, Choice3, Choice4,Choice5, Correct_Ans,Category  FROM EF_mob_MasterQuestion where answered_flag = 0 order by Questionno asc";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _QuestionNo = cursor.getColumnIndex("Questionno");
            int _Question = cursor.getColumnIndex("Question");
            int _Choice1 = cursor.getColumnIndex("Choice1");
            int _Choice2 = cursor.getColumnIndex("Choice2");
            int _Choice3 = cursor.getColumnIndex("Choice3");
            int _Choice4 = cursor.getColumnIndex("Choice4");
            int _Choice5 = cursor.getColumnIndex("Choice5");
            int _Correct_Ans = cursor.getColumnIndex("Correct_ans");
            int _Category = cursor.getColumnIndex("Category");

            if (cursor.moveToFirst()) {
                do {
                    int QuestionNo = cursor.getInt(_QuestionNo);
                    String Question = cursor.getString(_Question);
                    String Choice1 = cursor.getString(_Choice1);
                    String Choice2 = cursor.getString(_Choice2);
                    String Choice3 = cursor.getString(_Choice3);
                    String Choice4 = cursor.getString(_Choice4);
                    String Choice5 = cursor.getString(_Choice5);
                    String Correct_Ans = cursor.getString(_Correct_Ans);
                    String Category = cursor.getString(_Category);
                    list.add(getdata(QuestionNo, Question, Choice1, Choice2, Choice3, Choice4, Choice5, Correct_Ans, Category));

                } while (cursor.moveToNext());


            }
        }
        return list;

    }

    //Get Exam Daily Questions---------------------------------------------------------------------------------

    public ArrayList<DailyExam> getDailyExamQuestion(String ExamDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DailyExam> list = new ArrayList<DailyExam>();

        String selectQuery = "SELECT id,quesDate ,QuesNo  ,Ques ,Choice1 ,Choice2 ,Choice3 ,Choice4 ,Choice5 ,CorrectAns ,Category   FROM EF_mob_DailyQues where quesDate = '" + ExamDate + "' order by QuesNo asc";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _id = cursor.getColumnIndex("id");
            int _QuesNo = cursor.getColumnIndex("QuesNo");
            int _quesDate = cursor.getColumnIndex("quesDate");
            int _Ques = cursor.getColumnIndex("Ques");
            int _Choice1 = cursor.getColumnIndex("Choice1");
            int _Choice2 = cursor.getColumnIndex("Choice2");
            int _Choice3 = cursor.getColumnIndex("Choice3");
            int _Choice4 = cursor.getColumnIndex("Choice4");
            int _Choice5 = cursor.getColumnIndex("Choice5");
            int _CorrectAns = cursor.getColumnIndex("CorrectAns");
            int _Category = cursor.getColumnIndex("Category");

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(_id);
                    int QuesNo = cursor.getInt(_QuesNo);
                    String quesDate = cursor.getString(_quesDate);
                    String Ques = cursor.getString(_Ques);
                    String Choice1 = cursor.getString(_Choice1);
                    String Choice2 = cursor.getString(_Choice2);
                    String Choice3 = cursor.getString(_Choice3);
                    String Choice4 = cursor.getString(_Choice4);
                    String Choice5 = cursor.getString(_Choice5);
                    int CorrectAns = cursor.getInt(_CorrectAns);
                    String Category = cursor.getString(_Category);

                    list.add(new DailyExam(id, QuesNo, Ques, Choice1, Choice2, Choice3, Choice4, Choice5, CorrectAns, Category));

                } while (cursor.moveToNext());

            }
        }
        return list;

    }


    //End Get exam Daily Question------------------------------------------------------------------------------


    //Start :- Get Daily Article Data---------------------------------------------------------------------------------
    public ArrayList<DailyArticleData> getDailyArticle(String ExamDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DailyArticleData> list = new ArrayList<DailyArticleData>();

        String selectQuery = "SELECT ArticleNo , ArticleDate ,Topic ,ArticleDesc FROM EF_mob_DailyArticle where ArticleDate = '" + ExamDate + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _ArticleNo = cursor.getColumnIndex("ArticleNo");
            int _ArticleDate = cursor.getColumnIndex("ArticleDate");
            int _Topic = cursor.getColumnIndex("Topic");
            int _ArticleDesc = cursor.getColumnIndex("ArticleDesc");

            if (cursor.moveToFirst()) {
                do {
                    int ArticleNo = cursor.getInt(_ArticleNo);
                    String ArticleDate = cursor.getString(_ArticleDate);
                    String Topic = cursor.getString(_Topic);
                    String ArticleDesc = cursor.getString(_ArticleDesc);


                    list.add(new DailyArticleData(ArticleNo, ArticleDate, Topic, ArticleDesc));

                } while (cursor.moveToNext());

            }
        }
        return list;

    }


    //End Get Daily Article------------------------------------------------------------------------------


    private storequestiondetails getdata(int QuestionNo, String Question, String Choice1, String Choice2, String Choice3, String Choice4, String Choice5, String Correct_Ans, String Category) {
        return new storequestiondetails(QuestionNo, Question, Choice1, Choice2, Choice3, Choice4, Choice5, Correct_Ans, Category);
    }

    //get total question count
    //get questions
    public int getQuestionCount() {
        int QuestionCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<storequestiondetails> list = new ArrayList<storequestiondetails>();

        String selectQuery = "SELECT  count(*) cnt  FROM EF_mob_MasterQuestion where Questionno is not null ";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _Questioncnt = cursor.getColumnIndex("cnt");


            if (cursor.moveToFirst()) {
                QuestionCount = cursor.getInt(_Questioncnt);
            } else {
                QuestionCount = 0;
            }
        }
        return QuestionCount;

    }

    // update score details
    public void updateScore(int questionnumber, int _answer_flag, int _timetaken, int FromScreen) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "";
        if (FromScreen == 0) {
            sql = "UPDATE EF_mob_MasterQuestion  SET answered_flag =" + _answer_flag + " , time_taken =" + _timetaken + " WHERE  Questionno   = '" + questionnumber + "'";
        } else if (FromScreen == 1) {
            sql = "UPDATE EF_mob_DailyQues  SET answeredFlag =" + _answer_flag + " , timeTaken =" + _timetaken + " WHERE  id   = '" + questionnumber + "'";
        }

        Log.i("Prasanna", sql);
        db.execSQL(sql);


        if (db.isOpen()) {
            db.close();
        }
    }


    // reset
    public void Resetquestion() {
        SQLiteDatabase db = this.getWritableDatabase();


        String sql = "UPDATE EF_mob_MasterQuestion  SET answered_flag =" + 0 + " , time_taken =" + 0;
        db.execSQL(sql);


        if (db.isOpen()) {
            db.close();
        }
    }

    //get score
    public String getscore(int FromScreen, String ExamDate) {
        int QuestionCount = 0;
        int correctanswer = 0;
        String selectQuery = null;
        SQLiteDatabase db = this.getWritableDatabase();

        if (FromScreen == 0) {
            QuestionCount = getattemptedcount(FromScreen, ExamDate);
            selectQuery = "SELECT count(*) cnt  FROM EF_mob_MasterQuestion where answered_flag = 1 ";
        } else {
            QuestionCount = getDailyExamCount(ExamDate);
            selectQuery = "SELECT count(*) cnt  FROM EF_mob_DailyQues where quesDate = '" + ExamDate + "' and answeredFlag = 1";
        }
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (null != cursor && cursor.moveToFirst()) {

            int _correctanswer = cursor.getColumnIndex("cnt");

            if (cursor.moveToFirst()) {
                correctanswer = cursor.getInt(_correctanswer);
            } else {
                correctanswer = 0;
            }
        }

        return String.valueOf(correctanswer) + '/' + String.valueOf(QuestionCount);

    }

    //get average
    public String getAverage(int FromScreen, String ExamDate) {
        int QuestionCount = 0;
        int average = 0;
        int sumtimetaken = 0;
        String selectQuery = "";
        SQLiteDatabase db = this.getWritableDatabase();

        QuestionCount = getattemptedcount(FromScreen, ExamDate);
        if (FromScreen == 0) {
            selectQuery = "SELECT   sum(" + getTimer() + "-time_taken) sum  FROM EF_mob_MasterQuestion where answered_flag  > 0";
        } else {
            selectQuery = "SELECT   sum(" + getTimer() + "-timeTaken) sum  FROM  EF_mob_DailyQues where quesDate = '" + ExamDate + "' and answeredFlag  > 0";
        }

        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _sumtimetaken = cursor.getColumnIndex("sum");


            if (cursor.moveToFirst()) {
                sumtimetaken = cursor.getInt(_sumtimetaken);
            } else {
                sumtimetaken = 0;
            }
        } else {
            Log.i("Timer", "no value");
        }

        if (sumtimetaken > 0 && QuestionCount > 0) {
            average = sumtimetaken / QuestionCount;
        }

        return String.valueOf(average);

    }


    public int getattemptedcount(int FromScreen, String ExamDate) {
        int QuestionCount = 0;
        String selectQuery = "";
        SQLiteDatabase db = this.getWritableDatabase();
        if (FromScreen == 0) {
            selectQuery = "SELECT count(*) cnt FROM EF_mob_MasterQuestion where answered_flag in (1,2)";
        } else {
            selectQuery = "SELECT count(*) cnt FROM EF_mob_DailyQues where quesDate = '" + ExamDate + "' and answeredFlag in (1,2)";
        }

        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _Questioncnt = cursor.getColumnIndex("cnt");
            if (cursor.moveToFirst()) {
                QuestionCount = cursor.getInt(_Questioncnt);
            } else {
                QuestionCount = 0;
            }
        } else {
            Log.i("Prasanna", "No attempted ques");

        }
        Log.i("Prasanna", selectQuery);
        return QuestionCount;
    }

    //DAily exam question count
    public int getDailyExamCount(String ExamDate) {
        int QuestionCount = 0;
        String selectQuery = "";
        SQLiteDatabase db = this.getWritableDatabase();

        selectQuery = "SELECT count(*) cnt FROM EF_mob_DailyQues where quesDate = '" + ExamDate + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _Questioncnt = cursor.getColumnIndex("cnt");
            if (cursor.moveToFirst()) {
                QuestionCount = cursor.getInt(_Questioncnt);
            } else {
                QuestionCount = 0;
            }
        } else {
            Log.i("Prasanna", "No attempted ques");

        }
        Log.i("Prasanna", selectQuery);
        return QuestionCount;
    }


    //get score
    public String getSkippedCount(int FromScreen, String ExamDate) {
        int skippedCount = 0;
        String selectQuery = "";
        SQLiteDatabase db = this.getWritableDatabase();
        if (FromScreen == 0) {
            selectQuery = "SELECT count(*) cnt  FROM EF_mob_MasterQuestion where answered_flag = 3 ";
        } else {
            selectQuery = "SELECT count(*) cnt  FROM EF_mob_DailyQues where quesDate = '" + ExamDate + "' and answeredFlag = 3 ";
        }
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (null != cursor && cursor.moveToFirst()) {

            int _correctanswer = cursor.getColumnIndex("cnt");

            if (cursor.moveToFirst()) {
                skippedCount = cursor.getInt(_correctanswer);
            } else {
                skippedCount = 0;
                Log.i("Prasanna", "No Skipped Count");
            }
        }

        Log.i("Prasanna", String.valueOf(skippedCount));

        return String.valueOf(skippedCount);

    }

    public int getmaxquestionnumber() {
        int LastQuestionNum = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT max(Questionno) maxi FROM EF_mob_MasterQuestion";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _Questioncnt = cursor.getColumnIndex("maxi");
            if (cursor.moveToFirst()) {
                LastQuestionNum = cursor.getInt(_Questioncnt);
            } else {
                LastQuestionNum = 0;
            }
        }

        return LastQuestionNum;
    }

    public int getMaxDailyQuestionNumber() {
        int LastQuestionNum = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT max(id) maxi FROM EF_mob_DailyQues";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _Questioncnt = cursor.getColumnIndex("maxi");
            if (cursor.moveToFirst()) {
                LastQuestionNum = cursor.getInt(_Questioncnt);
            } else {
                LastQuestionNum = 0;
            }
        }

        return LastQuestionNum;
    }

    public int getMaxDailyArticle() {
        int LastQuestionNum = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT max(ArticleNo) maxi FROM EF_mob_DailyArticle";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _Questioncnt = cursor.getColumnIndex("maxi");
            if (cursor.moveToFirst()) {
                LastQuestionNum = cursor.getInt(_Questioncnt);
            } else {
                LastQuestionNum = 0;
            }
        }

        return LastQuestionNum;
    }

    public String getCategoryscore(String category, int FromScreen, String ExamDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        int totalquestion = 0;
        int answeredcnt = 0;
        String selectQuery = "";

        if (FromScreen == 0) {
            selectQuery = "SELECT  count(*) cnt  FROM EF_mob_MasterQuestion where category = '" + category + "'  and  answered_flag in (1,2,3) ";
        } else {
            selectQuery = "SELECT  count(*) cnt  FROM EF_mob_DailyQues where Category = '" + category + "' and quesDate = '" + ExamDate + "'  and  answeredFlag in (1,2,3) ";
        }

        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _totalquestion = cursor.getColumnIndex("cnt");


            if (cursor.moveToFirst()) {
                totalquestion = cursor.getInt(_totalquestion);
            } else {
                totalquestion = 0;
            }

        }

        if (FromScreen == 0) {
            selectQuery = "SELECT  count(*) cnt  FROM EF_mob_MasterQuestion where category='" + category + "' and  answered_flag in (1)";
        } else {
            selectQuery = "SELECT  count(*) cnt  FROM EF_mob_DailyQues where Category = '" + category + "' and quesDate = '" + ExamDate + "'  and  answeredFlag in (1) ";
        }

        cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _answeredcnt = cursor.getColumnIndex("cnt");


            if (cursor.moveToFirst()) {
                answeredcnt = cursor.getInt(_answeredcnt);
            } else {
                answeredcnt = 0;
            }

        }
        return String.valueOf(answeredcnt) + " Out of " + String.valueOf(totalquestion);
    }

    // Getting the chat from table of the user
    public List<notificationtable> Getnotitification() {
        List<notificationtable> list = new ArrayList<notificationtable>();
        try {
            int success;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            Log.i("News", "came in");
            JSONObject json = jsonParser.makeHttpRequest(
                    masterdetails.getnews, "GET", params);

            if (json.length() > 0) {
                // json success tag
                success = json.getInt("success");
                if (success == 1) {
                    // successfully received product details
                    JSONArray objNews = json.getJSONArray("News"); // JSON
                    // Array
                    for (int i = 0; i < objNews.length(); i++) {

                        String news_text = "";
                        String timestamp = "";
                        String newsid = "";


                        JSONObject md = objNews.getJSONObject(i);

                        newsid = md.getString("news_id");
                        news_text = md.getString("news_text");
                        timestamp = md.getString("timestamp");

                        Log.i("News", news_text);
                        Log.i("News", timestamp);


                        list.add(getnot(news_text, timestamp,
                                newsid));


                        //End of getting question details
                    }
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
            Log.i("exception", e.getMessage());
        }
        return list;
    }

    private notificationtable getnot(String message, String timestamp,
                                     String notificationid) {
        return new notificationtable(notificationid, message, timestamp);
    }


}
