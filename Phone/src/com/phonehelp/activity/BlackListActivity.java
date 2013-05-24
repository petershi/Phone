package com.phonehelp.activity;

import java.util.ArrayList;

import com.phonehelp.R;
import com.phonehelp.adapter.BlackListAdapter;
import com.phonehelp.db.BlacklistDBUtil;
import com.phonehelp.util.AppUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class BlackListActivity extends Activity implements
		android.view.View.OnClickListener {

	private BlacklistDBUtil bDBUtil;
	private String entryTextStr;
	private EditText entryText;

	private BlackListAdapter bAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.blacklist_activity);
		this.setTitle(R.string.blacklist);

		bDBUtil = new BlacklistDBUtil(this);
		bDBUtil.open();

		Cursor cursor = bDBUtil.queryNum();
		bAdapter = new BlackListAdapter(getLayoutInflater(),
				getContentResolver());
		bAdapter.setCursor(cursor);
		ListView listView = (ListView) findViewById(R.id.blacklist_activity_list);
		listView.setAdapter(bAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (bAdapter.getSelectedItem().get(position)) {
					bAdapter.getSelectedItem().set(position, false);
				} else {
					bAdapter.getSelectedItem().set(position, true);
				}
				bAdapter.notifyDataSetChanged();
			}
		});
		bDBUtil.closeDB();

		((CheckBox) findViewById(R.id.blacklist_activity_all_select))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int size = bAdapter.getSelectedItem().size();
						for (int i = 0; i < size; i++) {
							bAdapter.getSelectedItem().set(i, isChecked);
						}
						bAdapter.notifyDataSetChanged();
					}
				});
		findViewById(R.id.blacklist_activity_refresh).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// menu.add(0, 1, 1, R.string.refresh);
	// SubMenu subMenu = menu.addSubMenu(0, 2, 2, R.string.add);
	// subMenu.add(1, 11, 1, R.string.addbyedit);
	// subMenu.add(1, 12, 2, R.string.addbycalllog);
	// subMenu.add(1, 13, 3, R.string.addbysms);
	// subMenu.add(1, 14, 4, R.string.addbycontacts);
	// menu.add(0, 3, 3, R.string.delete);
	//
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem arg0) {
	// if (arg0.getItemId() == 1) {
	// this.refresh();
	//
	// } else if (arg0.getItemId() == 11) {
	// // SubMenu subMenu = arg0.getSubMenu();
	//
	// this.createDialog();
	//
	// } else if (arg0.getItemId() == 3) {
	//
	// this.deleteList();
	// this.refresh();
	// } else if (arg0.getItemId() == 12) {
	// Intent intent = new Intent(this, AddByCall.class);
	// this.startActivity(intent);
	// } else if (arg0.getItemId() == 13) {
	// Intent intent = new Intent(this, AddBySms.class);
	// this.startActivity(intent);
	// } else if (arg0.getItemId() == 14) {
	// Intent intent = new Intent(this, AddByContacts.class);
	// this.startActivity(intent);
	// }
	// return super.onOptionsItemSelected(arg0);
	// }

	/**
	 * refresh the list
	 */
	private void refresh() {

		bDBUtil.open();

		Cursor cursor = bDBUtil.queryNum();
		bAdapter.setCursor(cursor);
		bAdapter.notifyDataSetChanged();

		bDBUtil.closeDB();

	}

	private void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create a AlertGialog
	 */
	private void createDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View textEntryView = inflater.inflate(R.layout.blacklist_entry_dialog,
				(ViewGroup) findViewById(R.id.blacklist_entry_dialog));
		entryText = (EditText) textEntryView
				.findViewById(R.id.blacklist_entryText);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.add);
		builder.setView(textEntryView);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						entryTextStr = entryText.getText().toString().trim();

						if (AppUtils.isEmpty(entryTextStr)) {
							// Toast.makeText(this, R.string.inputerror,
							// Toast.LENGTH_SHORT).show();
							showToast(getResources().getString(
									R.string.inputerror));

						} else {
							if (bDBUtil.isNumExist(entryTextStr)) {
								showToast(entryTextStr
										+ getResources().getString(
												R.string.exist));

							}

							bDBUtil.addNum(entryTextStr);
							bDBUtil.closeDB();
							refresh();
						}
						// mAdapter.addNum(entryTextStr);
						// refresh();

					}

				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.create().show();
	}

	private void deleteList() {
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < bAdapter.getCount(); i++) {
			if (bAdapter.getSelectedItem().get(i)) {
				bAdapter.getCursor().moveToPosition(i);
				list.add(bAdapter.getCursor().getString(0));
			}
		}
		bDBUtil.open();
		bDBUtil.deleteNum(list);
		bDBUtil.closeDB();

	}

	@Override
	public void onClick(View v) {

	}

}
