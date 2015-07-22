package com.fyshadows.examframework.examframework;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "Exam";

	JSONParser jsonParser = new JSONParser();
  public static  String  identify;



    static String notificationfromscreen = null;

	public GCMIntentService() {
		super(masterdetails.sender_id);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
        Toast.makeText(GCMIntentService.this, "on registered", Toast.LENGTH_LONG);
        String email = getEmail(this);
        if(email.trim().isEmpty() )
        {
            email="no email found";
        }
        //Store it in server
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email",email));
        params.add(new BasicNameValuePair("regid",registrationId ));
        JSONObject json = jsonParser.makeHttpRequest(
                masterdetails.registeruser, "GET", params);

        Exam_database edb=new Exam_database(this);
        edb.StoreUserDetails(email,registrationId);
        Log.i(TAG, String.valueOf(json));
		// ServerUtilities.register(context, "prasanna", "prasanna",
		// registrationId);
	}


	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		//displayMessage(context, getString(R.string.gcm_unregistered));
		//ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        identify = intent.getExtras().getString("identify");
        if (identify.trim().equals("notification")) {
            String message = "";
            String receivedmessage = "";
            receivedmessage = intent.getExtras().getString("message");
            generateNotification(context, receivedmessage);

        }
    }

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		//String message = getString(R.string.gcm_deleted, total);
		///displayMessage(context, message);
		// notifies user
		//generateNotification(context, message);

	}

	/**
	 * Method called on Error
	 * */
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


    static String getEmail(Context context) {
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

    private static void generateNotification(Context context, String message) {

            int icon = R.mipmap.ic_launcher;
            long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(icon, message, when);
            notificationfromscreen = "notify";
            String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context,
                Configuringactivity.class);
            PendingIntent intent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);
            notification.setLatestEventInfo(context, title, message, intent);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            // Play default notification sound
            notification.defaults |= Notification.DEFAULT_SOUND;

            // Vibrate if vibrate is enabled
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notificationManager.notify(0, notification);

    }



}
