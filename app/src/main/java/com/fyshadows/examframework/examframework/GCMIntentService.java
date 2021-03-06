package com.fyshadows.examframework.examframework;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_AsyncTask.AsyncUpdateChatMessage;
import ExamFramework_AsyncTask.AsyncUpdateChatRoom;
import ExamFramework_AsyncTask.AsyncUpdateNewValues;


public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "Exam";

    JSONParser jsonParser = new JSONParser();
    public static String identify;
    public static String ExamReminder;
    public static int RoomId;
    public String UserName;
    public static String RoomName;


    static String notificationfromscreen = null;

    public GCMIntentService() {
        super(masterdetails.sender_id);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Toast.makeText(GCMIntentService.this, "on registered", Toast.LENGTH_LONG);
        String email = getEmail(this);
        if (email.trim().isEmpty()) {
            email = "no email found";
        }
        //Store it in server
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("regid", registrationId));
        JSONObject json = jsonParser.makeHttpRequest(
                masterdetails.registeruser, "GET", params);

        Exam_database edb = new Exam_database(this);
        edb.StoreUserDetails(email, registrationId);
    }


    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        //displayMessage(context, getString(R.string.gcm_unregistered));
        //ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        identify = intent.getExtras().getString("identify");
        ExamReminder = intent.getExtras().getString("ExamReminder");
        if (identify.trim().equals("notification")) {
            String MessageTitle = "";
            String MessageText = "";

            MessageTitle = intent.getExtras().getString("MessageTitle");
            MessageText = intent.getExtras().getString("MessageText");
            generateNotification(context, MessageTitle, MessageText);

        } else if (identify.trim().equals("update")) {
            new AsyncUpdateNewValues(GCMIntentService.this).execute();
            //Start : Insert Analysis
            masterdetails masterdetails = new masterdetails(this);
            masterdetails.insertAnalysis(this, 8);
            Log.i("Notification", "Executing update task");
            //End : Insert Analysis
        } else if (identify.trim().equals("DeleteRoom")) {
            Exam_database db = new Exam_database(GCMIntentService.this);
            int RoomId = Integer.parseInt(intent.getExtras().getString("RoomId"));
            db.deleteChatRoom(RoomId);
            //Start : Insert Analysis
            masterdetails masterdetails = new masterdetails(this);
            masterdetails.insertAnalysis(this, 10);
            Log.i("Notification", "Deleted Room Id");
            //End : Insert Analysis
        } else if (identify.trim().equals("UpdateChat")) {
            //Start : Get ChatRoom
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new AsyncUpdateChatRoom(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new AsyncUpdateChatRoom(this).execute();
            }
            //End : Chat Room

            //Start : Get Chat Message
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new AsyncUpdateChatMessage(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new AsyncUpdateChatMessage(this).execute();
            }
            //End : Chat Message
        } else if (identify.trim().equals("chatmessage")) {

            //Start : Get Chat Message
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new AsyncUpdateChatMessage(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new AsyncUpdateChatMessage(this).execute();
            }
            //End : Chat Message

            Exam_database db = new Exam_database(GCMIntentService.this);
            RoomId = Integer.parseInt(intent.getExtras().getString("RoomId"));
            UserName = intent.getExtras().getString("UserName");
            RoomName = db.getChatRoomName(RoomId).toUpperCase();

            String MessageTitle = RoomName;
            String MessageText = intent.getExtras().getString("ChatMessage");
            Boolean isNotificationEnabled = db.checkNotificationEnabledForChatRoom(RoomId);
            if (isNotificationEnabled) {
                generateNotification(context, MessageTitle, MessageText);
            }
        }
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");

    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        //displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        //displayMessage(context,
        //	getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }


    public static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);

        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public static void generateNotification(Context context, String MessageTitle, String MessageText) {
        try {
            int icon = R.drawable.logotrans;
            long when = System.currentTimeMillis();
            Intent notificationIntent;

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(icon, MessageText, when);

            // String title = context.getString(R.string.app_name);
            String title = MessageTitle;
            notificationIntent = new Intent(context, Configuringactivity.class);

            if (identify.trim().equals("chatmessage")) {
                notificationIntent = new Intent(context, ChatWindow
                        .class);
                Bundle bundle = new Bundle();
                bundle.clear();
                bundle.putInt("RoomId", RoomId);
                bundle.putString("RoomName", RoomName);

                notificationIntent.putExtras(bundle);
            } else {

                if (ExamReminder != null) {
                    if (ExamReminder.equalsIgnoreCase("Yes")) {
                        notificationIntent = new Intent(context, DailyExamQuestion.class);
                    } else {
                        notificationIntent = new Intent(context, ViewNotification.class);
                    }
                }

                Bundle bundle = new Bundle();
                bundle.clear();
                bundle.putString("MessageTitle", MessageTitle);
                bundle.putString("MessageText", MessageText);

                notificationIntent.putExtras(bundle);
            }


            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

     // set intent so it does not start a new activity
    int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
    PendingIntent contentIntent = PendingIntent.getActivity(context, iUniqueId, notificationIntent, 0);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    notification.setLatestEventInfo(context, title, MessageText, contentIntent);
    notification.flags |= Notification.FLAG_AUTO_CANCEL;

    // Play default notification sound
    notification.defaults |= Notification.DEFAULT_SOUND;
    notificationManager.notify(0, notification);

         /*   Notification.Builder builder = new Notification.Builder(context);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setSmallIcon(R.drawable.notification_template_icon_bg)
                    .setContentTitle("Music player")
                    .setContentIntent(pendingIntent);
            notificationManager.notify(R.drawable.notification_template_icon_bg, notification);*/
        } catch (Exception ex) {
            Log.i("error", "error in GCM intent service" + ex.toString());
        }

    }

}
