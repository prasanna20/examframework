package ExamFramework_AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fyshadows.examframework.examframework.Exam_database;
import com.fyshadows.examframework.examframework.JSONParser;
import com.fyshadows.examframework.examframework.masterdetails;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Data.ChatData;

/**
 * Created by Prasanna on 14-11-2015.
 */


public class AsyncSendChatMessage extends AsyncTask<String, Void, String> {

    JSONParser jsonParser = new JSONParser();
    Exam_database db;
    private Context myCtx;


    public AsyncSendChatMessage(Context ctx) {
        // Now set context
        this.myCtx = ctx;
    }


    @Override
    protected void onPreExecute() {

    }


    @Override
    protected String doInBackground(String... Values) {
        try {

            db = new Exam_database(this.myCtx);

            Log.i("Async", "Executing Aync task send message");
            int RoomId = Integer.valueOf(Values[0]);
            String ChatMessage = Values[1];
            String Email = db.GetEmailDetails(this.myCtx);
            String UserName = db.getUserName();

            Log.i("RoomId", String.valueOf(RoomId));
            Log.i("ChatMessage", ChatMessage);
            Log.i("Email", Email);

            if (UserName == null) {
                int End = Email.indexOf("@");
                UserName = Email.substring(0, End);
            }

            Log.i("UserName", UserName);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.clear();
            params.add(new BasicNameValuePair("RoomId", String.valueOf(RoomId)));
            params.add(new BasicNameValuePair("ChatMessage", ChatMessage));
            params.add(new BasicNameValuePair("Email", Email));
            params.add(new BasicNameValuePair("UserName", UserName));

            JSONObject json = jsonParser.makeHttpRequest(
                    masterdetails.sendChatMessage, "GET", params);

            if (json != null) {
                if (json.length() > 0) {
                    // json success tag
                    int success = json.getInt("success");
                    if (success == 1) {

                        Log.i("InsertChat", String.valueOf(json.getInt("Id")));

                        ChatData chatData = new ChatData();
                        chatData.setChatMessage(ChatMessage);
                        chatData.setEmail(Email);
                        chatData.setid(json.getInt("Id"));
                        chatData.setRoomId(RoomId);
                        chatData.setTimeStamp(json.getString("TimeStamp"));
                        chatData.setUsername(UserName);

                        db.InsertChatMessage(chatData);
                    }
                }
            }
        } catch (Exception e) {

            Log.i("Analysis activity", "Error");
        }
        return null;
    }

}