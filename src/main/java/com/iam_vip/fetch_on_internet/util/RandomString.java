/**
 * 
 */
package com.iam_vip.fetch_on_internet.util;

/**
 * @author Colin
 *
 */
public class RandomString {

	/**
	 * 
	 */
	public RandomString() {
	}

	private int len = 8;
	private int all = 1;

	public static final String[] CODES_ALL = {
			"a",
			"A",
			"b",
			"B",
			"c",
			"C",
			"d",
			"D",
			"e",
			"E",
			"f",
			"F",
			"g",
			"G",
			"h",
			"H",
			"i",
			"I",
			"j",
			"J",
			"k",
			"K",
			"l",
			"L",
			"m",
			"M",
			"n",
			"N",
			"o",
			"O",
			"p",
			"P",
			"q",
			"Q",
			"r",
			"R",
			"s",
			"S",
			"t",
			"T",
			"u",
			"U",
			"v",
			"V",
			"w",
			"W",
			"x",
			"X",
			"y",
			"Y",
			"z",
			"Z",
			"0",
			"1",
			"2",
			"3",
			"4",
			"5",
			"6",
			"7",
			"8",
			"9" };
	private static final int LEN_ALL = CODES_ALL.length;

	public static final String[] CODES_NUM = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private static final int LEN_NUM = CODES_NUM.length;

	/**
	 * @param len
	 */
	public RandomString(int len) {
		this.len = len;
	}
	
	/**
	 * @param len
	 */
	public RandomString(int len, int all) {
		this.len = len;
		this.all = all;
	}

	/**
	 * @return
	 */
	public String toString() {

		if (all == 1) {

			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < len; ++i) {
				buf.append(CODES_ALL[(int) (Math.random() * LEN_ALL)]);
			}
			return buf.toString();

		} else {

			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < len; ++i) {
				buf.append(CODES_NUM[(int) (Math.random() * LEN_NUM)]);
			}
			return buf.toString();

		}
	}

	public static String random() {
		return random(8);
	}

	public static String random(int len) {
		return new RandomString(len).toString();
	}

	public static String randomNum() {
		return randomNum(8);
	}

	public static String randomNum(int len) {
		return new RandomString(len, 2).toString();
	}

}
