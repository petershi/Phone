package com.phonehelp.activity;

import java.util.ArrayList;
import java.util.Date;

import com.phonehelp.R;
import com.phonehelp.adapter.SmsListAdapter;
import com.phonehelp.db.SmsDBUtil;

import android.app.ListActivity;
import android.content.ContentValues;
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

@Deprecated
public class SmsListActivity extends ListActivity {
	private static final String SMS_URI_INBOX = "content://sms/inbox";
	private SmsDBUtil sDBUtil;
	private SmsListAdapter sAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.smslist_activity);
		sDBUtil = new SmsDBUtil(this);
		Cursor cursor = sDBUtil.querySMS();
		sAdapter = new SmsListAdapter(getLayoutInflater(), getContentResolver());
		sAdapter.setCursor(cursor);
		ListView listView = getListView();
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, R.string.allselected);
				menu.add(0, 1, 1, R.string.alldisselected);
			}
		});

		listView.setAdapter(sAdapter);
		sDBUtil.closeDB();
		// refresh();

	}

	@Override
	protected void onResume() {
		// refresh();
		super.onResume();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			for (int i = 0; i < sAdapter.getSelectedItem().size(); i++) {
				sAdapter.getSelectedItem().set(i, true);
			}
		} else if (item.getItemId() == 1) {
			for (int i = 0; i < sAdapter.getSelectedItem().size(); i++) {
				sAdapter.getSelectedItem().set(i, false);
			}
		}
		sAdapter.notifyDataSetChanged();
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, R.string.refresh);
		menu.add(0, 1, 1, R.string.restore);
		menu.add(0, 2, 2, R.string.delete);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			refresh();

		} else if (item.getItemId() == 1) {
			this.restore();
			this.refresh();

		} else if (item.getItemId() == 2) {
			this.deleteList();
			this.refresh();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		if (sAdapter.getSelectedItem().get(position)) {
			sAdapter.getSelectedItem().set(position, false);
		} else {
			sAdapter.getSelectedItem().set(position, true);
		}

		sAdapter.notifyDataSetChanged();
		super.onListItemClick(l, v, position, id);
	}

	private void refresh() {
		Cursor cursor = sDBUtil.querySMS();
		sAdapter.setCursor(cursor);
		sAdapter.notifyDataSetChanged();
		sDBUtil.closeDB();
	}

	private void deleteList() {
		ArrayList<Date> dates = new ArrayList<Date>();
		for (int i = 0; i < sAdapter.getCount(); i++) {
			if (sAdapter.getSelectedItem().get(i)) {
				sAdapter.getCursor().moveToPosition(i);
				Date date = new Date(sAdapter.getCursor().getLong(1));
				dates.add(date);
			}
			sDBUtil.deleteRecord(dates);
			sDBUtil.closeDB();
		}
	}

	private void restore() {
		Uri url = Uri.parse(SMS_URI_INBOX);
		ArrayList<Date> dates = new ArrayList<Date>();
		// String[] col = new String[] {"address","body","date"};
		for (int i = 0; i < sAdapter.getCount(); i++) {
			if (sAdapter.getSelectedItem().get(i)) {
				sAdapter.getCursor().moveToPosition(i);
				String num = sAdapter.getCursor().getString(0);
				Date date = new Date(sAdapter.getCursor().getLong(1));
				dates.add(date);
				String content = sAdapter.getCursor().getString(2);
				try {
					ContentValues values = new ContentValues();
					values.put("address", num);
					values.put("body", content);
					values.put("date", date + "");
					getContentResolver().insert(url, values);

				} catch (Exception e) {
				}
			}
			sDBUtil.deleteRecord(dates);
			sDBUtil.closeDB();

		}
	}

}
