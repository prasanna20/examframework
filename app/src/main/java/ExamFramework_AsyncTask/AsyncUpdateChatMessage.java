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

/**
 * Created by Prasanna on 14-11-2015.
 */

public class AsyncUpdateChatMessage extends AsyncTask<String, Void, String> {

    JSONParser jsonParser = new JSONParser();
    Exam_database db;
    private Context myCtx;

    public AsyncUpdateChatMessage(Context ctx) {
        // Now set context
        this.myCtx = ctx;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... Values) {
        try {
            Log.i("Async", "Executing get chat Message");

            db = new Exam_database(this.myCtx);
            int LastId=0;

            LastId=db.getMaxChatMessage();

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.clear();
            params.add(new BasicNameValuePair("LastId", String.valueOf(LastId)));

            JSONObject json = jsonParser.makeHttpRequest(
                    masterdetails.getChatMessage, "GET", params);
            int success = 0;
            JSONArray objChatMessage;
            ChatData chatData;
            Log.i("chatMessageinsert","Value"+ String.valueOf(json.length()) );
            if (json.length() > 0) {
                success = json.getInt("success");
                Log.i("chatMessageinsert","success"+ String.valueOf(success) );
                if (success == 1) {

                    objChatMessage = json.getJSONArray("MasterChatMessage"); // JSON

                    Log.i("chatMessageinsert","objChatMessage"+ String.valueOf(objChatMessage.length()) );

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

                            db.InsertChatMessage(chatData);
                        }

                }

            }
        } catch (JSONException e) {

            Log.i("Analysis activity", "Error");
        }
        return null;
    }
}

