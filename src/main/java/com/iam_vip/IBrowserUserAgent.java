/**
 * 
 */
package com.iam_vip;

import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author Colin
 */
public interface IBrowserUserAgent {
	
	String START = "------------------------------------";

	String CHROME_WIN1 = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36";

	String CHROME_WIN2 = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	
	String CHROME_WIN_64_1 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";

	String FIREFOX_WIN1 = "Mozilla/5.0 (Windows NT 6.1; rv:41.0) Gecko/20100101 Firefox/41.0";

	String FIREFOX_WIN2 = "Mozilla/5.0 (Windows NT 6.1; rv:42.0) Gecko/20100101 Firefox/42.0";

	String FIREFOX_WIN3 = "Mozilla/5.0 (Windows NT 6.1; rv:43.0) Gecko/20100101 Firefox/43.0";

	String FIREFOX_WIN4 = "Mozilla/5.0 (Windows NT 6.1; rv:48.0) Gecko/20100101 Firefox/48.0";
	
	String FIREFOX_WIN_64_1 = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0";

	String[] ArrUserAgent = { CHROME_WIN1, CHROME_WIN2, CHROME_WIN_64_1, FIREFOX_WIN1, FIREFOX_WIN2, FIREFOX_WIN3, FIREFOX_WIN4, FIREFOX_WIN_64_1 };
	
	default Document getDoc(String url) throws IOException {
		int idx = new Random().nextInt(ArrUserAgent.length);
		String agent = ArrUserAgent[idx];
		return Jsoup.connect(url).header("Referer", url).header("User-Agent", agent).timeout(1000 * 60 * 3).get();
	}

}
