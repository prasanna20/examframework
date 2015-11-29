package ExamFramework_AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fyshadows.examframework.examframework.Exam_database;
import com.fyshadows.examframework.examframework.JSONParser;
import com.fyshadows.examframework.examframework.masterdetails;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Data.ChatRoomData;

/**
 * Created by Prasanna on 14-11-2015.
 */

public class AsyncUpdateChatRoom extends AsyncTask<String, Void, String> {

    JSONParser jsonParser = new JSONParser();
    Exam_database db;
    private Context myCtx;

    public AsyncUpdateChatRoom(Context ctx) {
        // Now set context
        this.myCtx = ctx;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... Values) {
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
        return null;
    }
}

