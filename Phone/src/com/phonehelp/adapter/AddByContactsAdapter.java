package com.phonehelp.adapter;

import java.util.ArrayList;
import com.phonehelp.R;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AddByContactsAdapter extends BaseAdapter {

	private Cursor cursor;
	private ArrayList<Boolean> selectedItem;
	private LayoutInflater inflater;

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

	public AddByContactsAdapter(LayoutInflater inflater) {
		super();
		this.inflater = inflater;

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
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.addbycontacts, null);
			holder = new ViewHolder();
			holder.nameTextView = (TextView) view
					.findViewById(R.id.addbycontactsname);
			holder.numTextView = (TextView) view
					.findViewById(R.id.addbycontactsnum);
			holder.checkBox = (CheckBox) view
					.findViewById(R.id.addbycontactscheckBox);
			view.setTag(holder);
		}
		holder = (ViewHolder) view.getTag();

		holder.nameTextView.setText(cursor.getString(0));
		holder.numTextView.setText(cursor.getString(1));
		holder.checkBox.setChecked(selectedItem.get(position));
		return view;
	}
}
