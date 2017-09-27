package ExamFramework_AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fyshadows.examframework.examframework.Exam_database;
import com.fyshadows.examframework.examframework.JSONParser;
import com.fyshadows.examframework.examframework.masterdetails;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanna on 14-11-2015.
 */


public class AsyncUpdateUserName extends AsyncTask<String, Void, String> {

    JSONParser jsonParser = new JSONParser();
    Exam_database db;
    private Context myCtx;


    public AsyncUpdateUserName(Context ctx) {
        // Now set context
        this.myCtx = ctx;
    }


    @Override
    protected void onPreExecute() {

    }


    @Override
    protected String doInBackground(String... Values) {
        try {
            Log.i("Async", "Executing Aync task");
            String UserName = Values[0];

            db = new Exam_database(this.myCtx);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.clear();
            params.add(new BasicNameValuePair("email", db.GetEmailDetails(this.myCtx)));
            params.add(new BasicNameValuePair("UserName", String.valueOf(UserName)));


            JSONObject json = jsonParser.makeHttpRequest(
                    masterdetails.UpdateUserName, "GET", params);

        } catch (Exception e) {

            Log.i("Analysis activity", "Error");
        }
        return null;
    }

}