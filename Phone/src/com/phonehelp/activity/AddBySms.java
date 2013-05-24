package com.phonehelp.activity;

import java.util.ArrayList;
import com.phonehelp.R;
import com.phonehelp.adapter.AddBySmsAdapter;
import com.phonehelp.db.BlacklistDBUtil;
import com.phonehelp.util.AppUtils;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ListView;
import android.widget.Toast;

public class AddBySms extends ListActivity {

	private static final String SMS_URI_INBOX = "content://sms/inbox";
	private AddBySmsAdapter adapter;
	private BlacklistDBUtil bDBUtil = new BlacklistDBUtil(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbysms_activity);
		this.setTitle(R.string.addbysms);

		Uri url = Uri.parse(SMS_URI_INBOX);
		String[] col = new String[] { "address", "date", "body" };
		Cursor cursor = getContentResolver().query(url, col, null, null, null);
		adapter = new AddBySmsAdapter(getLayoutInflater(), getContentResolver());
		adapter.setCursor(cursor);
		ListView listView = getListView();
		listView.setAdapter(adapter);
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, R.string.allselected);
				menu.add(0, 1, 1, R.string.alldisselected);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// initData();

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (adapter.getSelectedItem().get(position) == false) {
			adapter.getSelectedItem().set(position, true);
		} else {
			adapter.getSelectedItem().set(position, false);
		}
		adapter.notifyDataSetChanged();

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			for (int i = 0; i < adapter.getCount(); i++) {
				adapter.getSelectedItem().set(i, true);
			}
		} else if (item.getItemId() == 1) {
			for (int i = 0; i < adapter.getCount(); i++) {
				adapter.getSelectedItem().set(i, false);
			}
		}
		adapter.notifyDataSetChanged();
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, R.string.ok);
		menu.add(0, 1, 1, R.string.cancel);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			this.input();
			this.finish();
		} else if (item.getItemId() == 1) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	// private void initData() {
	// String[] col = new String[] { "address", "date", "body" };
	// Cursor cursor = getContentResolver().query(Uri.parse(SMS_URI_INBOX),
	// col, null, null, null);
	//
	// adapter.setCursor(cursor);
	// adapter.notifyDataSetChanged();
	//
	// }

	private void input() {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		Cursor cursor = adapter.getCursor();
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getSelectedItem().get(i)) {
				cursor.moveToPosition(i);
				String num = cursor.getString(0);
				list.add(num);

			}

		}
		list = AppUtils.removesame(list);
		for (int i = 0; i < list.size(); i++) {
			if (bDBUtil.isNumExist(list.get(i))) {
				Toast.makeText(this,
						list.get(i) + getResources().getString(R.string.exist),
						Toast.LENGTH_SHORT).show();
				list2.add(list.get(i));
			} else {
				list1.add(list.get(i));
			}
		}
		bDBUtil.addNum(list1);
		bDBUtil.setNumTag1(list2);
		bDBUtil.closeDB();
		Toast.makeText(this, getResources().getString(R.string.successed),
				Toast.LENGTH_SHORT).show();
	}

}
