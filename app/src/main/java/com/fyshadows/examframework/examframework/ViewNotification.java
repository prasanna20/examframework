package com.fyshadows.examframework.examframework;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

		//Initialize Airpush
		main=new Main(this);

		//for calling banner 360
		main.start360BannerAd(this);

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
			getnotification();
		} else {

			TextView txt = (TextView) this
					.findViewById(R.id.nonotificationtext);
			txt.setText("No internet Connection.Please connect to internet");
			Toast.makeText(this, "No internet Connection.Please connect to internet..", Toast.LENGTH_LONG).show();
		}



		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				if (masterdetails.isOnline(ViewNotification.this)) {
					getnotification();
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
Log.i("scrol listen","scrol listen");

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
           // finish();

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
			// ...

			// restore index and position

		}

		list = db.Getnotitification();
		if (!list.isEmpty()) {
			TextView txt = (TextView) this
					.findViewById(R.id.nonotificationtext);
			txt.setVisibility(View.GONE);
			adapter = new notificationcustomadapter(this, list);
			setListAdapter(adapter);
			adapter.notifyDataSetChanged();
			Log.i("asdas", "called");
			//if (first == 0) {
			//	Log.i("set last","set last");
			//	myListView.setSelection(scrolly);
			//}

		//	if (first > 0) {
				// myListView.setSelection(scrolly);
		//		myListView.setSelectionFromTop(index, top);
		//	}

		} else {

			TextView txt = (TextView) this
					.findViewById(R.id.nonotificationtext);
			txt.setVisibility(View.VISIBLE);
		}

	}

	/*
	 * // This task will be called to store the images in back ground private
	 * class AsyncTaskEx extends AsyncTask<Void, Void, Void> {
	 * 
	 * protected void onPreExecute() {
	 * 
	 * Log.i("into async", "into async"); }
	 * 
	 * 
	 * The system calls this to perform work in a worker thread and delivers it
	 * the parameters given to AsyncTask.execute()
	 * 
	 * protected Void doInBackground(Void... arg0) {
	 * 
	 * } }
	 */
}
