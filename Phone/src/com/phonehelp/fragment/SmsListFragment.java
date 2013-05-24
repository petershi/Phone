package com.phonehelp.fragment;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import com.phonehelp.R;
import com.phonehelp.adapter.SmsListAdapter;
import com.phonehelp.db.SmsDBUtil;

public class SmsListFragment extends Fragment implements OnClickListener {
	public static final String TAG = SmsListFragment.class.getName();
	private static final String SMS_URI_INBOX = "content://sms/inbox";
	private SmsDBUtil sDBUtil;
	private SmsListAdapter sAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.smslist_fragment, container,
				false);
		sDBUtil = new SmsDBUtil(getActivity());
		Cursor cursor = sDBUtil.querySMS();
		sAdapter = new SmsListAdapter(inflater, getActivity()
				.getContentResolver());
		sAdapter.setCursor(cursor);
		ListView listView = (ListView) view
				.findViewById(R.id.smslistfragment_list);
		listView.setAdapter(sAdapter);
		sDBUtil.closeDB();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (sAdapter.getSelectedItem().get(position)) {
					sAdapter.getSelectedItem().set(position, false);
				} else {
					sAdapter.getSelectedItem().set(position, true);
				}
				sAdapter.notifyDataSetChanged();
			}
		});
		((CheckBox) view.findViewById(R.id.smslistfragment_all_select))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						for (int i = 0; i < sAdapter.getSelectedItem().size(); i++) {
							sAdapter.getSelectedItem().set(i, isChecked);
						}
						sAdapter.notifyDataSetChanged();
					}
				});

		view.findViewById(R.id.smslistfragment_delete).setOnClickListener(this);
		view.findViewById(R.id.smslistfragment_refresh)
				.setOnClickListener(this);
		view.findViewById(R.id.smslistfragment_restore)
				.setOnClickListener(this);

		return view;

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
					getActivity().getContentResolver().insert(url, values);

				} catch (Exception e) {
				}
			}
			sDBUtil.deleteRecord(dates);
			sDBUtil.closeDB();

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.smslistfragment_delete:
			deleteList();
			refresh();
			break;
		case R.id.smslistfragment_refresh:
			refresh();
			break;
		case R.id.smslistfragment_restore:
			restore();
			refresh();
			break;

		default:
			break;
		}
	}
}
