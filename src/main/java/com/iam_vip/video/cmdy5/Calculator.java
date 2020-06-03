package com.iam_vip.video.cmdy5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.iam_vip.video.m3u8.FileDownloader;

public class Calculator {
	
	private String name;
	private String href;
	private File m3u8;
	private String[] pures;

	public Calculator(String name, String href) {
		this.name = name;
		this.href = href;
	}
	
	public Calculator(String name, File m3u8) {
		this.name = name;
		this.m3u8 = m3u8;
	}
	
	private File download() {
		if (m3u8 == null) {
			FileDownloader downloader = new FileDownloader(href, System.getProperty("java.io.tmpdir"), name + "." + System.currentTimeMillis() + ".1");
			if (downloader.download() == 1) {
				m3u8 = downloader.getFile();
			} else {
				System.err.println("文件下载失败！");
			}
		}
		return m3u8;
	}
	
	private List<String> read() {
		File f = download();
		if (f == null) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			String line = null;
			while ( (line = reader.readLine()) != null ) {
				list.add(line);
			}
			f.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public String[] gets() {
		if (pures != null) {
			return pures;
		}
		pures = read().stream()
				.filter(s -> !s.startsWith("#"))
				.map(s -> { 
					int i = s.indexOf("#");
					if (i > 0) {
						return s.subSequence(0, i);
					}
					return s;
				})
				.toArray(String[]::new);
		String first = pures[0];
		int i = first.indexOf("m3u8");
		if (i > 0) { /// 转发 
			if (first.startsWith("http")) { /// 完全转发
				return new Calculator(name, first).gets();
			} else if (first.startsWith("/")) { /// 相对根路径转发
				String http = href.substring(0, href.indexOf("/", 10));
				return new Calculator(name, http + first).gets();
			} else { /// 当前路径转发
				String http = href.substring(0, href.lastIndexOf("/", 10) + 1);
				return new Calculator(name, http + first).gets();
			}
		} else { /// 最终文件 
			String prefix = null;
			if (first.startsWith("http")) { /// 完全路径
			} else if (first.startsWith("/")) { /// 相对根路径
				prefix = href.substring(0, href.indexOf("/", 10));
			} else { /// 当前路径
				prefix = href.substring(0, href.lastIndexOf("/", 10) + 1);
			}
			if (prefix != null) {
				for (int n = 0, l = pures.length; n < l; ++n) {
					pures[n] = prefix + pures[n];
				}
			}
			return pures;
		}
	}
	
	//private int[] split(int n) {
	//	int slice_l = 100;
	//	int left = n % slice_l;
	//	int group_l = n / slice_l + (left == 0 ? 0 : 1);
	//	int[] ints = new int[group_l];
	//	left = n % group_l;
	//	int part_l = n / group_l;
	//	for (int i = 0; i < group_l; ++i) {
	//		int start = i * part_l;
	//		int value = start * 10000 + part_l;
	//		ints[i] = value;
	//	}
	//	ints[group_l - 1] += left;
	//	return ints;
	//}
	
	public String cmd() {
		if (pures == null) {
			gets();
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(m3u8.getAbsolutePath() + ".dir", "list.txt")))) {
			for (String s : pures) {
				writer.write("file '" + s.substring(s.lastIndexOf("/") + 1) + "'");
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//int[] ints = split(pures.length);
		//String[] cmds = new String[ints.length];
		//int i = 0;
		//for (int n : ints) {
		//	int l = n % 10000;
		//	int k = n / 10000 % 10000;
		//	String concats = Arrays.asList(pures).stream().skip(k).limit(l).map(s -> s.substring(s.lastIndexOf("/") + 1)).reduce((v1, v2) -> v1 + "|" + v2).get();
		//	String out = " " + name + "-" + (1000+i+1) + ".mp4";
		//	cmds[i++] = "ffmpeg -i \"concat:" + concats + "\" -acodec copy -vcodec copy -absf aac_adtstoasc " + out;
		//}
		//return cmds;
		return "ffmpeg -f concat -i list.txt -c copy ../" + name + ".mp4";
	}
	

}
