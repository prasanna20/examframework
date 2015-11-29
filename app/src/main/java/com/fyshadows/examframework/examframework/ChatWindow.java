package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Adapter.ChatWindowAdapter;
import ExamFramework_AsyncTask.AsyncSendChatMessage;
import ExamFramework_Data.ChatData;

public class ChatWindow   extends ListActivity {

    TextView empty;
    TextView  txtChatRoomName;
    TextView txtMessage;
    ImageView imgSend;
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
        txtMessage=(TextView) this.findViewById(R.id.txtMessage);
        imgSend =(ImageView) this.findViewById(R.id.imgSend);

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

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                     if(masterdetails.isOnline(ChatWindow.this)) {
                         if (txtMessage.getText().length() > 0) {
                             new AsyncSendChatMessage(ChatWindow.this).execute(String.valueOf(RoomId), txtMessage.getText().toString());
                             txtMessage.setText("");
                             txtMessage.clearFocus();
                             View view = ChatWindow.this.getCurrentFocus();
                             if (view != null) {
                                 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                 imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                             }
                         } else {
                             Toast.makeText(ChatWindow.this, "Please enter message!", Toast.LENGTH_SHORT).show();
                         }
                     }
                        else
                     {
                         Toast.makeText(ChatWindow.this, "Please connect Internet!", Toast.LENGTH_SHORT).show();
                     }
            }
        });

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
