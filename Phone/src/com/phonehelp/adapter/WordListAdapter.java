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

public class WordListAdapter extends BaseAdapter {

	private Cursor cursor;
	private ArrayList<Boolean> selectedItem;
	private LayoutInflater inflater;

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

	public WordListAdapter(LayoutInflater inflater) {
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
		View mView = convertView;
		if (mView == null) {
			mView = inflater.inflate(R.layout.word, null);
		}
		TextView wordTextView = (TextView) mView
				.findViewById(R.id.wordtextView);
		TextView sumTextView = (TextView) mView.findViewById(R.id.wordsun);
		CheckBox checkBox = (CheckBox) mView.findViewById(R.id.wordcheckBox);

		wordTextView.setText(cursor.getString(0));
		sumTextView.setText(cursor.getInt(1) + "");
		checkBox.setChecked(selectedItem.get(position));
		return mView;
	}

}
