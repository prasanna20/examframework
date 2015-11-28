package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yyxqsg.bsyduo229750.Main;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Adapter.ChatRoomAdapter;
import ExamFramework_Data.ChatRoomData;


public class ChatRoom  extends ListActivity {
    private ListView listView;
    ChatRoomAdapter adapter;
    private List<String> ExamData;
    Exam_database db = new Exam_database(this);
    List<ChatRoomData> list = new ArrayList<ChatRoomData>();
    int scrolly = 0;
    int first = 0;
    TextView txtNoChatRoom;
    EditText editText_UserName;
    Button btnSave;
    Button btnAddRoom;
    ImageButton btnAdd;
    EditText editText_RoomName;
    EditText editText_RoomDesc;
    JSONParser jsonParser = new JSONParser();
    RelativeLayout AddLayout;
    final Handler handler = new Handler();
    Boolean isHandlerRunning = false;
    private Main main;
    private com.yyxqsg.bsyduo229750.AdView adView;
    String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.actionbar_chatroom);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Start : Insert Analysis
        masterdetails masterdet=new masterdetails(this);
        masterdet.insertAnalysis(this, 9);
        //End : Insert Analysis

        txtNoChatRoom=(TextView) this.findViewById(R.id.txtNoChatRoom);

        ListView myListView = (ListView) findViewById(android.R.id.list);
        list = db.getChatRoomDetails();
        int index = 0;
        int top = 0;

       if (!list.isEmpty()) {
            adapter = new ChatRoomAdapter(this, list);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (first == 0) {
                myListView.setSelection(scrolly);
            }

            if (first > 0) {
                myListView.setSelectionFromTop(index, top);
            }

            txtNoChatRoom.setVisibility(View.INVISIBLE);

        }
        else
        {
            txtNoChatRoom.setVisibility(View.VISIBLE);
        }

        editText_UserName= (EditText) findViewById(R.id.editText_UserName);
       UserName = db.getUserName();
        if(UserName != null) {
            editText_UserName.setText(UserName);
        }
        else
        {
            String Email = db.GetEmailDetails(this);
            int End = Email.indexOf("@");
            editText_UserName.setText(Email.substring(0,End));
        }
        btnSave= (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masterdetails.isOnline(ChatRoom.this)) {
                    db.updateUserName(editText_UserName.getText().toString());
                    Toast.makeText(getBaseContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Please Connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });



        AddLayout = (RelativeLayout) findViewById(R.id.top_layout);
        AddLayout.setVisibility(View.INVISIBLE);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (AddLayout.getVisibility() == View.VISIBLE) {
                        AddLayout.setVisibility(View.GONE);
                        btnAdd.setImageResource(R.drawable.menu);
                } else {
                        AddLayout.setVisibility(View.VISIBLE);
                        btnAdd.setImageResource(R.drawable.menuclose);
                }
            }
        });

        editText_RoomName= (EditText) findViewById(R.id.editText_RoomName);
        editText_RoomDesc= (EditText) findViewById(R.id.editText_RoomDesc);

        btnAddRoom = (Button) findViewById(R.id.btnAddRoom);
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masterdetails.isOnline(ChatRoom.this)) {
                    if (editText_RoomName.getText().toString().trim().length() > 0) {
                         new AsyncCreateRoom().execute(editText_RoomName.getText().toString().trim(),editText_RoomDesc.getText().toString().trim());
                    } else {
                        Toast.makeText(getBaseContext(), "Please input the Room Name", Toast.LENGTH_SHORT);
                        editText_RoomName.setSelection(0);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Please connect to internet.", Toast.LENGTH_SHORT);
                }
            }
        });


        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                isHandlerRunning = true;
                Log.i("Refreshing", "Refreshing");
                list = db.getChatRoomDetails();
                adapter = new ChatRoomAdapter(ChatRoom.this, list);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 60 * 1000);
                isHandlerRunning = false;
            }
        }, 60 * 1000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_daily_exam_question, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent i = new Intent(ChatRoom.this, HomeActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isHandlerRunning) {
            isHandlerRunning = false;
            handler.removeCallbacksAndMessages(null);
        }

            Log.i("Home", "in  resume here" + isHandlerRunning);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Home", "in  Destroy here" + isHandlerRunning);
        if (isHandlerRunning) {
            handler.removeCallbacksAndMessages(null);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Home", "in  resume here" + isHandlerRunning);

        }

    public class AsyncCreateRoom extends AsyncTask<String, Void, String> {
        String RoomName;
        String RoomDesc;
        int success = 0;
        ProgressDialog Progress = null;
        int RoomId;

        @Override
        protected void onPreExecute() {
            Progress = new ProgressDialog(ChatRoom.this);
            Progress.setMessage("Saving Room...");
            Progress.show();

        }
        @Override
        protected String doInBackground(String... Values) {

            db = new Exam_database(ChatRoom.this);

            RoomName = Values[0];
            RoomDesc = Values[1];

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.clear();
            params.add(new BasicNameValuePair("RoomName", RoomName));
            params.add(new BasicNameValuePair("Description", RoomDesc));
            params.add(new BasicNameValuePair("CreatedBy", db.GetEmailDetails(ChatRoom.this)));

            JSONObject json = jsonParser.makeHttpRequest(
                    masterdetails.AddChatRoom, "GET", params);
            try {
                if (json.length() > 0) {
                    // json success tag
                    success = json.getInt("success");
                    RoomId = json.getInt("Id");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (success == 1) {
                AddLayout.setVisibility(View.GONE);
                btnAdd.setImageResource(R.drawable.menu);

                editText_RoomDesc.setText("");
                editText_RoomName.setText("");

                ChatRoomData chatRoomData=new ChatRoomData();
                chatRoomData.setid(RoomId);
                chatRoomData.setRoomName(RoomName);
                chatRoomData.setDescription(RoomDesc);
                chatRoomData.setCreatedBy(db.getUserName());
                chatRoomData.setNotificationEnabled(1);
                chatRoomData.setFavEnabled(1);

                db.InsertChatRoomDetails(chatRoomData);

                Toast.makeText(getBaseContext(), "Room Added.", Toast.LENGTH_SHORT);

            } else if (success == 2) {
                editText_RoomName.setSelection(0);
                Toast.makeText(getBaseContext(), "Room with Same Name available.", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(getBaseContext(), "Unable to create room. Please try after sometime..", Toast.LENGTH_SHORT);
            }

            list = db.getChatRoomDetails();
            adapter = new ChatRoomAdapter(ChatRoom.this, list);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
            Progress.dismiss();
        }

    }

}