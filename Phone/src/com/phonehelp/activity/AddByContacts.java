package com.phonehelp.activity;

import java.util.ArrayList;
import com.phonehelp.R;
import com.phonehelp.adapter.AddByContactsAdapter;
import com.phonehelp.db.BlacklistDBUtil;
import com.phonehelp.util.AppUtils;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ListView;
import android.widget.Toast;

public class AddByContacts extends ListActivity {

	private AddByContactsAdapter adapter;
	private BlacklistDBUtil bDBUtil = new BlacklistDBUtil(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.addbycontacts_activity);
		this.setTitle(R.string.addbycontacts);

		Cursor cursor = this.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] {
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
						ContactsContract.CommonDataKinds.Phone.NUMBER }, null,
				null, null);
		adapter = new AddByContactsAdapter(getLayoutInflater());
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
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			for (int i = 0; i < adapter.getSelectedItem().size(); i++) {
				adapter.getSelectedItem().set(i, true);
			}
		} else {
			for (int i = 0; i < adapter.getSelectedItem().size(); i++) {
				adapter.getSelectedItem().set(i, false);
			}
		}
		adapter.notifyDataSetChanged();
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (adapter.getSelectedItem().get(position)) {
			adapter.getSelectedItem().set(position, false);
		} else {
			adapter.getSelectedItem().set(position, true);
		}
		adapter.notifyDataSetChanged();
		super.onListItemClick(l, v, position, id);
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
		} else {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void input() {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		Cursor cursor = adapter.getCursor();
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getSelectedItem().get(i)) {
				cursor.moveToPosition(i);
				String num = cursor.getString(1);
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
