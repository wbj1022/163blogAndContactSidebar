package com.franco.sidebar;

import java.util.Comparator;

public class CompareObject implements Comparator<Object> {

	public int compare(Object arg0, Object arg1) {
		ContactName letterObject0 = (ContactName) arg0;
		ContactName letterObject1 = (ContactName) arg1;

		int char0 = letterObject0.firstChar;
		int char1 = letterObject1.firstChar;
		int flag = char0 - char1;
		if (flag == 0 && char0 == '#') {
			return letterObject0.displayName
					.compareTo(letterObject1.displayName);
		}
		return flag;

	}

}
