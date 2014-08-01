package io.yunba.yo.activity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;




import io.yunba.android.manager.YunBaManager;
import io.yunba.yo.R;
import io.yunba.yo.util.SharePrefsHelper;
import io.yunba.yo.util.YoUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends Activity  implements android.view.View.OnClickListener{
	private TextView userNameTxt;
	private Button loginBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("设置用户名");
		setContentView(R.layout.login);
		initView();
		
	}

	private void initView() {
		userNameTxt = (TextView)findViewById(R.id.username_txt);
		loginBtn = (Button)findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg) {
	    switch (arg.getId()) {
		case R.id.login_btn:
			String userName = userNameTxt.getText().toString().trim();
			if(YoUtil.isEmpty(userName) || !YoUtil.isValidUserName(userName)) {
				YoUtil.showToast("用户名不合法，必须为数字字母和下滑线组成的字符串", this);
				return;
			}  else {
				YunBaManager.setAlias(getApplicationContext(), userName, new IMqttActionListener() {
					
					@Override
					public void onSuccess(IMqttToken mqtt) {
						SharePrefsHelper.setString(getApplicationContext(), "user_name", mqtt.getAlias());
						startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("user_name",  mqtt.getAlias()));
						
					}
					
					@Override
					public void onFailure(IMqttToken arg, Throwable arg1) {
						YoUtil.showToast("用户名设置失败", getApplicationContext());
						
					}
				});
			}
			break;

		default:
			break;
		}
		
	}
}
