package io.yunba.yo;

import java.util.HashMap;
import java.util.Map;


import io.yunba.android.manager.YunBaManager;
import android.app.Application;

public class YoApplication extends Application {
	public static Map<String, String> msgs = new HashMap<String, String>();
	@Override
	public void onCreate() {
		super.onCreate();
		YunBaManager.start(getApplicationContext());
		
	}
	
	
	public static void putMsg(String from_user, String msg){
		 if(YoApplication.msgs.containsKey(from_user)) {
				String log = YoApplication.msgs.get(from_user);
				log += msg + "\r\n";
				YoApplication.msgs.put(from_user, log);
			 } else {
				 YoApplication.msgs.put(from_user,  msg + "\r\n");
			 }
	} 
}
