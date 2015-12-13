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
    public static Boolean isHandlerRunning = false;

    public AsyncUpdateChatMessage(Context ctx) {
        // Now set context
        this.myCtx = ctx;
    }

    @Override
    protected void onPreExecute() {
        isHandlerRunning=true;
    }

    @Override
    protected String doInBackground(String... Values) {
        try {
            Log.i("Async", "Executing get chat Message");

            db = new Exam_database(this.myCtx);
            int LastId = 0;

            LastId = db.getMaxChatMessage();

            List<NameValuePair> params = new ArrayList<NameValuePair>();

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

                    // Start : Generate Notification
                  //  String MessageTitle = "New message";
                  //  String MessageText = "You have un-read messages.";
                  //  Boolean isNotificationEnabled = false;

//                    GCMIntentService.generateNotification(myCtx, MessageTitle, MessageText);
                    // End  : Generate Notification

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

                      /*//Start : Check is notification enabled
                        if(!isNotificationEnabled) {

                                isNotificationEnabled = db.checkNotificationEnabledForChatRoom(obj.getInt("RoomId"));

                        }
                        //End : Check is notification enabled

                        if(objChatMessage.length() == i-1 )
                        {
                            if(isNotificationEnabled) {
                                GCMIntentService.generateNotification(myCtx, MessageTitle, MessageText);
                            }
                        }*/
                    }

                }

            }
        }
        } catch (JSONException e) {
            isHandlerRunning=false;
            Log.i("Analysis activity", "Error");
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        isHandlerRunning=false;
         }
}

