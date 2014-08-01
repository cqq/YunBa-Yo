package io.yunba.yo.activity;

import io.yunba.yo.util.SharePrefsHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String userName = SharePrefsHelper.getString(getApplicationContext(), "user_name", null);
		if(null == userName) {
			startActivity(new Intent(this, LoginActivity.class));
		} else {
			startActivity(new Intent(this, MainActivity.class).putExtra("user_name", userName));
		}
		
	}

}
