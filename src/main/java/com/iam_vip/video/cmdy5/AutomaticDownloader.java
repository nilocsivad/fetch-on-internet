package com.iam_vip.video.cmdy5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.iam_vip.video.m3u8.FileDownloader;
import com.iam_vip.video.m3u8.ThreadDownloader;

public class AutomaticDownloader {

	public AutomaticDownloader() {
	}

	public static void main(String[] args) throws Exception {
		System.out.println("===================================================");
		
		AutomaticDownloader ader = new AutomaticDownloader();
		
		/// 根据网址把分集的项及m3u8文件写入到文件，控制台打印其所在地址全路径 
		//String url = "https://www.cmdy5.com/ribendongman/mingzhentankenan.html";
		//ader.step_list_children(url);
		
		/// 复制上面方法控制台的地址全路径并执行，得到具体分集的m3u8列表并写入到文件夹中 
		//String path = "";
		//ader.step_read_episodes(path);
		
		/// 复制上面控制台的文件夹地址全路径并执行 
		String dir = args[0];
		ader.downloadEpisodes(dir);
		
		
		//String line = "第1004集###https://www.mzy2000.com/20190908/QLiBMgzQ/index.m3u8";
		//String[] ss = line.split("###");
		//String[] tss = new Calculator(ss[0], ss[1]).gets();
		//for (String ts : tss) {
		//	System.out.println(ts);
		//}
	}
	
	public Document get(String url) {
		return get(url, 3);
	}
	public Document get(String url, int retry) {
		Document doc = null;
		if (retry > 0) {
			try {
				doc = Jsoup.connect(url).get();
			} catch (Exception e) {
				return get(url, retry - 1);
			}
		}
		return doc;
	}

	public void step_list_children(String url) {
		Element e = get(url).getElementById("vlink_1").getElementsByTag("a").get(0);
		System.out.println(e.outerHtml());
		step_fetch_children(e.attr("href"));
	}
	
	public void step_fetch_children(String url) {
		Element ele = get(url).getElementsByTag("script").stream().filter(e -> e.hasAttr("src") && e.attr("src").contains("/playdata/")).findFirst().get();
		step_download_playdata(ele.absUrl("src"));
	}
	
	public void step_download_playdata(String url) {
		FileDownloader getter = new FileDownloader(url, System.getProperty("java.io.tmpdir"));
		if (getter.download() == 1) {
			File js = getter.getFile();
			createDatFile(js);
		}
	}
	
	private Object executeJavascript(String js_cmd) {
		try {
			ScriptEngineManager m = new ScriptEngineManager();
			ScriptEngine engine = m.getEngineByName("js");
			return engine.eval(js_cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<String> dealWithJs(String txt) {
		int index1 = txt.indexOf("unescape(");
		int index2 = txt.indexOf(")", index1 + 9);
		String js_cmd = txt.substring(index1, index2 + 1);
		Object obj = executeJavascript(js_cmd);
		if (obj == null) {
			System.err.println("Null value returned from eval");
		} else {
			if (obj instanceof String) {
				return Arrays.asList(obj.toString().split("#")).stream().collect(Collectors.toList());
			} else {
				System.out.println(obj.getClass().getTypeName());
			}
		}
		return null;
	}
	
	public void createDatFile(File js) {
		/// 读取包含分集与其链接地址的加密数据js文件
		StringBuilder buf = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(js))) {
			String line = null;
			while ( (line = reader.readLine()) != null ) {
				buf.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		/// 通过java调用js引擎并执行返回结果
		List<String> episodes = dealWithJs(buf.toString());
		/// 把多个分集与其对应的m3u8链接写入文件
		File episode_f = new File(js.getParent(), js.getName() + ".dat");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(episode_f))) {
			for (String episode : episodes) {
				writer.write(episode.replace("$", "###") + "\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("===================================================");
		System.out.println("=== " + episode_f.getAbsolutePath());
		System.out.println("===================================================");
	}
	
	public void readFile(String path) throws Exception {
		File f = new File(path);
		if (!f.exists()) {
			return;
		}
		File dir = new File(path + ".dir");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		ExecutorService pool = Executors.newWorkStealingPool();
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			String line = null;
			while ( (line = reader.readLine()) != null ) {
				String f_line = line;
				pool.execute(() -> {
					String[] ss = f_line.split("###");
					String[] tss = new Calculator(ss[0], ss[1]).gets();
					File m3u8 = new File(dir, ss[0] + ".m3u8");
					try (BufferedWriter writer = new BufferedWriter(new FileWriter(m3u8))) {
						writer.write("#####" + ss[0] + "#####" + "\r\n");
						for (String ts : tss) {
							writer.write(ts + "\r\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(m3u8.getAbsoluteFile());
				});
			}
		}
		pool.shutdown();
		while (!pool.isTerminated()) {
			Thread.sleep(2 * 1000);
		}
		System.out.println("===================================================");
		System.out.println("=== " + dir.getAbsolutePath());
		System.out.println("===================================================");
	}
	
	public void downloadEpisodes(String dir) {
		ExecutorService pool = Executors.newFixedThreadPool(5);
		File[] files = new File(dir).listFiles((e, n) -> n.endsWith("m3u8"));
		for (File f : files) {
			pool.execute(() -> {
				downloadEpisode(f);
			});
		}
		pool.shutdown();
		try {
			while (!pool.isTerminated()) {
				Thread.sleep(1 * 1000);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void downloadEpisode(File m3u8_f) {
		/// 已经下载的任务忽略
		if (new File(m3u8_f.getAbsolutePath() + ".mp4").exists()) {
			return;
		}
		/// 创建新子级目录
		File dir = new File(m3u8_f.getAbsolutePath() + ".dir");
		dir.mkdirs();
		/// 下载处理工具类
		Calculator c = new Calculator(m3u8_f.getName(), m3u8_f);
		String[] links = c.gets();
		String cmd = c.cmd();
		try {
			/// 下载包含的文件片段 
			download(dir.getAbsolutePath(), links);
			/// 合并至同一文件
			ffmpeg(dir, cmd);
			/// 收尾工作，删除下载的文件
			delete(dir);
		} catch (InterruptedException ee) {
			ee.printStackTrace();
		}
	}
	
	private void delete(File dir) {
		File[] fs = dir.listFiles();
		for (File f : fs) {
			f.delete();
		}
		dir.delete();
	}

	private void download(String path, String[] links) throws InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(5);
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

	private void ffmpeg(File dir, String... cmds) throws InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(5);
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
