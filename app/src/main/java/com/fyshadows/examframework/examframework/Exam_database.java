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
import java.util.List;

import ExamFramework_AsyncTask.AsyncUpdateUserName;
import ExamFramework_Data.ChatData;
import ExamFramework_Data.ChatRoomData;
import ExamFramework_Data.DailyArticleData;
import ExamFramework_Data.DailyExam;
import ExamFramework_Data.notificationtable;

/**
 * Created by Prasanna on 16-03-2015.
 */

public class Exam_database extends SQLiteOpenHelper {

    JSONParser jsonParser = new JSONParser();
    private Context myCtx;

    public Exam_database(Context context) {

        super(context, "exam_database", null, 4);// version number is given at
        myCtx = context;
    }

    private boolean doesDatabaseExist(ContextWrapper context) {
        File dbFile = context.getDatabasePath("exam_database");
        return dbFile.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase myDB) {

        myDB.execSQL("CREATE TABLE if not exists EF_mob_UserDetails(mail_id varchar(250),GCM_registration_id TEXT,username  varchar(250) );");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_MasterQuestion(Questionno int,Question text,Choice1 varchar(350),Choice2 varchar(350),Choice3 varchar(350),Choice4 varchar(350),Choice5 varchar(350),Correct_ans varchar(350),Category,answered_flag int,time_taken int);");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_QuestionTimer(Timervalue int);");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_DailyQues(id  int,quesDate date,QuesNo int ,Ques text,Choice1 varchar(350),Choice2 varchar(350),Choice3 varchar(350),Choice4 varchar(350),Choice5 varchar(350),CorrectAns varchar(350),Category varchar(250),answeredFlag int,timeTaken int,Rank int);");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_DailyArticle(ArticleNo int, ArticleDate Date,Topic varchar(300),ArticleDesc text);");

        myDB.execSQL("CREATE TABLE if not exists EF_mob_ChatRoom(id int, RoomName varchar(300),Description varchar(750), CreatedBy  varchar(300),ChatCount int,NotificationEnabled int,favourite int);");

        myDB.execSQL("CREATE TABLE if not exists Exam_Chat(id int, RoomId int,username varchar(250), mail_id varchar(250),ChatMessage text,TimeStamp timestamp);");

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

        myDB.execSQL("CREATE TABLE if not exists EF_mob_ChatRoom(id int, RoomName varchar(300),CreatedBy  varchar(300),Description varchar(750),ChatCount int,NotificationEnabled int,favourite int);");

        myDB.execSQL("ALTER TABLE EF_mob_UserDetails ADD COLUMN username  varchar(250);");

        myDB.execSQL("CREATE TABLE if not exists Exam_Chat(id int, RoomId int,username varchar(250), Email varchar(250),ChatMessage text,TimeStamp timestamp);");

        Log.i("table upgraded", "table upgraded");
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


    public String GetEmailDetails(Context context) {

        String email="";
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT mail_id  FROM EF_mob_UserDetails";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (null != cursor && cursor.moveToFirst()) {

            int _mail_id = cursor.getColumnIndex("mail_id");

            if (cursor.moveToFirst()) {
                email = cursor.getString(_mail_id);
            }
        } else {
            email = GCMIntentService.getEmail(context);
        }

        return email;
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
        ContentValues values = new ContentValues();

        if(QuesNo != 0 &&  CorrectAns != 0) {

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


        }
        if (db.isOpen()) {
            db.close();
        }
    }


    //Insert Daily Question details
    public void InsertDailyArticle(int ArticleNo, String ArticleDate, String Topic, String ArticleDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
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
        Log.i("Lastquestion",String.valueOf(LastQuestionNum));
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

    //Get Chat Room Details
    public ArrayList<ChatRoomData> getChatRoomDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<ChatRoomData> list = new ArrayList<ChatRoomData>();
        String selectQuery = "SELECT  id,RoomName ,CreatedBy ,ChatCount,NotificationEnabled ,favourite,Description FROM EF_mob_ChatRoom ORDER BY favourite DESC,NotificationEnabled  DESC,ChatCount DESC,id DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _id = cursor.getColumnIndex("id");
            int _RoomName = cursor.getColumnIndex("RoomName");
            int _CreatedBy = cursor.getColumnIndex("CreatedBy");
            int _ChatCount = cursor.getColumnIndex("ChatCount");
            int _NotificationEnabled = cursor.getColumnIndex("NotificationEnabled");
            int _FavEnabled = cursor.getColumnIndex("favourite");
            int _Description = cursor.getColumnIndex("Description");


            if (cursor.moveToFirst()) {
                do {
                    int id=cursor.getInt(_id);
                    String RoomName = cursor.getString(_RoomName);
                    String CreatedBy = cursor.getString(_CreatedBy);
                    int ChatCount = cursor.getInt(_ChatCount);
                    int NotificationEnabled = cursor.getInt(_NotificationEnabled);
                    int FavEnabled = cursor.getInt(_FavEnabled);
                    String Description=cursor.getString(_Description);

                    list.add(new ChatRoomData( id,  ChatCount,  NotificationEnabled,  RoomName,  CreatedBy, FavEnabled,Description));

                } while (cursor.moveToNext());

            }
        }
        return list;

    }

    //To Update User Name
    public void updateUserName(String UserName) {
        SQLiteDatabase db = this.getWritableDatabase();


        String sql = "UPDATE EF_mob_UserDetails  SET username ='" + UserName +"'";
        db.execSQL(sql);


        new AsyncUpdateUserName(this.myCtx).execute(UserName);


        if (db.isOpen()) {
            db.close();
        }
    }

    //To get user Name

    public String getUserName() {
        SQLiteDatabase db = this.getWritableDatabase();
        String UserName = "";
        String selectQuery = "SELECT  username FROM EF_mob_UserDetails";

        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _username = cursor.getColumnIndex("username");

            if (cursor.moveToFirst()) {

                     UserName=cursor.getString(_username);

            }
        }
        return UserName;

    }

