package com.iam_vip.file;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * 
 */

/**
 * @author Administrator
 */
public class ReadTxtLinesToFile {

	/**
	 * 
	 */
	public ReadTxtLinesToFile() {
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		
		String txt_path = "xxxxxx.txt";
		
		
		File txt_file = new File(txt_path);
		
		//ReadSeveralLines( txt_file, 0, 10000 );

		SplitTo( txt_file, 1000000 );

	}
	
	/**
	 * @param txt_file
	 * @param max_lines must greater than 0
	 */
	public static void SplitTo( File txt_file, int max_lines ) throws Exception {
		
		max_lines = max_lines < 1 ? 1 : max_lines;
		
		BufferedReader reader = new BufferedReader( new FileReader( txt_file ) );

		String line = null;
		FileWriter writer = null;
		int i = 0;
		
		while ( ( line = reader.readLine() ) != null ) {
			
			
			if ( i == 0 ) {
				
				if ( writer != null ) {
					writer.flush();
					writer.close();
				}
				String to = txt_file + "_" + System.currentTimeMillis();
				writer = new FileWriter( to );
				System.out.println( "To: " + to );
				Thread.sleep(1);
			}

			writer.write( line + "\r\n" );
			i++;
			
			if ( i == max_lines ) {
				i = 0;
			}
			
		}

		if ( writer != null ) {
			writer.flush();
			writer.close();
		}
		
		reader.close();
	}

	/**
	 * @param txt_file
	 * @param min greater than 0
	 */
	public static void ReadSeveralLines( File txt_file, int min, int max ) throws Exception {
		
		BufferedReader reader = new BufferedReader( new FileReader( txt_file ) );
		
		FileWriter writer = new FileWriter( txt_file.getAbsolutePath() + "_" + System.currentTimeMillis() );

		String line = null;
		int i = 0;
		
		while ( ( line = reader.readLine() ) != null ) {
			
			i++;
			
			if ( i >= min && i <= max ) {
				writer.write( line + " \r\n");
			}
			
			if ( i >= max ) {
				writer.flush();
				break;
			}
			
		}
		
		reader.close();
		writer.close();
	}

}
