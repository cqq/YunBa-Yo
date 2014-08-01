package io.yunba.yo.activity;

import io.yunba.android.manager.YunBaManager;
import io.yunba.yo.R;
import io.yunba.yo.YoApplication;
import io.yunba.yo.adapter.YoAdapter;
import io.yunba.yo.bean.ItemBean;
import io.yunba.yo.util.SharePrefsHelper;
import io.yunba.yo.util.YoUtil;
import io.yunba.yo.view.SwipeDetector;
import io.yunba.yo.view.SwipeDetector.Action;

import java.util.ArrayList;

import java.util.Set;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainActivity extends Activity implements android.widget.AdapterView.OnItemClickListener{
	 ArrayList<ItemBean> listItems = new ArrayList<ItemBean>();
	 private ListView listview;
	 public static YoAdapter adapter;
	 SwipeDetector swipeDetector;

    
	private String userName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  Intent intent = getIntent();
	        if (null != intent) {
		        Bundle bundle = getIntent().getExtras();
		        userName = bundle.getString("user_name");
		        if(YoUtil.isEmpty(userName)) {
		        	userName = SharePrefsHelper.getString(getApplicationContext(), "user_name", null);
		        }
		        setTitle("welcome user: " +  userName);
		        
	        } else {
	        	userName = SharePrefsHelper.getString(getApplicationContext(), "user_name", null);
	        	setTitle("welcome user: " +  userName);
	        }
	       
		setContentView(R.layout.fragment_main);
		intUses();
		
	  //   gd = new GestureDetector(this);
		 adapter = new YoAdapter(listItems, this);
	     listview = (ListView)findViewById(R.id.list);
         listview.setAdapter(adapter);
         swipeDetector = new SwipeDetector();
         listview.setOnTouchListener(swipeDetector);
         listview.setOnItemClickListener(this);
	}

	private void intUses() {
		Set<String> sets = SharePrefsHelper.getSet(getApplicationContext(), YunBaManager.HISTORY_TOPICS, null);
		if (sets != null && listItems.size() == 0) {
			for (String string : sets) {
				listItems.add(new ItemBean(string));
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, LoginActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		
		if(swipeDetector.swipeDetected()) {
            if(swipeDetector.getAction() == Action.RL ) {
            	  System.err.println("RK");
            	  adapter.users.get(index).show = 1;
            	  adapter.notifyDataSetChanged();
            } else {

            }
        }  else {
        	
        	final int id = index;
       	    final ItemBean it = adapter.users.get(index);
       	     System.err.println("click - " + it.value );
       	     if (it.show == 2 || it.value.contains(" sent ")) return;
       	     
       	  JSONObject js = new JSONObject();
			try {
				js.put("user_name", userName);
				js.put("msg", "yo");
			} catch (JSONException e) {
			}
			YunBaManager.publishToAlias(getApplicationContext(), it.value,  js.toString(), new IMqttActionListener() {
				
				@Override
				public void onSuccess(IMqttToken arg0) {
					final String old = it.value;
					adapter.users.get(id).show = 0;
					YoApplication.putMsg(it.value, "Send msg = " + "yo to " + it.value + " Succeed!");
					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							adapter.users.get(id).value = it.value + " sent succeed!";
							adapter.notifyDataSetChanged();	
						}
					});
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
					
					MainActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {	
							adapter.users.get(id).value = old;
							adapter.notifyDataSetChanged();	
						}
					});
				
				}
				
				@Override
				public void onFailure(IMqttToken arg0, Throwable arg1) {
					final String old = it.value;
					adapter.users.get(id).show = 0;
					YoApplication.putMsg(it.value, "Send msg = " + "yo to " + it.value + " Failed!");
					MainActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {					
							adapter.users.get(id).value = it.value + " sent faild!";
							adapter.notifyDataSetChanged();							
						}
					});
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					
					}
					
					MainActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {	
							
							adapter.users.get(id).value = old;
							adapter.notifyDataSetChanged();	
						}
					});
				}
			});
			adapter.users.get(index).show = 2;
			adapter.notifyDataSetChanged();
        }
		
	}

}
