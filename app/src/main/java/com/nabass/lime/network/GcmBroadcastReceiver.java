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
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nabass.lime.Constants;
import com.nabass.lime.Init;
import com.nabass.lime.MainActivity;
import com.nabass.lime.R;
import com.nabass.lime.db.DBConstants;
import com.nabass.lime.db.DBExtended;

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
				sendNotification(Constants.NOTIFICATION_ERR_SEND, false);
			}
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification(Constants.NOTIFICATION_ERR_DEL, false);
			}
            else {
                Log.e(TAG, "Message received from GCM");
                String from = intent.getStringExtra(DBConstants.COL_FROM);
                if(DBExtended.checkIsBlockedByEmail(ctx.getContentResolver(), from)) {
                    return;
                }
				ContentValues values = new ContentValues(4);
				values.put(DBConstants.COL_MSG_TYPE,  DBConstants.MsgDirection.DIRECTION_INCOMING.ordinal());
				values.put(DBConstants.COL_MSG, intent.getStringExtra(DBConstants.COL_MSG));
				values.put(DBConstants.COL_FROM, intent.getStringExtra(DBConstants.COL_FROM));
				values.put(DBConstants.COL_TO, intent.getStringExtra(DBConstants.COL_TO));
                DBExtended.insertIncomingMsg(context.getContentResolver(), values, intent.getStringExtra(DBConstants.COL_FROM));
				
				if (Init.isNotify()) {
					sendNotification(Constants.NOTIFICAITON_NEW_MSG, true);
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