//Insert chat room details
public void InsertChatRoomDetails(ChatRoomData chatRoomData) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    Log.i("chatinsert",chatRoomData.getRoomName());
    String CreatedBy="";
    if(chatRoomData.getCreatedBy() == null)
    {
        CreatedBy = "";
    }
    else
    {
        CreatedBy = chatRoomData.getCreatedBy();
    }
    values.put("id", chatRoomData.getid());
    values.put("RoomName", chatRoomData.getRoomName());
    values.put("Description", chatRoomData.getDescription());
    values.put("CreatedBy", CreatedBy);
    values.put("ChatCount", chatRoomData.getChatCount());
    values.put("NotificationEnabled", chatRoomData.getNotificationEnabled());
    values.put("favourite", chatRoomData.getFavEnabled());

    db.insert("EF_mob_ChatRoom", null, values);

    if (db.isOpen()) {
        db.close();
    }
}

    //To Update Toggle notification
    public void updateToggleNotification(int Id,int Flag) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE EF_mob_ChatRoom  SET NotificationEnabled ='" + Flag +"' " +  " where id ='" + Id +"'";
        Log.i("sql-noti", sql);
        db.execSQL(sql);

        if (db.isOpen()) {
            db.close();
        }
    }

    //To Update Toggle Favourite
    public void updateToggleFavourite(int Id,int Flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE EF_mob_ChatRoom  SET favourite ='" + Flag +"' " +  " where id ='" + Id +"'";
        Log.i("sql-fav",sql);
        db.execSQL(sql);

        if (db.isOpen()) {
            db.close();
        }
    }

    //Check whether the room id is present
    public Boolean checkChatRoomId(int Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String UserName = "";
        Boolean Output = false ;
        String selectQuery = "SELECT  id FROM EF_mob_ChatRoom "  +  " where id ='" + Id +"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (null != cursor && cursor.moveToFirst()) {
            Output= true;
        }
        else
        {
            Output= false;
        }
        return Output;

    }


    //To Update Toggle notification
    public void updateChatRoomDetails(ChatRoomData chatRoomData) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE EF_mob_ChatRoom  SET RoomName ='" + chatRoomData.getRoomName() +"', Description ='" + chatRoomData.getDescription() +"',  ChatCount ='" + chatRoomData.getChatCount() +"' " +  " where id ='" + chatRoomData.getid() +"'";
        Log.i("sql-UpadateChatRoom", sql);
        db.execSQL(sql);
        if (db.isOpen()) {
            db.close();
        }
    }

    //Delete the chat room
    public void deleteChatRoom(int RoomId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("EF_mob_ChatRoom", "id  = " + RoomId, null);
        db.close();
        if (db.isOpen()) {
            db.close();
        }
    }

    //Get Chat Message
    public ArrayList<ChatData> getChatMessage(int RoomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<ChatData> list = new ArrayList<ChatData>();
        String selectQuery = "SELECT  Ec.id , Ec.id RoomId,Ecr.RoomName ,Ec.username , Ec.mail_id ,Ec.ChatMessage ,Ec.TimeStamp  FROM Exam_Chat Ec left outer join EF_mob_ChatRoom Ecr on Ec.RoomId=Ecr.id where RoomId = " + RoomId + " ORDER BY TimeStamp asc";

        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _id = cursor.getColumnIndex("id");
            int _RoomName = cursor.getColumnIndex("RoomName");
            int _RoomId = cursor.getColumnIndex("RoomId");
            int _username = cursor.getColumnIndex("username");
            int _Email = cursor.getColumnIndex("mail_id");
            int _ChatMessage= cursor.getColumnIndex("ChatMessage");
            int _TimeStamp = cursor.getColumnIndex("TimeStamp");

            if (cursor.moveToFirst()) {
                do {
                    int id=cursor.getInt(_id);
                    int RoomIdd = cursor.getInt(_RoomId);
                    String RoomName = cursor.getString(_RoomName);
                    String username = cursor.getString(_username);
                    String Email = cursor.getString(_Email);
                    String ChatMessage = cursor.getString(_ChatMessage);
                    String TimeStamp=cursor.getString(_TimeStamp);
                    list.add(new ChatData( id,  RoomIdd, RoomName,   username,  Email,  ChatMessage, TimeStamp));
                } while (cursor.moveToNext());

            }
        }
        return list;

    }

    //Get last message id
    public int getMaxChatMessage() {
        int LastMessageId = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT max(id) maxi FROM Exam_Chat";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (null != cursor && cursor.moveToFirst()) {

            int _LastMessageId = cursor.getColumnIndex("maxi");
            if (cursor.moveToFirst()) {
                LastMessageId = cursor.getInt(_LastMessageId);
            } else {
                LastMessageId = 0;
            }
        }

        return LastMessageId;
    }

    //Insert chat Message details
    public void InsertChatMessage(ChatData chatData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Log.i("dbchatinsert", chatData.getChatMessage());

        values.put("id", chatData.getid());
        values.put("RoomId", chatData.getRoomId());
        values.put("username", chatData.getUsername());
        values.put("mail_id", chatData.getEmail());
        values.put("ChatMessage", chatData.getChatMessage());
        values.put("TimeStamp", chatData.getTimeStamp());

        db.insert("Exam_Chat", null, values);

        if (db.isOpen()) {
            db.close();
        }
    }


}
