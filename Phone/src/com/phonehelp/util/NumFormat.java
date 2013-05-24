package com.phonehelp.util;

public class NumFormat {

	public String num;

	public String format(String num) {
		if ((num.length() > 11)
				&& ("+86".equals(num.substring(0, 3)) || "12520".equals(num
						.substring(0, 5)))) {
			return num.substring(num.length() - 11);
		}
		return num;

	}
}
