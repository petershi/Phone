package com.phonehelp.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

public class MyContacts {

	private String name;
	private String num;
	private ContentResolver contentResolver;

	public MyContacts() {
		super();
	}

	public MyContacts(ContentResolver contentResolver) {
		super();
		this.contentResolver = contentResolver;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the num
	 */
	public String getNum() {
		return num;
	}

	/**
	 * @param num
	 *            the num to set
	 */
	public void setNum(String num) {
		this.num = num;
	}

	public String getName(String num) {
		Cursor cursor = contentResolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] {
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
						ContactsContract.CommonDataKinds.Phone.NUMBER },
				ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
				new String[] { num }, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			return cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		} else {
			return null;
		}

	}

}
