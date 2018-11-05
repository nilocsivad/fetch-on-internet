/**
 * 
 */
package com.iam_vip.fetch_on_internet;

import java.io.InputStream;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.iam_vip.fetch_on_internet.file.DownloadFile;
import com.iam_vip.fetch_on_internet.http.HttpRequest;
import com.iam_vip.fetch_on_internet.util.RandomString;

/**
 * @author Colin
 */
public class Fetch_book118_com {

	/**
	 * 
	 */
	public Fetch_book118_com() {
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		String book118_url = "https://max.book118.com/html/2017/1027/138023162.shtm";
		
		String save_folder =  "/Users/Colin/Documents/aaaaaa";
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		String[] arr1 = book118_url.split( "/" );
		String number = arr1[ arr1.length - 1 ].split( "\\." )[0];
		
		String next_url = "https://max.book118.com/index.php?g=Home&m=View&a=viewUrl&flag=1&cid=" + number;
		System.out.println( "" + next_url );
		
        HttpUriRequest request = new HttpGet( next_url );
		
        	CloseableHttpResponse response = doRequest(request);
		
		next_url = "https:" + responseText( response );
		System.out.println( next_url );
		
		request = new HttpPost( next_url );
		
		response = doRequest(request);
		
		String prefix = "https://" + next_url.replace( "https://", "" ).split( "/" )[0];
		
		String pdf = response.getHeaders( "Location" )[0].getValue();
		
		request = new HttpPost( prefix + pdf );
		URI URI = request.getURI();
		
		String[] arr = URI.getPath().split( "/" );
		String f__ = arr[ arr.length - 1 ];
		
		next_url = prefix + pdf;
		System.out.println( next_url );
		
		
		
		Document htmlDoc = Jsoup.connect( next_url ).get();
		String img0 = htmlDoc.getElementById( "Img" ).val();
		String furl = htmlDoc.getElementById( "Furl" ).val();
		String limit = htmlDoc.getElementById( "ReadLimit" ).val();
		
		
		log( img0, save_folder, prefix, f__, limit, furl );
		
		next( img0, save_folder, prefix, f__, limit, furl );
		
	}
	
	public static void log( Object... objs ) {
		for ( Object o : objs ) {
			System.out.println( o.toString() );
		}
	}
	
	public static Map<String, String> queryToMap( String query ) {
		Map<String, String> map = new HashMap<>();
		String[] arr = query.split( "&" );
		for ( String s0 : arr ) {
			String[] strings = s0.split( "=" );
			map.put( strings[0], strings[1] );
		}
		return map;
	}
	
	public static String responseText( CloseableHttpResponse response ) throws Exception {
		byte[] buf = new byte[ 1024 * 1024 * 128 ];
		InputStream is = response.getEntity().getContent();
		int len = is.read( buf );
		is.close();
		return new String( buf, 0, len );
	}
	
	public static CloseableHttpResponse doRequest( HttpUriRequest request ) throws Exception {
        
    	String protocol = request.getURI().toURL().getProtocol().toLowerCase();
		
		RequestConfig config = RequestConfig.custom().build();
		
		SSLContext sslContext = SSLContext.getInstance( "SSLv3" );
		X509TrustManager trustManager = new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		};
		sslContext.init( null, new TrustManager[] { trustManager }, null );
		
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http",
                PlainConnectionSocketFactory.INSTANCE).register("https",
                new SSLConnectionSocketFactory(sslContext)).build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		HttpClients.custom().setConnectionManager(connManager);
		
		HttpClientBuilder HttpClientbuilder = HttpClients.custom().setConnectionManager(connManager);
		HttpClientbuilder.setDefaultRequestConfig(config);
		CloseableHttpClient client = HttpClientbuilder.build();
		
		if ( protocol.startsWith( "https" ) == true ) {
		
			TrustManager[] trustManagers = new TrustManager[1];
			TrustManager m0 = new HttpsTrustManager();
			trustManagers[0] = m0;
			SSLContext sc = SSLContext.getInstance( "SSL" );
			sc.init( null, trustManagers, null );
			HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );
			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		}
		
		CloseableHttpResponse response = client.execute( request );
		return response;
		
	}
	
	static class HttpsTrustManager implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }
        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }
    }
			
	public static void next( String first_img, String save_folder, String url_base, String f, String limit, String furl ) throws Exception {
		
			String save_format = "Page-%d.jpg";
			
			String img = first_img;
			String img_base = url_base + "/img/?img=";
			String next_base = url_base + "/pdf/GetNextPage/?f=" + f + "&isMobile=false&isNet=True&readLimit=" + limit + "&furl=" + furl + "&img=";
			
			int fff = 100001;
			
			Gson gson = new Gson();
			
			do {
				
					Map<String, String> parameterMap = new HashMap<>();
					parameterMap.put( "from", img );
					parameterMap.put( "rdm", RandomString.random( 16 ) );
				
					String rrr_txt = HttpRequest.doRequest( next_base + img, parameterMap );
					
					if ( rrr_txt.startsWith( "{" ) == true && rrr_txt.endsWith( "}" ) == true ) {
							@SuppressWarnings("unchecked")
							Map<String, Object> rrr_map = gson.fromJson( rrr_txt, Map.class );
							
							img = rrr_map.get( "NextPage" ) + "";
							
							if ( img == null || "".equals( img.trim() ) == true ) {
									break;
							}
					} else {
							System.out.println( rrr_txt );
							break;
					}
				
					String to_name = String.format( save_format, fff );
					
					String suffix = "&_t=" + System.currentTimeMillis() + "_t" + RandomString.random() + "=" + RandomString.random(24);
				
					DownloadFile.download( img_base + img + suffix, save_folder, to_name );
					
					Thread.sleep( 100 );
					
					fff++;
				
			} while ( img != null && "".equals( img.trim() ) == false );
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
