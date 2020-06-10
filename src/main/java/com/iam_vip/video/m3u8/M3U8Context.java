package com.iam_vip.video.m3u8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class M3U8Context {
	
	private String url;
	private File f;
	
	private String[] cmds;

	public M3U8Context(String m3u8, IFileGetter get) {
		url = m3u8;
		File exists = get.getFile();
		if (exists.isFile()) {
			f = exists;
		} else {
			f = exists.listFiles((d, n) -> n.endsWith("m3u8"))[0];
		}
	}
	
	public String[] getCmds() {
		return cmds;
	}
	
	public String getCmd() {
		return cmds[0];
	}
	
	public String[] get() {
		List<String> links = links();
		if (links == null || links.isEmpty()) {
			return new String[0];
		}
		createCommand(links);
		String prefix = identify(links.get(0));
		return links.stream().map(s -> prefix + s).toArray(String[]::new);
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
	
	private void createCommand(List<String> links) {
		int[] ints = split(links.size());
		cmds = new String[ints.length];
		int i = 0;
		for (int n : ints) {
			int l = n % 10000;
			int k = n / 10000 % 10000;
			String concats = links.stream().skip(k).limit(l).map(s -> s.substring(s.lastIndexOf("/") + 1)).reduce((v1, v2) -> v1 + "|" + v2).get();
			String out = " ../" + f.getParentFile().getName().replace(" ", "_") + (ints.length>1?("-"+(1000+i+1)):"") + ".mp4";
			cmds[i] = "ffmpeg -i \"concat:" + concats + "\" -acodec copy -vcodec copy -absf aac_adtstoasc " + out;
			writeLine(cmds[i++]);
		}
		close();
	}
	
	private BufferedWriter writer;
	
	private void close() {
		if (writer != null) {
			try {
				writer.close();
				writer = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeLine(String line) {
		try {
			if (writer == null) {
				File cmd = new File(f.getParent(), f.getName() + ".txt");
				writer = new BufferedWriter(new FileWriter(cmd, true));
				writer.write("###===" + cmd.getAbsolutePath());
				writer.write("\r\n");
			}
			writer.write(line);
			writer.write("\r\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String identify(String link) {
		int n = url.lastIndexOf("/") + 1;
		if (link.startsWith("/")) {
			n = url.indexOf("/", 10);
		}
		if (link.startsWith("http")) {
			return "";
		}
		return url.substring(0, n);
	}

	private List<String> links() {
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			List<String> list = new ArrayList<>(100);
			String line = null;
			int index = -1;
			while ( (line = reader.readLine()) != null ) {
				line = line.trim();
				if (line.startsWith("#")) {
					continue;
				}
				if ( (index = line.indexOf("#")) > 0) {
					line = line.substring(0, index);
				}
				list.add(line.trim());
			}
			return list;
		} catch (Exception e) {
		}
		return null;
	}

}
