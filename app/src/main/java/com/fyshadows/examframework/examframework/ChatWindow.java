package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Adapter.ChatWindowAdapter;
import ExamFramework_Data.ChatData;

public class ChatWindow   extends ListActivity {

    TextView empty;
    TextView  txtChatRoomName;
    List<ChatData> list = new ArrayList<ChatData>();
    int scrolly = 0;
    int first = 0;
    Exam_database db;
    int RoomId;
    String RoomName;
    ChatWindowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.actionbar_chatwindow);
        getActionBar().setDisplayHomeAsUpEnabled(true);

         db = new Exam_database(this);

        empty=(TextView) this.findViewById(android.R.id.empty);
        txtChatRoomName=(TextView) this.findViewById(R.id.txtChatRoomName);

        Bundle bundle = getIntent().getExtras();
        RoomId = bundle.getInt("RoomId");
        RoomName= bundle.getString("RoomName");
        txtChatRoomName.setText(RoomName);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        ListView myListView = (ListView) findViewById(android.R.id.list);
        list = db.getChatMessage(RoomId);
        int index = 0;
        int top = 0;

        if (!list.isEmpty()) {
            adapter = new ChatWindowAdapter(this, list);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (first == 0) {
                myListView.setSelection(scrolly);
            }

            if (first > 0) {
                myListView.setSelectionFromTop(index, top);
            }

            empty.setVisibility(View.INVISIBLE);

        }
        else
        {
            empty.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(ChatWindow.this, ChatRoom.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
