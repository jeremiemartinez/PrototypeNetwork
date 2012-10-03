package com.proto;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

@EActivity
public class MainActivity extends Activity {

	/**************************************************************************/
	/**     Constants                                                         */
	/**************************************************************************/

	private static final String FILE_URL = "http://upload.wikimedia.org/wikipedia/commons/c/c2/Space_Needle_panorama_large.jpg"; // 8.9MB
	private static final int INIT_RATE = 300; // Kbs
	private static final int MAX_RATE = 1024; // Kbs
	private static final int DELAY = 250; // ms

	/**************************************************************************/
	/**     Views                                                             */
	/**************************************************************************/

	@ViewById(R.id.traffic_txt)
	TextView traffic_txt;

	@ViewById(R.id.traffic)
	ProgressBar traffic;

	@ViewById(R.id.graph)
	LinearLayout graph;

	@ViewById(R.id.button)
	Button button;

	@ViewById(R.id.download_txt)
	TextView download_txt;

	@ViewById(R.id.download)
	ProgressBar download;

	/**************************************************************************/
	/**     Util classes                                                      */
	/**************************************************************************/

	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			checkConnection();			
		}
	};

	private class TrafficProcessor implements Runnable {

		private long lastProcessedTime;
		private long lastProcessedData;
		private boolean stop;

		public void reset() {
			lastProcessedTime = System.currentTimeMillis();
			lastProcessedData = 0;
			stop = false;
		}

		public void stop() {
			stop = true;
			resetTraffic();
		}

		@Override
		public void run() {
			if (stop)
				return;

			long currentTime = System.currentTimeMillis();
			double timeDelta = currentTime - lastProcessedTime;
			double dataDelta = currentData - lastProcessedData;

			updateTraffic(dataDelta / 1024d / timeDelta * 1000d);

			lastProcessedTime = currentTime;
			lastProcessedData = currentData;
			handler.postDelayed(this, DELAY);
		}
	}

	/**************************************************************************/
	/**     Members                                                           */
	/**************************************************************************/

	private boolean isDownloading;

	private long currentData;

	private Handler handler = new Handler();

	private TrafficProcessor trafficProcessor = new TrafficProcessor(); 

	private ArrayList<GraphViewData> plots = new ArrayList<GraphViewData>();

	private int txcount;

	private double maxRate;

	/**************************************************************************/
	/**     Life-cycle                                                        */
	/**************************************************************************/ 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@AfterViews
	protected void init() {
		GraphView graphView = new LineGraphView(this, "traffic");
		graphView.setManualYAxisBounds(INIT_RATE, 0d);
		graph.addView(graphView);
	}

	@AfterViews
	protected void checkConnection() {
		if (isConnected()) {
			if (!isDownloading)
				traffic_txt.setText("Wifi Ok");
		} else
			traffic_txt.setText("No Wifi");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**************************************************************************/
	/**     Button                                                            */
	/**************************************************************************/

	@Click(R.id.button)
	public void clic(View view) {
		if (isDownloading) {
			isDownloading = false;
			button.setText(R.string.txt_button);
			return;
		}
		if (!isConnected())
			return;
		button.setText(R.string.txt_button_alt);
		startDownload();
	}

	/**************************************************************************/
	/**     Network                                                           */
	/**************************************************************************/

	private boolean isConnected() {
		ConnectivityManager conn =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conn.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
	}

	/**************************************************************************/
	/**     Download                                                          */
	/**************************************************************************/

	@Background
	protected void startDownload() {
		isDownloading = true;
		trafficProcessor.reset();
		resetDownload();
		handler.postDelayed(trafficProcessor, DELAY);

		URL url;
		HttpURLConnection urlConnection = null;
		try {
			url = new URL(FILE_URL);
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			readStream(in, urlConnection.getContentLength());
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} finally {
			urlConnection.disconnect();
		}
	}

	private void readStream(InputStream in, int length) throws IOException {
		try {
			byte[] buffer = new byte[1448];
			List<String> file = new LinkedList<String>();
			int readBytes;
			int dispc = 0;
			while ((readBytes = in.read(buffer)) != -1) {
				if (!isDownloading)
					break;
				file.add(new String(buffer, 0, readBytes));
				currentData += readBytes;
				if (dispc++ > 20) {
					updateDownload(((double) currentData)/((double) length), ((double) currentData) / 1024d);
					dispc = 0;
				}
			}
			updateDownload(((double) currentData)/((double) length), ((double) currentData) / 1024d);
		} finally {
			isDownloading = false;
			enableButton();
			trafficProcessor.stop();
		}
	}

	/**************************************************************************/
	/**     Ui update                                                         */
	/**************************************************************************/

	@UiThread
	protected void enableButton() {
		button.setText(R.string.txt_button);
	}

	@UiThread
	protected void updateDownload(double progress, double data) {
		String dispData = (data >= 1024) ? String.format("%.2f Mb", data / 1024d) : String.format("%.1f Kb", data);
		download.setProgress((int) (progress * download.getMax()));
		download_txt.setText(String.format("%s   /   %.1f %%", dispData, progress * 100));
	}

	@UiThread
	protected void updateTraffic(double rate) {
		if (rate >= maxRate)
			maxRate = rate;
		updateGraph(rate);
		traffic.setProgress((int) (rate / MAX_RATE * traffic.getMax()));
		traffic_txt.setText((rate >= 1024) ? String.format("%.2f Mbs", rate / 1024d) : String.format("%.0f Kbs", rate));
	}

	private void updateGraph(double rate) {
		GraphView graphView  = new LineGraphView(this, "traffic");
		graphView.setManualYAxisBounds((maxRate <= INIT_RATE) ? INIT_RATE : maxRate, 0d);
		plots.add(new GraphViewData(txcount++, rate));

		GraphViewData[] aplots = new GraphViewData[plots.size()];
		int i = 0;
		for (GraphViewData g : plots)
			aplots[i++] = g;

		graphView.addSeries(new GraphViewSeries(aplots));
		graph.removeAllViews();
		graph.addView(graphView);
	}

	@UiThread
	protected void resetDownload() {
		currentData = 0;
		download.setProgress(0);
		download_txt.setText(R.string.download_txt);
	}

	@UiThread
	protected void resetTraffic() {
		txcount = 0;
		maxRate = 0;
		traffic.setProgress(0);
		checkConnection();
		init();
		plots.clear();
	}
}
