package com.phonehelp.activity;

import java.util.ArrayList;

import com.phonehelp.R;
import com.phonehelp.adapter.WordListAdapter;
import com.phonehelp.db.WordDBUtil;
import com.phonehelp.util.AppUtils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.EditText;
import android.widget.ListView;

public class WordActivity extends ListActivity {

	private WordDBUtil wDBUtil;
	private WordListAdapter wAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.word_activity);
		this.setTitle(R.string.keyword);
		ListView listView = this.getListView();
		wDBUtil = new WordDBUtil(this);
		wDBUtil.open();
		Cursor cursor = wDBUtil.query();
		wAdapter = new WordListAdapter(getLayoutInflater());
		wAdapter.setCursor(cursor);
		wDBUtil.closeDB();
		listView.setAdapter(wAdapter);
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
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			for (int i = 0; i < wAdapter.getSelectedItem().size(); i++) {
				wAdapter.getSelectedItem().set(i, true);
			}

		} else if (item.getItemId() == 1) {
			for (int i = 0; i < wAdapter.getSelectedItem().size(); i++) {
				wAdapter.getSelectedItem().set(i, false);
			}
		}

		wAdapter.notifyDataSetChanged();
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, R.string.refresh);
		menu.add(0, 1, 1, R.string.add);
		menu.add(0, 2, 2, R.string.delete);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			this.refresh();
		} else if (item.getItemId() == 1) {
			this.add();
			this.refresh();
		} else if (item.getItemId() == 2) {
			this.delete();
			this.refresh();
		}
		return super.onOptionsItemSelected(item);
	}

	private void refresh() {
		wDBUtil.open();
		Cursor cursor = wDBUtil.query();
		wAdapter.setCursor(cursor);
		wAdapter.notifyDataSetChanged();
		wDBUtil.closeDB();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (wAdapter.getSelectedItem().get(position)) {
			wAdapter.getSelectedItem().set(position, false);
		} else {
			wAdapter.getSelectedItem().set(position, true);
		}
		wAdapter.notifyDataSetChanged();
		super.onListItemClick(l, v, position, id);
	}

	private void add() {
		LayoutInflater inflater = getLayoutInflater();
		View textEntryView = inflater.inflate(R.layout.word_entry_dialog, null);
		final EditText entryText = (EditText) textEntryView
				.findViewById(R.id.word_entry_editText);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.add);
		builder.setView(textEntryView);

		builder.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String entryTextStr = entryText.getText().toString().trim();

				if (AppUtils.isEmpty(entryTextStr)) {
					AppUtils.showToast(WordActivity.this, getResources()
							.getString(R.string.inputerror));

				} else {
					if (wDBUtil.isExist(entryTextStr)) {
						AppUtils.showToast(WordActivity.this, entryTextStr
								+ getResources().getString(R.string.exist));

					}

					wDBUtil.addWord(entryTextStr);
					wDBUtil.closeDB();
					refresh();
				}

			}
		});

		builder.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}

	private void delete() {
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < wAdapter.getCount(); i++) {
			if (wAdapter.getSelectedItem().get(i)) {
				wAdapter.getCursor().moveToPosition(i);
				list.add(wAdapter.getCursor().getString(0));
				// wDBUtil.delete(wAdapter.cursor.getString(0));
			}
		}
		wDBUtil.open();
		wDBUtil.delete(list);
		wDBUtil.closeDB();

	}
}
