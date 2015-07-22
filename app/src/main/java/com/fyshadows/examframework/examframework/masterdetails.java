package com.fyshadows.examframework.examframework;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Prasanna on 16-03-2015.
 */
public class masterdetails {

    static String sender_id="830753459977";
    static int timer=36;
    static String registeruser ="http://collegemateapp.com/ExamFrameWork/GCMregistration.php";
    static String getMasterQuestion="http://collegemateapp.com/ExamFrameWork/getExamQuestions.php";
    static String checkForupdate="http://collegemateapp.com/ExamFrameWork/CheckForNewQuestions.php";
    static String getNewQuestions="http://collegemateapp.com/ExamFrameWork/getNewQuestions.php";

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
