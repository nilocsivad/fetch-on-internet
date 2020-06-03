package com.iam_vip.video.m3u8;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

public class ExtTestM3U8 {

	public ExtTestM3U8() {
	}
	
	@Test
	public void splite2() {
		int[] ints = split2(500);
		for (int n : ints) {
			int l = n % 10000;
			int k = n / 10000 % 10000;
			System.out.println(n + "---" + k + "---" + l);
		}
	}
	
	private int[] split2(int n) {
		int slice_l = 100;
		int left = n % slice_l;
		int group_l = n / slice_l + (left == 0 ? 0 : 1);
		int[] ints = new int[group_l];
		left = n % group_l;
		int part_l = n / group_l;
		for (int i = 0; i < group_l; ++i) {
			int start = i * part_l;
			int value = start * 10000 + part_l;
			ints[i] = value;
		}
		ints[group_l - 1] += left;
		return ints;
	}
	
	private int[] split(int n) {
		int x = 100;
		int m = n % x;
		int g = n / x;
		int[] s = new int[g + (m>0?1:0)];
		for (int i = 0; i < g; ++i) {
			int t = i * x;
			int f = t * 10000 + x;
			s[i] = f;
		}
		if (m > 0) {
			s[s.length - 1] = (n-m) * 10000 + m;
		}
		return s;
	}
	
	@Test
	public void m1() {
		String[] ss = Stream.of("1010", "1000", "1100", "1101", "0111", "0110", "1110", "0101").toArray(String[]::new);
		List<String> links = Arrays.asList(ss);
		int[] ints = split(links.size());
		for (int n : ints) {
			int l = n % 10000;
			int k = n / 10000 % 10000;
			System.out.println(n + "---" + k + "---" + l);
			String concats = links.stream().skip(k).limit(l).map(s -> s.substring(s.lastIndexOf("/") + 1)).reduce((v1, v2) -> v1 + "|" + v2).get();
			System.out.println(concats);
		}
	}
	
	@Test
	public void m2() {
		File dir = new File("D:\\tmp");
		File[] files = dir.listFiles((d, n) -> n.startsWith("m3u8-"));
		for (File file : files) {
			file.renameTo(new File(dir, file.getName().replace("m3u8-", "")));
		}
	}

}
