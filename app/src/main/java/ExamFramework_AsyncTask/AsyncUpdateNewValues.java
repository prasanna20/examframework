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

                JSONObject json = jsonParser.makeHttpRequest(
                        masterdetails.checkForupdate, "GET", params);
                Log.i("Exam", "got json");
                if(json != null) {
                    if (json.length() > 0) {
                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {

                            // successfully received product details
                            JSONArray QuestionObj = json.getJSONArray("LastQuestion"); // JSON
                            // Array
                            JSONObject md = QuestionObj.getJSONObject(0);


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
                                                String Correct_Ans = "";
                                                String Category = "";


                                                md = QuestionObj.getJSONObject(i);

                                                QuestionNo = md.getInt("QuestionNo");
                                                Question = md.getString("Question");
                                                Choice1 = md.getString("Choice1");
                                                Choice2 = md.getString("Choice2");
                                                Choice3 = md.getString("Choice3");
                                                Choice4 = md.getString("Choice4");
                                                Choice5 = md.getString("Choice5");
                                                Correct_Ans = md.getString("Correct_Ans");
                                                Category = md.getString("Category");


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
                                                if (Correct_Ans.trim() == "") {
                                                    Correct_Ans = "NA";
                                                }
                                                if (Category.trim() == "") {
                                                    Category = "NA";
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

                            //End of getting question detail
                            // //Start :  Get Daily Question-----------------------------------------------------------------------
                            params.clear();
                            params.add(new BasicNameValuePair("lastquestionnumber", String.valueOf(db.getMaxDailyQuestionNumber())));

                            json = jsonParser.makeHttpRequest(
                                    masterdetails.getDailyTestQuestions, "GET", params);
                            Log.i("maximum Exam Daily", String.valueOf(db.getMaxDailyQuestionNumber()));
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
//End  : Get Daily Question--------------------------------------------------------------------------

                            //Start :  Get Daily Article details-----------------------------------------------------------------------
                            params.clear();
                            params.add(new BasicNameValuePair("lastArticleNumber", String.valueOf(db.getMaxDailyArticle())));

                            json = jsonParser.makeHttpRequest(
                                    masterdetails.getDailyArticle, "GET", params);
                            Log.i("Maximum Exam Article", String.valueOf(db.getMaxDailyArticle()));
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
//End  : Get Daily Question--------------------------------------------------------------------------s

                        }
                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }

