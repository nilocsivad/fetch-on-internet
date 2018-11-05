/**
 * 
 */
package com.iam_vip.fetch_on_internet.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.iam_vip.fetch_on_internet.util.RandomString;

/**
 * @author Colin
 */
public class DownloadFile {

	/**
	 * 
	 */
	public DownloadFile() {
	}
	
	
	
	
	public static void download( String link, String to_folder ) {
		download( link, to_folder, null );
	}
	
	public static void download( String link, String to_folder, String file_name ) {
		download( link, to_folder, file_name, "", "" );
	}
	
	public static void download( String link, String to_folder, String file_name, String prefix, String suffix ) {
		
		try {
			
			
				if ( prefix == null || "".equals( prefix.trim() ) ) {
					prefix = "";
				}
			
				if ( suffix == null || "".equals( suffix.trim() ) ) {
					suffix = "";
				}
			
				if ( file_name == null || "".equals( file_name.trim() ) ) {
					file_name = prefix + System.currentTimeMillis() + "-" + RandomString.random();
				}
					
			
				File out_file = new File( to_folder, file_name );
			
				URL url = new URL( link );
			
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)" );
				conn.setRequestProperty( "Auth-" + RandomString.random(), RandomString.random( 24 ) );
				conn.setRequestProperty( "Request-ID", RandomString.random( 32 ) );
				
				
				BufferedInputStream in = new BufferedInputStream( conn.getInputStream() );
				
				BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( out_file ) );
				
				byte[] buf = new byte[ 24 * 1024 * 1024 ];
				
				int len = 0;
				
				while ( ( len = in.read( buf ) ) > 0 ) {
					out.write( buf, 0, len );
				}
				
				in.close();
				out.close();
				
				
				System.out.println( "Download file save at " + out_file.getAbsolutePath() );
				
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
	}
	
	
	

}
