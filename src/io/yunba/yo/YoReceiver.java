package io.yunba.yo;

import org.json.JSONException;
import org.json.JSONObject;

import io.yunba.android.manager.YunBaManager;
import io.yunba.yo.activity.DetailActivity;
import io.yunba.yo.adapter.YoAdapter;
import io.yunba.yo.util.YoUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class YoReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (YunBaManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {

			String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
			String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);
			try {
				JSONObject js = new JSONObject(msg);
				String message = js.optString("msg");
				String from_user = js.optString("user_name");

				if (!YoUtil.isEmpty(from_user) && !YoUtil.isEmpty(message)) {
					YoAdapter.addUser(context, from_user);
					if (message.equals("yo")) {
						YoUtil.showNotifation(context, "From " + from_user,
								message, null);
						
					} else {
						YoUtil.showNotifation(context, "From " + from_user, message, from_user);
					}
					YoApplication.putMsg(from_user, "recevied msg = " + message + " from " + from_user);
					if (DetailActivity.isActivityOn) {
						if (null != DetailActivity.msg_show) {
							DetailActivity.msg_show.append("recevied msg = " + message + " from " + from_user + "\r\n");
						}
					}
				}

			} catch (JSONException e) {
			}
			
		}
	}

}
