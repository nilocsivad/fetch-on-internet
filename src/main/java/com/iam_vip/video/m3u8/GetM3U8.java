package com.iam_vip.video.m3u8;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class GetM3U8 {

	public GetM3U8() {
	}
	
	@Test
	public void executeByFile() throws Exception {
File cd = new File("/tmp");
File dir = new File(cd, "");
M3U8Context ctx = new M3U8Context(
	"", ()->dir);
		
		
		String[] links = ctx.get();
		
		download(dir.getAbsolutePath(), links);
		
		System.out.println("===============================================");
		Thread.sleep(5 * 1000);
		
		ffmpeg(dir, ctx.getCmds());

		System.out.println("===============================================");
		Thread.sleep(5 * 1000);
		clear(dir);
		
	}
	
	@Test
	public void executeByURL() throws Exception {
		String[] datas = {
		};
		for (int i = 0; i < datas.length; ++i) {
			download_ts(datas[i], datas[++i].trim());
		}
	}
	
	public void download_ts(String m3u8_http, String name) throws Exception {
		
		String path = "/tmp/m3u8-" + name;
		File dir = new File(path);
			 dir.mkdirs();
		
		FileDownloader dn = new FileDownloader(m3u8_http, path);
		int et1 = dn.download();
		if (et1 != 1) {
			System.err.println("Fail to download file from " + m3u8_http);
			return;
		}
		System.out.println(dn.getPath());
		
		M3U8Context ctx = new M3U8Context(m3u8_http, dn);
		String[] links = ctx.get();
		
		download(path, links);
		
		System.out.println("===============================================");
		Thread.sleep(5 * 1000);
		
		ffmpeg(dir, ctx.getCmds());

		System.out.println("===============================================");
		Thread.sleep(5 * 1000);
		clear(dir);
		
	}

	private void clear(File dir) {
		File[] fs = dir.listFiles();
		for (File f : fs) {
			f.delete();
		}
		dir.delete();
	}

	private void download(String path, String[] links) throws InterruptedException {
		ExecutorService pool = Executors.newWorkStealingPool();
		for (String link : links) {
			Runnable r = new ThreadDownloader(link, path);
			pool.execute(r);
		}
		pool.shutdown();
		while (!pool.isTerminated()) {
			Thread.sleep(1 * 1000);
		}
		System.out.println("ExecutorService Download Done");
	}

	private void ffmpeg(File dir, String[] cmds) throws InterruptedException {
		ExecutorService pool = Executors.newWorkStealingPool();
		for (String cmd : cmds) {
			Runnable r = new Runnable() {
				public void run() {
					Process proc = null;
					try {
						System.out.println(System.currentTimeMillis());
						proc = Runtime.getRuntime().exec(cmd, null, dir);
						System.out.println(System.currentTimeMillis());
						//proc.destroy();
						BufferedReader info = new BufferedReader(new InputStreamReader(proc.getInputStream()));
						BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
						String line = null;
						while ( (line = err.readLine()) != null ) {
							System.out.println("EXEC " + line);
						}
						while ( (line = info.readLine()) != null ) {
							System.out.println("INFO " + line);
						}
						//info.close();
						//err.close();
						int ev = proc.waitFor();
						System.out.println("Exit Value: " + ev);
						proc.destroyForcibly();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			pool.execute(r);
		}
		pool.shutdown();
		while (!pool.isTerminated()) {
			Thread.sleep(1 * 1000);
		}
		System.out.println("ExecutorService FFMPEG Done");
	}

}
