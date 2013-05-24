package com.phonehelp.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.phonehelp.R;
import com.phonehelp.util.MyContacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CallListAdapter extends BaseAdapter {

	private Cursor cursor;
	private ArrayList<Boolean> selectedItem;
	private LayoutInflater inflater;
	private MyContacts myContacts;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		selectedItem = new ArrayList<Boolean>();
		for (int i = 0; i < cursor.getCount(); i++) {
			this.selectedItem.add(false);
		}
		this.cursor = cursor;
	}

	public ArrayList<Boolean> getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ArrayList<Boolean> selectedItem) {
		this.selectedItem = selectedItem;
	}

	public CallListAdapter(LayoutInflater inflater,
			ContentResolver contentResolver) {
		super();
		this.inflater = inflater;
		myContacts = new MyContacts(contentResolver);
	}

	@Override
	public int getCount() {
		return cursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		cursor.moveToPosition(position);
		View mView = convertView;
		if (mView == null) {
			mView = inflater.inflate(R.layout.calllist_item, parent, false);
		}
		TextView numTextView = (TextView) mView.findViewById(R.id.callnum);
		TextView dateTextView = (TextView) mView.findViewById(R.id.calldate);
		TextView nameTextView = (TextView) mView.findViewById(R.id.callname);
		CheckBox checkBox = (CheckBox) mView.findViewById(R.id.callcheckbox);
		checkBox.setChecked(selectedItem.get(position));
		numTextView.setText(cursor.getString(0));
		Date date = new Date(cursor.getLong(1));
		String time = sdf.format(date);
		dateTextView.setText(time);
		String name = myContacts.getName(cursor.getString(0));
		if (name != null) {
			nameTextView.setText(name);
		}
		return mView;
	}

}
