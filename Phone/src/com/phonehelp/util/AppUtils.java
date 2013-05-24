package com.phonehelp.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import android.content.Context;
import android.widget.Toast;

public class AppUtils {
	public static ArrayList<String> removesame(ArrayList<String> list) {

		HashSet<String> set = new HashSet<String>();
		ArrayList<String> newList = new ArrayList<String>();
		for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
			String element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		return newList;
	}

	public static boolean isEmpty(String s) {
		if (s == null || s.trim().length() == 0) {
			return true;
		} else
			return false;
	}

	public static void showToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
}
