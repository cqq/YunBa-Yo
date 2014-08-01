package io.yunba.yo.adapter;

import io.yunba.android.manager.YunBaManager;
import io.yunba.yo.activity.DetailActivity;
import io.yunba.yo.activity.MainActivity;
import io.yunba.yo.bean.ItemBean;
import io.yunba.yo.util.SharePrefsHelper;
import io.yunba.yo.util.YoUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.yunba.yo.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class YoAdapter extends BaseAdapter {
	public static ArrayList<ItemBean> users;
	

	private static Context mContext;

	public YoAdapter(ArrayList<ItemBean> users, Context context) {
		this.users = users;
		this.mContext = context;
	}

	
	@Override
	public int getCount() {
		return users.size() + 1;
	}

	@Override
	public Object getItem(int arg0) {
		if (arg0 == users.size())
			return null;
		return users.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.user_row, parent, false);
		}

		final EditText ex = (EditText) convertView.findViewById(R.id.rowPlusText);
		if (position == users.size()) {
			ex.setText("");
			ex.setHint(R.string.plus);
			ex.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View ex, boolean on) {
					final EditText ex1 = (EditText) ex;
					ex1.setHint(R.string.plus_text);
				}
			});
			ex.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_GO) {
						String user = v.getText().toString().trim();
						if (!YoUtil.isEmpty(user)) {
							addUser(user);
						}
						YoAdapter.this.notifyDataSetChanged();
						return false;
					}
					return false;
				}

			});
			ex.requestFocus();
			ex.setVisibility(View.VISIBLE);
		} else {
			final TextView textViewItem = (TextView) convertView
					.findViewById(R.id.rowUsername);
			LinearLayout ll = (LinearLayout) convertView
					.findViewById(R.id.user_row_swipe_buttons_layout);
			ProgressBar pb = (ProgressBar) convertView
					.findViewById(R.id.rowProgrssBar);
			Button cancel = (Button) convertView
					.findViewById(R.id.user_row_cancel_button);
			Button delete = (Button) convertView
					.findViewById(R.id.user_row_delete_button);
			Button detail = (Button) convertView
					.findViewById(R.id.user_row_detail_button);
			cancel.setOnClickListener(new ButtonClick(position,
					ButtonClick.CANCEL));
			delete.setOnClickListener(new ButtonClick(position,
					ButtonClick.DELETE));
			detail.setOnClickListener(new ButtonClick(position,
					ButtonClick.DETAIL));
			final String tv = users.get(position).value;
			textViewItem.setText(tv);
			// textViewItem.setOnClickListener(myLister);
			// object item based on the position
			switch (users.get(position).show) {
			case 0:
				textViewItem.setVisibility(View.VISIBLE);
				ex.setVisibility(View.GONE);
				// get the TextView and then set the text (item name) and tag
				// (item ID) values
				ll.setVisibility(View.GONE);
				pb.setVisibility(View.GONE);

				break;
			case 1:
				ex.setVisibility(View.GONE);
				textViewItem.setVisibility(View.GONE);
				ll.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
				break;
			case 2:
				ex.setVisibility(View.GONE);
				textViewItem.setVisibility(View.GONE);
				ll.setVisibility(View.GONE);
				pb.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}

		}
		
		return convertView;
	}

	private static void AddUserToShareprefs(ArrayList<ItemBean> users) {
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < users.size(); i++) {
			set.add(users.get(i).value);
		}
		SharePrefsHelper.setSet(mContext, YunBaManager.HISTORY_TOPICS, set);
	}

	class ButtonClick implements OnClickListener {
		public static final int CANCEL = 1;
		public static final int DELETE = 2;
		public static final int DETAIL = 3;
		public int index;
		public int action;

		public ButtonClick(int index, int action) {
			this.index = index;
			this.action = action;
		}

		@Override
		public void onClick(View arg0) {
			switch (action) {
			case 1:
				users.get(index).show = 0;
				YoAdapter.this.notifyDataSetChanged();
				break;
			case 2:
				if (users.size() > index) users.remove(index);
				AddUserToShareprefs(users);
				YoAdapter.this.notifyDataSetChanged();
				break;
			case 3:
				Intent detil = new Intent((Activity) mContext,
						DetailActivity.class);
				detil.putExtra("user_name", users.get(index).value);
				detil.putExtra("index", index);
				mContext.startActivity(detil);
				break;
			default:
				break;
			}
		}
	}
	
	
	public static boolean hasUser(String name) {
		for (int i = 0; i < users.size(); i++) {
			String oldUser = users.get(i).value;
			if(!YoUtil.isEmpty(oldUser) && oldUser.equals(name))
				return true;
		}
		return false;
	}
	
	public static void addUser(String name) {
		if(!hasUser(name)) {
			if (!YoUtil.isEmpty(name)) {
				users.add(new ItemBean(name));
				AddUserToShareprefs(users);
				if(null != MainActivity.adapter) MainActivity.adapter.notifyDataSetChanged();
			}
			
		}
	}
	
	

}
