package com.iam_vip.video.m3u8;

public class ThreadDownloader extends FileDownloader implements Runnable {

	public ThreadDownloader(String url, String path) {
		super(url, path);
	}

	@Override
	public void run() { 
		download(); 
		System.out.println(getPath());
	}

}
