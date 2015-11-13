package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyxqsg.bsyduo229750.AdConfig;
import com.yyxqsg.bsyduo229750.Main;

import java.util.ArrayList;
import java.util.List;

import ExamFramework_Adapter.notificationcustomadapter;
import ExamFramework_Data.notificationtable;

public class ViewNotification extends ListActivity {

	ArrayList<notificationtable> notification;
	notificationcustomadapter adapter;
	EditText text;
	int scrolly = 0;
	int first = 0;

	Exam_database db = new Exam_database(this);
	List<notificationtable> list = new ArrayList<notificationtable>();
	final Handler handler = new Handler();
	private Main main; //Declare here
	private com.yyxqsg.bsyduo229750.AdView adView;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);

		//advertisement start
		AdConfig.setAppId(280371);  //setting appid.
		AdConfig.setApiKey("1435945311229750247"); //setting apikey
		// AdConfig.setTestMode(true);
		//AdConfig.setAdListener(adListener);  //setting global Ad listener.
		AdConfig.setCachingEnabled(false); //Enabling SmartWall ad caching.
		AdConfig.setPlacementId(0); //pass the placement id.

		//Initialize Airpush
		main=new Main(this);

		//for calling banner 360
		//main.start360BannerAd(this);

		//for calling Smartwall ad
		main.startInterstitialAd(AdConfig.AdType.smartwall);

		adView=(com.yyxqsg.bsyduo229750.AdView) findViewById(R.id.myAdView);
		adView.setBannerType(com.yyxqsg.bsyduo229750.AdView.BANNER_TYPE_IN_APP_AD);
		adView.setBannerAnimation(com.yyxqsg.bsyduo229750.AdView.ANIMATION_TYPE_FADE);
		adView.showMRinInApp(false);
		//adView.setNewAdListener(adListener); //for passing a new listener for inline banner ads.
		adView.loadAd();

        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.actionbar_notification);
        getActionBar().setDisplayHomeAsUpEnabled(true);



        TextView title = (TextView) findViewById(android.R.id.text1);
        title.setText("Notification Center");




        if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		first=0;

		ListView lv = getListView();
		if (masterdetails.isOnline(this)) {
			new asyncGetNotification().execute();
		} else {

			TextView txt = (TextView) this
					.findViewById(R.id.nonotificationtext);
			txt.setText("No internet Connection.Please connect to internet");

		}



		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				if (masterdetails.isOnline(ViewNotification.this)) {
					new asyncGetNotification().execute();
				} else {
					TextView txt = (TextView) ViewNotification.this
							.findViewById(R.id.nonotificationtext);
					txt.setText("No internet Connection.Please connect to internet");
					Toast.makeText(ViewNotification.this, "No internet Connection.Please connect to internet..", Toast.LENGTH_LONG).show();
				}


				handler.postDelayed(this, 300 * 500);
			}
		}, 300 * 500);



		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				first = first + 1;
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			 //app icon in action bar clicked; go home
			 Intent intent = new Intent(this, HomeActivity.class);
			 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 startActivity(intent);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() {

		handler.removeCallbacksAndMessages(null);
		super.onPause();
	}

	@Override
	public void onDestroy() {

		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	public void getnotification() {
		int index = 0;
		int top = 0;
		ListView myListView = (ListView) findViewById(android.R.id.list);
		if (first > 0) {
			index = myListView.getFirstVisiblePosition();
			View v = myListView.getChildAt(0);
			top = (v == null) ? 0 : v.getTop();
			list.clear();

		}

		list = db.Getnotitification();
		if (!list.isEmpty()) {
			TextView txt = (TextView) this
					.findViewById(R.id.nonotificationtext);
			txt.setVisibility(View.GONE);
			adapter = new notificationcustomadapter(this, list);
			setListAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else {

			TextView txt = (TextView) this
					.findViewById(R.id.nonotificationtext);
			txt.setVisibility(View.VISIBLE);
		}

	}

	//to check for updates in questions
	public class asyncGetNotification extends AsyncTask<String, Void, String> {
		JSONParser jsonParser = new JSONParser();
		int MainDBLastQuestion;


		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... urls) {


			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				getnotification();
			}
			catch (Exception ex)
			{
				Log.i("Exception", "got exception");
			}
		}
	}


}
