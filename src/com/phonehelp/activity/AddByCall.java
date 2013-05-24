package com.phonehelp.activity;

import java.util.ArrayList;

import com.phonehelp.R;
import com.phonehelp.adapter.AddByCallAdapter;
import com.phonehelp.db.BlacklistDBUtil;
import com.phonehelp.util.AppUtils;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;

public class AddByCall extends Activity implements OnClickListener {

	private AddByCallAdapter mAdapter;
	private BlacklistDBUtil bDBUtil = new BlacklistDBUtil(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.addbycall_activity);
		this.setTitle(R.string.addbycalllog);

		Cursor cursor = getContentResolver().query(
				CallLog.Calls.CONTENT_URI,
				new String[] { CallLog.Calls.CACHED_NAME, CallLog.Calls.DATE,
						CallLog.Calls.NUMBER, CallLog.Calls.TYPE }, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);

		mAdapter = new AddByCallAdapter(getLayoutInflater());
		mAdapter.setCursor(cursor);
		ListView listView = (ListView) findViewById(R.id.addbycall_activity_list);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ArrayList<Boolean> arrayList = mAdapter.getSelectedItem();
				if (arrayList.get(position)) {
					arrayList.set(position, false);
				} else {
					arrayList.set(position, true);
				}
				mAdapter.notifyDataSetChanged();
			}
		});
		((CheckBox) findViewById(R.id.addbycall_activity_all_select))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						ArrayList<Boolean> arrayList = mAdapter
								.getSelectedItem();

						for (int i = 0; i < arrayList.size(); i++) {
							arrayList.set(i, isChecked);
						}
						mAdapter.notifyDataSetChanged();

					}
				});
		findViewById(R.id.addbycall_activity_ok).setOnClickListener(this);
		findViewById(R.id.addbycall_activity_cancel).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void input() {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		bDBUtil.open();
		Cursor cursor = mAdapter.getCursor();
		for (int i = 0; i < mAdapter.getCount(); i++) {
			if (mAdapter.getSelectedItem().get(i)) {

				cursor.moveToPosition(i);
				String num = cursor.getString(3);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addbycall_activity_ok:
			input();
			finish();
			break;
		case R.id.addbycall_activity_cancel:
			finish();
			break;
		default:
			break;
		}
	}

}
