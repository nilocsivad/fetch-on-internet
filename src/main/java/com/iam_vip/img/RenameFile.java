/**
 * 
 */
package com.iam_vip.img;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

/**
 * @author Colin
 *
 */
public class RenameFile {

	/**
	 * 
	 */
	public RenameFile() {
	}
	
	
	@Test
	public void rename1() {

		
		/// 要重命名的父级文件夹 ///
		File dir = new File( "" );
		System.out.println( "Rename the files in \r\n" + dir.getAbsolutePath() );
		
		
		/// 后缀过滤规则及排序规则 ///
		String suffix = ".png";
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File f, String n) {
				return f.isDirectory() && ( suffix != null ? n.endsWith( suffix ) : true );
			}
		};
		Comparator<File> comparator = new Comparator<File>() {
			public int compare(File f1, File f2) {
				String s1 = f1.getName().split( "at" )[1].replace( " ", "" ).replace( ".", "" );
				String s2 = f2.getName().split( "at" )[1].replace( " ", "" ).replace( ".", "" );
				return s1.compareTo( s2 );
			}
		};
		
		
		
		
		
		File[] imgs = dir.listFiles( filter );
		
		if ( imgs.length == 0 ) {
			System.err.println( "The folder contains 0 files." );
			return;
		}
		
		List<File> list = Arrays.asList( imgs );
		Collections.sort( list, comparator);
		
		String dir_n = dir.getName();
		int i = 0;
		
		for ( File f : list ) {
			++i;
			f.renameTo( new File( dir, dir_n + "-" + ( 1000 + i ) + suffix ) );
		}
		
		System.out.println( "Done!" );
		
	}
	

}
