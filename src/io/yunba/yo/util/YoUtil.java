package io.yunba.yo.util;


import java.util.List;
import java.util.Random;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.yunba.yo.R;


import io.yunba.android.manager.YunBaManager;
import io.yunba.yo.activity.DetailActivity;
import io.yunba.yo.activity.MainActivity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class YoUtil {
	public static boolean isEmpty(String s) {
	    if (null == s)
	        return true;
	    if (s.length() == 0)
	        return true;
	    if (s.trim().length() == 0)
	        return true;
	    return false;
	}

	public static boolean showNotifation(Context context, String topic, String msg, String user) {
		try {
			Uri alarmSound = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.yo);
			long[] pattern = { 500, 500, 500 };
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(topic).setContentText(msg)
					.setSound(alarmSound).setVibrate(pattern).setAutoCancel(true);
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(context, MainActivity.class);
			if (!YoUtil.isEmpty(user)) {
				resultIntent = new Intent(context, DetailActivity.class);
				resultIntent.putExtra("user_name", user);
			}
			if (!YoUtil.isEmpty(topic))
				resultIntent.putExtra(YunBaManager.MQTT_TOPIC, topic);
			if (!YoUtil.isEmpty(msg))
				resultIntent.putExtra(YunBaManager.MQTT_MSG, msg);
			// The stack builder object will contain an artificial back stack
			// for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out
			// of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(MainActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			Random r = new Random();
			mNotificationManager.notify(r.nextInt(), mBuilder.build());
		} catch (Exception e) {
			return false;
		}
		return true;
   }
	
	 public static void showToast(final String toast, final Context context)
	    {
		 if (!isAppOnForeground(context)) return;
	    	new Thread(new Runnable() {
				
				@Override
				public void run() {
					Looper.prepare();
					Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
					Looper.loop();
				}
			}).start();
	    }
	 
	  public static boolean isAppOnForeground(Context context) { 
		   	 ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		       // Returns a list of application processes that are running on the device 
		       List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses(); 
		       if (appProcesses == null) return false; 
		       for (RunningAppProcessInfo appProcess : appProcesses) { 
		           // importance: 
		           // The relative importance level that the system places  
		           // on this process. 
		           // May be one of IMPORTANCE_FOREGROUND, IMPORTANCE_VISIBLE,  
		           // IMPORTANCE_SERVICE, IMPORTANCE_BACKGROUND, or IMPORTANCE_EMPTY. 
		           // These constants are numbered so that "more important" values are 
		           // always smaller than "less important" values. 
		           // processName: 
		           // The name of the process that this object is associated with. 
		           if (appProcess.processName.equals(context.getPackageName()) 
		                   && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) { 
		               return true; 
		           } 
		       } 
		       return false; 
		   } 
	  
	  
		public static <T> String join(T[] array, String cement) {
		    StringBuilder builder = new StringBuilder();

		    if(array == null || array.length == 0) {
		        return null;
		    }
		    for (T t : array) {
		        builder.append(t).append(cement);
		    }

		    builder.delete(builder.length() - cement.length(), builder.length());

		    return builder.toString();
		}
		
		 public static boolean isNetworkEnabled(Context context) {
		        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo info = conn.getActiveNetworkInfo();
		        return (info != null && info.isConnected());
		    }
		 
		 public static String getImei(Context context, String imei) {
					TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
					imei = telephonyManager.getDeviceId();
				return imei;
			}
		 
		    public static boolean isValidUserName(String userName) {
		        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		        Matcher m = p.matcher(userName);
		        return m.matches();
		    }
}
