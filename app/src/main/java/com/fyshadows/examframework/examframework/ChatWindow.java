package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Adapter.ChatWindowAdapter;
import ExamFramework_AsyncTask.AsyncSendChatMessage;
import ExamFramework_AsyncTask.AsyncUpdateChatMessage;
import ExamFramework_Data.ChatData;

public class ChatWindow   extends ListActivity {

    TextView empty;
    TextView  txtChatRoomName;
    TextView txtMessage;
    ImageView imgSend;
    List<ChatData> list = new ArrayList<ChatData>();
    int scrolly = 0;
    int index = 0;
    int top = 0;
    static int Scrolling=0;
    Exam_database db;
    int RoomId;
    String RoomName;
    ChatWindowAdapter adapter;
    Handler handler = new Handler();
    Handler UpdateListViewHandler = new Handler();
    Boolean isUpdateListViewHandlerRunning = false;
    static int scrollChanged=0;
    ListView myListView;


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
        RoomName= bundle.getString("RoomName").toUpperCase();
        txtChatRoomName.setText(RoomName.toUpperCase());

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Start : Insert Analysis
        masterdetails masterdet=new masterdetails(this);
        masterdet.insertAnalysis(this, 11);
        //End : Insert Analysis


        myListView = (ListView) findViewById(android.R.id.list);
        list = db.getChatMessage(RoomId);


        if (!list.isEmpty()) {
            adapter = new ChatWindowAdapter(this, list);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
            empty.setVisibility(View.INVISIBLE);

        }
        else
        {
            empty.setVisibility(View.VISIBLE);
        }

        myListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                Log.i("scrol listen", "scrol listen");
                Scrolling=1;

            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                Log.i("messageactivity", "messageactivity");
                Scrolling=0;
                scrollChanged = 1;
            }
        });

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masterdetails.isOnline(ChatWindow.this)) {
                    if (txtMessage.getText().length() > 0) {
                        Toast.makeText(ChatWindow.this, "Sending Message...", Toast.LENGTH_SHORT).show();
                        new AsyncSendChatMessage(ChatWindow.this).execute(String.valueOf(RoomId), txtMessage.getText().toString());
                        txtMessage.setText("");
                        txtMessage.clearFocus();
                        View view = ChatWindow.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    } else {
                        Toast.makeText(ChatWindow.this, "Please enter message!", Toast.LENGTH_SHORT).show();
                    }
                    scrollChanged = 0;
                } else {
                    Toast.makeText(ChatWindow.this, "Please connect Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

     /*   //handler to check for messages
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (masterdetails.isOnline(ChatWindow.this)) {

                    if(!AsyncUpdateChatMessage.isHandlerRunning) {
                        Log.i("Chat Window", "Executing async updating chat messages");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new AsyncUpdateChatMessage(ChatWindow.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new AsyncUpdateChatMessage(ChatWindow.this).execute();
                        }
                    }
                }

                handler.postDelayed(this, 150 * 50);
            }
        }, 150 * 50);
*/
          //handler to update List View
        UpdateListViewHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.i("Updating", "Upd list scroll");
                if (Scrolling == 0 || scrollChanged == 0) {
                    int index = 0;
                    int top = 0;
                    if (scrollChanged == 1) {
                        index = myListView.getFirstVisiblePosition();
                        View v = myListView.getChildAt(0);
                        top = (v == null) ? 0 : v.getTop();

                    }

                    Log.i("Updating", "Upd list view");
                    isUpdateListViewHandlerRunning = true;
                    list = db.getChatMessage(RoomId);
                    adapter = new ChatWindowAdapter(ChatWindow.this, list);

                     setListAdapter(adapter);


                    adapter.notifyDataSetChanged();

                    if (scrollChanged == 1) {

                        myListView.setSelectionFromTop(index, top);
                    }

                    isUpdateListViewHandlerRunning = false;

                }
                UpdateListViewHandler.postDelayed(this, 10 * 50);
            }


        }, 10 * 50);


        //To Execute first time when the chat window is opened
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncUpdateChatMessage(ChatWindow.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            new AsyncUpdateChatMessage(ChatWindow.this).execute();
        }
*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                UpdateListViewHandler.removeCallbacksAndMessages(null);
                Intent i = new Intent(ChatWindow.this, ChatRoom.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( AsyncUpdateChatMessage.isHandlerRunning) {
            UpdateListViewHandler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    Log.i("Updating", "Upd list scroll");
                    if (Scrolling == 0 || scrollChanged == 0) {
                        int index = 0;
                        int top = 0;
                        if (scrollChanged == 1) {
                            index = myListView.getFirstVisiblePosition();
                            View v = myListView.getChildAt(0);
                            top = (v == null) ? 0 : v.getTop();

                        }

                        Log.i("Updating", "Upd list view");
                        isUpdateListViewHandlerRunning = true;
                        list = db.getChatMessage(RoomId);
                        adapter = new ChatWindowAdapter(ChatWindow.this, list);

                        setListAdapter(adapter);


                        adapter.notifyDataSetChanged();

                        if (scrollChanged == 1) {

                            myListView.setSelectionFromTop(index, top);
                        }

                        isUpdateListViewHandlerRunning = false;

                    }
                    UpdateListViewHandler.postDelayed(this, 10 * 50);
                }


            }, 10 * 50);


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Home", "in  Destroy here" + AsyncUpdateChatMessage.isHandlerRunning);
        if (AsyncUpdateChatMessage.isHandlerRunning) {
            handler.removeCallbacksAndMessages(null);
        }

        if (isUpdateListViewHandlerRunning) {
            isUpdateListViewHandlerRunning = false;
            UpdateListViewHandler.removeCallbacksAndMessages(null);
        }
    }


}
