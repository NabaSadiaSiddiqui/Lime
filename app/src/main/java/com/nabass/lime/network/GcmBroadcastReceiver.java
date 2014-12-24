package com.nabass.lime.network;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nabass.lime.Init;
import com.nabass.lime.MainActivity;
import com.nabass.lime.R;
import com.nabass.lime.db.CustomCP;
import com.nabass.lime.db.CustomCP.MessageType;

public class GcmBroadcastReceiver extends BroadcastReceiver {
	
	private static final String TAG = "GcmBroadcastReceiver";
	private Context ctx;	

	@Override
	public void onReceive(Context context, Intent intent) {
		ctx = context;
		PowerManager mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		mWakeLock.acquire();
		try {
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
			String messageType = gcm.getMessageType(intent);
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error", false);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server", false);
			} else {
				String msg = intent.getStringExtra(CustomCP.COL_MESSAGE);
				String senderEmail = intent.getStringExtra(CustomCP.COL_SENDER_EMAIL);
				String receiverEmail = intent.getStringExtra(CustomCP.COL_RECEIVER_EMAIL);
				ContentValues values = new ContentValues(2);
				values.put(CustomCP.COL_TYPE,  MessageType.INCOMING.ordinal());
				values.put(CustomCP.COL_MESSAGE, msg);
				values.put(CustomCP.COL_SENDER_EMAIL, senderEmail);
				values.put(CustomCP.COL_RECEIVER_EMAIL, receiverEmail);
				context.getContentResolver().insert(CustomCP.CONTENT_URI_MESSAGES, values);
				
				if (Init.isNotify()) {
					sendNotification("New message", true);
				}
			}
			setResultCode(Activity.RESULT_OK);
		} finally {
			mWakeLock.release();
		}
	}

	private void sendNotification(String text, boolean launchApp) {
		NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder notification = new NotificationCompat.Builder(ctx);
		notification.setContentTitle(ctx.getString(R.string.app_name));
		notification.setContentText(text);
		notification.setAutoCancel(true);
		notification.setSmallIcon(R.drawable.ic_launcher);
		if (!TextUtils.isEmpty(Init.getRingtone())) {
			notification.setSound(Uri.parse(Init.getRingtone()));
		}
		
		if (launchApp) {
			Intent intent = new Intent(ctx, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setContentIntent(pi);
		}
	
		mNotificationManager.notify(1, notification.build());
	}
}