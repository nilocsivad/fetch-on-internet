package com.iam_vip.video.m3u8;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.iam_vip.fetch_on_internet.util.RandomString;

public class FileDownloader implements IFileGetter {
	
	protected String src;
	protected File file;

	public FileDownloader(String url, String path) {
		this(url, path, url.substring(url.lastIndexOf("/")+1));
	}
	
	public FileDownloader(String url, String path, String name) {
		src = url;
		file = new File(path, name);
	}
	
	public File getFile() {
		return file;
	}

	public String getPath() {
		return file.getAbsolutePath();
	}

	public int download() {
		return download(3);
	}

	public int download(int retry) {
		if (retry > 0) {
			try {
				URL url = new URL(src);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(30 * 1000);
					conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
					conn.setRequestProperty("Auth-" + RandomString.random(), RandomString.random(24));
				BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
				byte[] buf = new byte[ 24 * 1024 * 1024 ];
				int len = 0;
				while (( len = in.read(buf) ) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				return 1;
			} catch(Exception ex) {
				return download(retry - 1);
			}
		}
		return 0;
	}

}
