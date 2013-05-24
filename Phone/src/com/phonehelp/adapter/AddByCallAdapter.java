package com.phonehelp.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.phonehelp.R;
import com.phonehelp.util.NumFormat;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AddByCallAdapter extends BaseAdapter {

	private Cursor cursor;
	private ArrayList<Boolean> selectedItem;
	private LayoutInflater inflater;
	private NumFormat nf;
	private SimpleDateFormat sdf;

	public void setCursor(Cursor cursor) {
		selectedItem = new ArrayList<Boolean>();
		for (int i = 0; i < cursor.getCount(); i++) {
			selectedItem.add(false);
		}
		this.cursor = cursor;
	}

	public Cursor getCursor() {
		return cursor;
	}

	public ArrayList<Boolean> getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ArrayList<Boolean> selectedItem) {
		this.selectedItem = selectedItem;
	}

	public AddByCallAdapter(LayoutInflater inflater) {
		super();
		this.inflater = inflater;
		nf = new NumFormat();
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			mView = inflater.inflate(R.layout.addbycall, parent, false);
			holder = new ViewHolder();
			holder.nameTextView = (TextView) mView
					.findViewById(R.id.addbycallname);
			holder.dateTextView = (TextView) mView
					.findViewById(R.id.addbycalldate);
			holder.numTextView = (TextView) mView
					.findViewById(R.id.addbycallnumber);
			holder.typeTextView = (TextView) mView
					.findViewById(R.id.addbycalltype);
			holder.checkBox = (CheckBox) mView
					.findViewById(R.id.addbycallcheckBox);
			mView.setTag(holder);
		}
		holder = (ViewHolder) mView.getTag();

		if (cursor.getString(0) != null) {
			holder.nameTextView.setText(cursor.getString(0));
		}
		Date date = new Date(Long.parseLong(cursor.getString(1)));
		String time = sdf.format(date);
		holder.dateTextView.setText(time);

		holder.numTextView.setText(nf.format(cursor.getString(2)));
		if ("1".equals(cursor.getString(3))) {
			holder.typeTextView.setText(R.string.incoming);
		} else if ("2".equals(cursor.getString(3))) {
			holder.typeTextView.setText(R.string.outgoing);
		} else {
			holder.typeTextView.setText(R.string.missed);
		}

		holder.checkBox.setChecked(selectedItem.get(position));

		return mView;
	}
}
