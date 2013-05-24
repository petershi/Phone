package com.phonehelp.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.phonehelp.R;
import com.phonehelp.util.MyContacts;
import com.phonehelp.util.NumFormat;

import android.content.ContentResolver;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AddBySmsAdapter extends BaseAdapter {

	private Cursor cursor;
	private LayoutInflater inflater;
	private MyContacts myContacts;
	private ArrayList<Boolean> selectedItem;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private NumFormat nf = new NumFormat();

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		selectedItem = new ArrayList<Boolean>();
		for (int i = 0; i < cursor.getCount(); i++) {
			selectedItem.add(false);
		}
		this.cursor = cursor;
	}

	public ArrayList<Boolean> getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ArrayList<Boolean> selectedItem) {
		this.selectedItem = selectedItem;
	}

	public AddBySmsAdapter(LayoutInflater inflater,
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		cursor.moveToPosition(position);
		ViewHolder holder;
		View mView = convertView;
		if (mView == null) {
			mView = inflater.inflate(R.layout.addbysms, parent, false);
			holder = new ViewHolder();
			holder.numTextView = (TextView) mView
					.findViewById(R.id.addbysmsnum);
			holder.nameTextView = (TextView) mView
					.findViewById(R.id.addbysmsname);
			holder.dateTextView = (TextView) mView
					.findViewById(R.id.addbysmsdate);
			holder.bodyTextView = (TextView) mView
					.findViewById(R.id.addbysmsbody);
			holder.checkBox = (CheckBox) mView
					.findViewById(R.id.addbysmscheckBox);
			mView.setTag(holder);
		}
		holder = (ViewHolder) mView.getTag();

		String num = cursor.getString(0);
		num = nf.format(num);
		holder.numTextView.setText(num);

		if (myContacts.getName(num) != null) {
			holder.nameTextView.setText(myContacts.getName(num));
		}

		Date date = new Date(cursor.getLong(1));
		String time = sdf.format(date);
		holder.dateTextView.setText(time);
		holder.bodyTextView.setText(cursor.getString(2));
		holder.checkBox.setChecked(selectedItem.get(position));
		return mView;
	}

}
