/**
 * 
 */
package com.iam_vip.english;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.iam_vip.IBrowserUserAgent;
import com.iam_vip.fetch_on_internet.file.DownloadFile;
import com.iam_vip.fetch_on_internet.util.RandomString;

/**
 * www.hxen.com 恒星英语 语音文字下载
 * @author Colin
 */
public class HXEN implements IBrowserUserAgent {

	/**
	 * 
	 */
	public HXEN() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new HXEN().run();
	}
	
	public void run() throws IOException, Exception {
		
		/// 第一个页面的URL ///
		String sFirstUrl = "";
		int key = 1;
		String sDir = "/tmp";
		
		final Pattern p = Pattern.compile("[a-zA-Z']");
		boolean hasNext = false;
		do {
			Document doc = getDoc(sFirstUrl);
			String title = doc.getElementById("slistl").getElementsByClass("arcinfo").get(0).getElementsByTag("h1").get(0).text();
			System.out.println(title);
			System.out.println("==================================");
			Element arctext = doc.getElementById("arctext");
			
			if (key % 10 == 1) {
				String sText = arctext.getElementsByTag("p").get(0).html()
									  .replace("<br/>", "\r\n").replace("<br>", "\r\n").replace("<br />", "\r\n")
									  .replace("&nbsp;", "").replace("<strong>", "").replace("</strong>", "")
									  .replace("  ", " ").trim();
				System.out.println(sText);
			}
			if (key % 10 == 2) {
				arctext.childNodes().forEach(n -> {
					String sHtml = n.outerHtml().replace("&nbsp;"	, "");
					if (sHtml.length() > 1 && (sHtml.startsWith("<p>") == true || sHtml.startsWith("<") == false) ) {
						Matcher m = p.matcher(sHtml);
						if (m.find()) {
							int iii = m.start();
							System.out.print(sHtml.substring(iii) + " ");
						}
					}
				});
			}
			System.out.println("\r\n\r\n");
			Element f12 = arctext.getElementsByClass("f12").get(0);
			f12.getElementById("recomand").remove();
			Element nextA = f12.getElementsByTag("a").last();
			sFirstUrl = nextA.absUrl("href");
			hasNext = nextA.text().contains("返回列表") == false;
			
			if (key > 10) {
				String sMp3 = arctext.getElementsByTag("embed").attr("src");
				DownloadFile.download(sMp3, sDir, title + "-" + RandomString.randomNum() + ".mp3");
			}
			
			Thread.sleep(100);
		} while(hasNext);

	}

}
