package au.com.jtribe.stockquotes.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import au.com.jtribe.stockquotes.MainActivity;
import au.com.jtribe.stockquotes.R;
import au.com.jtribe.stockquotes.services.StockCollectorService;

public class GenerateNotification {
	private static final int STOCK_NOTIFY_ID = 1;
	
	public void createNotification(Context context, String latestStockPrice) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
		
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Latest stock price: $" + latestStockPrice;
		long when = System.currentTimeMillis();
		
		Intent notificationIntent = new Intent(context, MainActivity.class);
		//Take particular note of FLAG_UPDATE_CURRENT which tells our pending intent to refresh, and inturn load up our new stock value. Without it our MainActivity would only update once.
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Bundle bun = new Bundle();
		bun.putString("latestStockPrice", latestStockPrice);
		notificationIntent.putExtras(bun);
		
		// big view of the notification.
		Intent stopIntent = new Intent(context, StockCollectorService.class);
		stopIntent.setAction("stop");
		stopIntent.putExtra("notificationId", STOCK_NOTIFY_ID);
		PendingIntent piStop = PendingIntent.getService(context, 0, stopIntent, 0);
		
		
		NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(context);
		notiBuilder.setTicker(tickerText);
		notiBuilder.setContentTitle("Stock update");
		notiBuilder.setContentText("Latest stock price: $" + latestStockPrice);
		notiBuilder.setStyle(new NotificationCompat.BigTextStyle()
			.bigText("Latest stock price: $" + latestStockPrice + " updated recently.")
		);
		notiBuilder.addAction(android.R.drawable.ic_delete, "stop", piStop);
		notiBuilder.setWhen(when);
		notiBuilder.setSmallIcon(icon);
		notiBuilder.setContentIntent(contentIntent);
		
		
		Notification notification = notiBuilder.build();
		setNotificationProperties(notification);
		mNotificationManager.notify(STOCK_NOTIFY_ID, notification);
	}
	
	private void setNotificationProperties(Notification notification) {
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
		//sound
		notification.defaults |= Notification.DEFAULT_SOUND;
		
		//vibration
		notification.vibrate = new long[] {0, 100, 200, 300};
		//needs <uses-permission android:name="android.permission.VIBRATE" />
		
		//LED
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
	}
}
