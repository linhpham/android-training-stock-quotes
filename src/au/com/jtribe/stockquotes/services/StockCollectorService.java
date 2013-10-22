package au.com.jtribe.stockquotes.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import au.com.jtribe.stockquotes.notification.GenerateNotification;

public class StockCollectorService extends Service {
	private static final String TAG = "StockCollectorService";
	
	private Timer timer;
	
	private TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			Log.i(TAG, "Timer task doing work.");
			FetchStockPrice fsp = new FetchStockPrice();
			String latestStockPrice = fsp.getLatestStockPrice();
			Log.i(TAG, "Latest Stock Price: " + latestStockPrice);
			
			//Cannot call UI method in non-UI Thread! To fix this, we can have AsyncTask and do UI in the postExecute() method.
			//Toast.makeText(StockCollectorService.this, "Latest Stock Price: " + latestStockPrice, Toast.LENGTH_SHORT).show();
			GenerateNotification genNotification = new GenerateNotification();
			genNotification.createNotification(StockCollectorService.this, latestStockPrice);
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		timer = new Timer("StockCollectorTimer");
		timer.schedule(updateTask, 1000L, 5 * 1000L);
		Toast.makeText(this, "Started Service", Toast.LENGTH_SHORT).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_NOT_STICKY;
	}
}
