package io.yunba.yo.activity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.json.JSONException;
import org.json.JSONObject;




import io.yunba.android.manager.YunBaManager;
import io.yunba.yo.R;
import io.yunba.yo.YoApplication;
import io.yunba.yo.util.SharePrefsHelper;
import io.yunba.yo.util.YoUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailActivity extends Activity  implements android.view.View.OnClickListener{
	private TextView userNameTxt;
	private EditText msgTxt;
	private Button sendBtn;
	public static boolean isActivityOn = false;
	public static TextView msg_show;
	private String user;
	public static ScrollView scroll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		initView();
		 Intent intent = getIntent();
	        if (null != intent) {
		        Bundle bundle = getIntent().getExtras();
		        String userName = bundle.getString("user_name");
		        setTitle(userName);
		        user = userName;
		        userNameTxt.setText(userName);
	        }
		
		
		
	}

	private void initView() {
		userNameTxt = (TextView)findViewById(R.id.tx_username);
		msgTxt = (EditText)findViewById(R.id.et_msg);
		sendBtn = (Button)findViewById(R.id.msg_publish);
		sendBtn.setOnClickListener(this);
		msg_show = (TextView)findViewById(R.id.msg_rec);
		msg_show.setMovementMethod(ScrollingMovementMethod.getInstance());
		msg_show.setBackgroundResource(R.drawable.text_view_border); 
		msg_show.setMaxLines(300);
		scroll = (ScrollView) findViewById(R.id.scroller);
	}

	@Override
	public void onClick(View arg) {
	    switch (arg.getId()) {
		case R.id.msg_publish:
			String msg = msgTxt.getText().toString().trim();
			if(YoUtil.isEmpty(msg)) {
				YoUtil.showToast("msg should not be null", this);
				return;
			}  else {
				JSONObject js = new JSONObject();
				try {
					js.put("user_name", SharePrefsHelper.getString(getApplicationContext(), "user_name", null));
					js.put("msg", msg);
				} catch (JSONException e) {
				}
				YunBaManager.publishToAlias(getApplicationContext(), userNameTxt.getText().toString().trim(), js.toString(), new IMqttActionListener() {
					
					@Override
					public void onSuccess(IMqttToken arg0) {
						YoUtil.showToast("Send msg succeed", getApplicationContext());	
						setCostomMsg("Send msg succeed to - " + userNameTxt.getText().toString().trim());
					}
					
					@Override
					public void onFailure(IMqttToken arg0, Throwable arg1) {
						YoUtil.showToast("Send msg failed", getApplicationContext());	
						setCostomMsg("Send msg fialed to - " + userNameTxt.getText().toString().trim());
					}
				});
			}
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isActivityOn = true;
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(null != msg_show)msg_show.setText(YoApplication.msgs.get(userNameTxt.getText().toString().trim()));				
			}
		});
	
		scroll.post(new Runnable() {
			public void run() {
				if (null != scroll) scroll.fullScroll(View.FOCUS_DOWN);
			}
		});
	}

	@Override
	protected void onPause() {
		isActivityOn = false;
		super.onPause();
	}
	private void setCostomMsg(final String msg){
		 if (null != msg_show) {
		
			 this.runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		           	 msg_show.append(msg+"\r\n");
		           	if(null != scroll) scroll.fullScroll(View.FOCUS_DOWN);
		            }
		     });
        }
	}
}
