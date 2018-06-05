/**
 * 
 */
package com.iam_vip.fetch_on_internet;

import java.util.HashMap;
import java.util.Map;

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

			String first_img = "Hs92T42xAvvgQ@Uxim97DcBT7QxP3QRGBuTo@OjxV4xbgzVrfDmiJb6Mmsyr1hG89_QRr4zfOlE=";
			String save_format = "class1-6-%d.jpg";
			String save_folder = "Y:\\University-Class1-to-6-Words\\";
			
			String img = first_img;
			String url_base = "https://view3652.book118.com";
			String img_base = url_base + "/img/?img=";
			String next_base = url_base + "/pdf/GetNextPage/?f=dXAyNS5ib29rMTE4LmNvbS44MFwzNzgwMTkzLTU5MjE0MjA5OWM3YjUucGRm&isMobile=false&isNet=True&readLimit=a6Q5wtQfHGxN1SV16Ztq2w%3D%3D&furl=YOQStEpojXDkR5q1h7jAA6aP2h6TJ9QI%409WXyuBb6wi1A9xo624sBQACeuW4GxrVSyHObTcDLiRwhjcon3_Jd_CZKhXtIbCj7%40eamZalTao%3D&img=";
			
			int fff = 100000;
			
			Gson gson = new Gson();
			
			do {
				
					String to_name = String.format( save_format, fff );
					
					String suffix = "&_t=" + System.currentTimeMillis() + "_t" + RandomString.random() + "=" + RandomString.random(24);
				
					DownloadFile.download( img_base + img + suffix, save_folder, to_name );
					
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
					
					Thread.sleep( 2 );
				
			} while ( img != null && "".equals( img.trim() ) == false );
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
