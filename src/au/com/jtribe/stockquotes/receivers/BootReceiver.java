package au.com.jtribe.stockquotes.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import au.com.jtribe.stockquotes.services.StockCollectorService;

public class BootReceiver extends BroadcastReceiver {
	private static final String TAG = "BootReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");
		context.startService(new Intent(context, StockCollectorService.class));
	}

}
