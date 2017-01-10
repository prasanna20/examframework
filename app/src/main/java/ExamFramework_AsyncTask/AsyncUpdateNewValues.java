package ExamFramework_AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fyshadows.examframework.examframework.Exam_database;
import com.fyshadows.examframework.examframework.JSONParser;
import com.fyshadows.examframework.examframework.masterdetails;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Data.ChatData;
import ExamFramework_Data.ChatRoomData;

/**
 * Created by Prasanna on 19-11-2015.
 */

    //to check for updates in questions
    public class AsyncUpdateNewValues extends AsyncTask<String, Void, String> {
    JSONParser jsonParser = new JSONParser();
    Exam_database db;
    private Context myCtx;

    public AsyncUpdateNewValues(Context ctx) {
        // Now set context
        this.myCtx = ctx;
    }
        int MainDBLastQuestion;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                db = new Exam_database(this.myCtx);
                // To get question details
                int success;
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject md;
                JSONArray QuestionObj;

                JSONObject json = jsonParser.makeHttpRequest(
                        masterdetails.checkForupdate, "GET", params);
                Log.i("Exam", "got json");
                if(json != null) {
                    if (json.length() > 0) {
                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {

                            // successfully received product details
                             QuestionObj = json.getJSONArray("LastQuestion"); // JSON
                            // Array
                             md = QuestionObj.getJSONObject(0);


                            MainDBLastQuestion = md.getInt("maxQuestionNo");
                            if (MainDBLastQuestion > db.getmaxquestionnumber()) {
                                Log.i("exam", "we have new set of question");
                                try {

                                    // To get question details
                                    params.clear();
                                    params.add(new BasicNameValuePair("lastquestionnumber", String.valueOf(db.getmaxquestionnumber())));

                                    json = jsonParser.makeHttpRequest(
                                            masterdetails.getNewQuestions, "GET", params);
                                    Log.i("Exam", "got json");
                                    if (json.length() > 0) {
                                        // json success tag
                                        success = json.getInt("success");
                                        if (success == 1) {
                                            Log.i("Exam", "Check_suc");
                                            // successfully received product details
                                            QuestionObj = json.getJSONArray("MasterQuestion"); // JSON
                                            // Array
                                            for (int i = 0; i < QuestionObj.length(); i++) {

                                                int QuestionNo = 0;
                                                String Question = "";
                                                String Choice1 = "";
                                                String Choice2 = "";
                                                String Choice3 = "";
                                                String Choice4 = "";
                                                String Choice5 = "";
                                                int Correct_Ans = 0;
                                                int Category = 0;

                                                md = QuestionObj.getJSONObject(i);

                                                QuestionNo = md.getInt("QuestionNo");
                                                Question = md.getString("Question");
                                                Choice1 = md.getString("Choice1");
                                                Choice2 = md.getString("Choice2");
                                                Choice3 = md.getString("Choice3");
                                                Choice4 = md.getString("Choice4");
                                                Choice5 = md.getString("Choice5");
                                                Correct_Ans = md.getInt("Correct_Ans");
                                                Category = md.getInt("Category");


                                                if (Question.trim() == "") {
                                                    Question = "NA";
                                                }
                                                if (Choice1.trim() == "") {
                                                    Choice1 = "NA";
                                                }
                                                if (Choice2.trim() == "") {
                                                    Choice2 = "NA";
                                                }
                                                if (Choice3.trim() == "") {
                                                    Choice3 = "NA";
                                                }
                                                if (Choice4.trim() == "") {
                                                    Choice4 = "NA";
                                                }
                                                if (Choice5.trim() == "") {
                                                    Choice5 = "NA";
                                                }

                                                db.InsertQuestionDetails(QuestionNo, Question, Choice1, Choice2, Choice3, Choice4, Choice5, Correct_Ans, Category);

                                                //End of getting question details
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    Log.i("exam", "we have not got any new set of question");
                                }
                                //download new question to database ends
                            } else {
                                Log.i("exam", "we have not got any new set of question");
                                //download new questions to database start

                            }
                        }
                    }
                }

                            //End of getting question detail
                            // //Start :  Get Daily Question-----------------------------------------------------------------------
                            params.clear();
                            params.add(new BasicNameValuePair("lastquestionnumber", String.valueOf(db.getMaxDailyQuestionNumber())));

                            json = jsonParser.makeHttpRequest(
                                    masterdetails.getDailyTestQuestions, "GET", params);
                            Log.i("maximum Exam Daily", String.valueOf(db.getMaxDailyQuestionNumber()));
                            if(json != null) {
                                if (json.length() > 0) {
                                    // json success tag
                                    success = json.getInt("success");
                                    if (success == 1) {
                                        Log.i("Exam", "Check_suc");
                                        // successfully received product details
                                        QuestionObj = json.getJSONArray("MasterDailyQuestion"); // JSON
                                        // Array
                                        for (int i = 0; i < QuestionObj.length(); i++) {
                                            md = QuestionObj.getJSONObject(i);

                                            db.InsertDailyQuestionDetails(md.getInt("Id"), md.getString("ExamDate"), md.getInt("QuestionNo"), md.getString("QuestionText"), md.getString("Choice1"), md.getString("Choice2"), md.getString("Choice3"), md.getString("Choice4"), md.getString("Choice5"), md.getInt("Answer"), md.getString("Category"));

                                            //End of getting question details
                                        }
                                    }
                                }
                            }
//End  : Get Daily Question--------------------------------------------------------------------------

                            //Start :  Get Daily Article details-----------------------------------------------------------------------
                            params.clear();
                            params.add(new BasicNameValuePair("lastArticleNumber", String.valueOf(db.getMaxDailyArticle())));

                            json = jsonParser.makeHttpRequest(
                                    masterdetails.getDailyArticle, "GET", params);
                            Log.i("Maximum Exam Article", String.valueOf(db.getMaxDailyArticle()));
                            if(json != null)
                            {
                            if (json.length() > 0) {
                                // json success tag
                                success = json.getInt("success");
                                if (success == 1) {
                                    // successfully received product details
                                    QuestionObj = json.getJSONArray("MasterDailyArticle"); // JSON
                                    // Array
                                    for (int i = 0; i < QuestionObj.length(); i++) {
                                        md = QuestionObj.getJSONObject(i);

                                        db.InsertDailyArticle(md.getInt("ArticleNo"), md.getString("ArticleDate"), md.getString("Topic"), md.getString("ArticleDesc"));

                                        //End of getting Article details
                                    }
                                }
                            }
                        }
//End  : Get Daily Question--------------------------------------------------------------------------

                //Start :  Get Monthly Question-----------------------------------------------------------------------
                params.clear();
                params.add(new BasicNameValuePair("lastquestionnumber", String.valueOf(db.getMaxMonthlyQuestionNumber())));

                json = jsonParser.makeHttpRequest(
                        masterdetails.getMonthlyTestQuestions, "GET", params);
                Log.i("Exam", "got Daily Ques Json");
                if (json.length() > 0) {
                    // json success tag
                    success = json.getInt("success");
                    if (success == 1) {
                        Log.i("Exam", "Check_suc");
                        // successfully received product details
                         QuestionObj = json.getJSONArray("MasterDailyQuestion"); // JSON
                        // Array
                        for (int i = 0; i < QuestionObj.length(); i++) {
                             md = QuestionObj.getJSONObject(i);

                            db.InsertMonthlyQuestionDetails(md.getInt("Id"), md.getString("Month"), md.getInt("QuestionNo"),md.getString("QuestionText"), md.getString("Choice1"), md.getString("Choice2"),md.getString("Choice3"),md.getString("Choice4"),md.getString("Choice5") ,md.getInt("Answer"),md.getString("Category") );

                            //End of getting question details
                        }
                    }
                }
//End  : Get Monthly Question--------------------------------------------------------------------------


            } catch (JSONException e) {

                e.printStackTrace();
            }

            //Update Chat Room-----------------------
            try {
                Log.i("Async", "Executing get chat room");

                db = new Exam_database(this.myCtx);

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.clear();

                JSONObject json = jsonParser.makeHttpRequest(
                        masterdetails.getChatRoom, "GET", params);
                int success = 0;
                JSONArray objChatRoom;
                ChatRoomData chatRoomData;
                if(json != null) {
                    if (json.length() > 0) {
                        // json success tag
                        success = json.getInt("success");
                        Log.i("chatinsert", "success" + String.valueOf(success));
                        if (success == 1) {

                            objChatRoom = json.getJSONArray("MasterChatRoom"); // JSON

                            Log.i("chatinsert", "objChatRoom" + String.valueOf(objChatRoom.length()));

                            for (int i = 0; i < objChatRoom.length(); i++) {


                                JSONObject obj = objChatRoom.getJSONObject(i);
                                Log.i("chatinsert", "RoomName" + String.valueOf(obj.getString("RoomName")));
                                Log.i("chatinsert", "Id" + String.valueOf(obj.getInt("Id")));

                                chatRoomData = new ChatRoomData();
                                chatRoomData.setRoomName(obj.getString("RoomName"));
                                chatRoomData.setDescription(obj.getString("Description"));
                                chatRoomData.setCreatedBy(obj.getString("CreatedBy"));
                                chatRoomData.setid(obj.getInt("Id"));
                                chatRoomData.setChatCount(obj.getInt("ChatCount"));

                                if (chatRoomData.getDescription().trim() == "") {
                                    chatRoomData.setDescription("No Description available");
                                }

                                if (db.checkChatRoomId(obj.getInt("Id"))) {
                                    db.updateChatRoomDetails(chatRoomData);
                                } else {
                                    chatRoomData.setNotificationEnabled(0);
                                    chatRoomData.setFavEnabled(0);
                                    db.InsertChatRoomDetails(chatRoomData);
                                }
                            }
                        }

                    }
                }
            } catch (JSONException e) {

                Log.i("Analysis activity", "Error");
            }

            //------------------Update New chat Messages
            try {
                Log.i("Async", "Executing get chat Message");

                db = new Exam_database(this.myCtx);
                int LastId = 0;

                LastId = db.getMaxChatMessage();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                Log.i("Async", "Executing get chat Message"+String.valueOf(LastId));
                params.clear();
                params.add(new BasicNameValuePair("LastId", String.valueOf(LastId)));

                JSONObject json = jsonParser.makeHttpRequest(
                        masterdetails.getChatMessage, "GET", params);
                int success = 0;
                JSONArray objChatMessage;
                ChatData chatData;
                if(json != null)
                {
                    if (json.length() > 0) {
                        success = json.getInt("success");
                        Log.i("chatMessageinsert", "success" + String.valueOf(success));
                        if (success == 1) {

                            objChatMessage = json.getJSONArray("MasterChatMessage"); // JSON

                            Log.i("chatMessageinsert", "objChatMessage" + String.valueOf(objChatMessage.length()));

                            for (int i = 0; i < objChatMessage.length(); i++) {

                                JSONObject obj = objChatMessage.getJSONObject(i);
                                Log.i("chatMessageinsert", "ChatMessage" + String.valueOf(obj.getString("ChatMessage")));

                                chatData = new ChatData();
                                chatData.setChatMessage(obj.getString("ChatMessage"));
                                chatData.setEmail(obj.getString("Email"));
                                chatData.setid(obj.getInt("id"));
                                chatData.setRoomId(obj.getInt("RoomId"));
                                chatData.setTimeStamp(obj.getString("TimeStamp"));
                                chatData.setUsername(obj.getString("UserName"));

                                if(!obj.getString("Email").trim().equalsIgnoreCase(db.GetEmailDetails(myCtx).trim()))
                                {
                                    db.InsertChatMessage(chatData);
                                }

                            }

                        }

                    }
                }
            } catch (JSONException e) {

                Log.i("Analysis activity", "Error");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }

