/**
 * 
 */
package com.iam_vip.fetch_on_internet.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author Colin
 */
public class HttpRequest {

	/**
	 * 
	 */
	public HttpRequest() {
	}
	
	
	public static String doRequest( String link, Map<String, ? extends Object> parameterMap ) {
		return doRequest( link, parameterMap, "UTF-8" );
	}
	
	public static String doRequest( String link, Map<String, ? extends Object> parameterMap, String charset ) {
		
		try {
			
				HttpClient httpClient = HttpClientBuilder.create().build();
				
				HttpPost httpPost = new HttpPost( link );
				
				if ( parameterMap.size() > 0 ) {
				
						List<NameValuePair> list = new ArrayList<>( parameterMap.size() );
						
						for ( Map.Entry<String, ? extends Object> itm0 : parameterMap.entrySet() ) {
								list.add( new BasicNameValuePair( itm0.getKey(), itm0.getValue() + "" ) );
						}
						
						httpPost.setEntity( new UrlEncodedFormEntity( list, charset ) );
				
				}
				
				HttpResponse response = httpClient.execute( httpPost );
				
				if ( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
						
						return EntityUtils.toString( response.getEntity(), charset );
					
				} else {	
						return null;
				}
				
				
				
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	

}
