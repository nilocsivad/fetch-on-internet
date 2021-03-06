/**
 * 
 */
package com.iam_vip.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.junit.Test;

import com.iam_vip.util.DTUtil;

/**
 * @author Colin
 */
public class MergeImage {

	/**
	 * 
	 */
	public MergeImage() {
	}
	
	
	
	@Test
	public void TestImageMerge() throws Exception {
		
		
	}
	
	
	
	/**
	 * /// 纵向合并PPT截图文件 ///
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void MergeByV( String directory ) throws IOException, InterruptedException {
		this.MergeByV(directory, null);
	}
	
	/**
	 * /// 自动按时间顺序排序,之后按给出的最大高度合并 ///
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void MergeByV( String directory, FilenameFilter filter ) throws IOException, InterruptedException {
		
		File src_f = new File( directory );
		System.out.println( "Merge the pictures from " + src_f.getAbsolutePath() );
		
		/// 扫描图片文件 ///
		File[] fs = filter == null ? src_f.listFiles() : src_f.listFiles( filter );
		int len = fs.length;
		
		String formatName = suffix( fs[0] );
		
		/// 初始化图片集合 ///
		BufferedImage[] images = null; 
		int max_width = 0;   /// 所有图片的最宽的图片像素 ///
		int max_height = 0;  /// 所有图片的最高的图片像素 ///
		int full_height = 0; /// 所有图片的高度总合 ///
		
		{ /// 转换文件为图片对象 ///
			List<File> list = Arrays.asList( fs );
			Collections.sort( list, new Comparator<File>() { /// 按照时间进行排序 ///
				public int compare(File o1, File o2) { return (int) (o1.lastModified() - o2.lastModified()); }
			});
			/// 加载文件并转换为图片对象 ///
			List<BufferedImage> temporary = new ArrayList<>( len + 1 );
			for ( File itm : list ) {
				BufferedImage img0 = ImageIO.read( itm );
				if ( img0 == null ) continue;
				temporary.add( img0 );
				int cur_w = img0.getWidth();
				int cur_h = img0.getHeight();
				full_height += cur_h;
				max_width = cur_w > max_width ? cur_w : max_width;
				max_height = cur_h > max_height ? cur_h : max_height;
			}
			images = temporary.toArray( new BufferedImage[0] );
		}
		
		String content = JOptionPane.showInputDialog( "The sum of height is  " 
				+ full_height + ". So how tall is per picture? (Input a number, please!)", 0 );
		int height = Integer.parseInt( content );
		
		/// 要合并的图片临时集合 ///
		List<BufferedImage> temporary = new ArrayList<>( 20 );
		
		int tmp_h = 0; /// 计算图片高度临时变量 ///
		
		StringBuffer buf = new StringBuffer();
		
		for ( BufferedImage img : images ) {
			
			tmp_h += img.getHeight();
			temporary.add( img );
			
			if ( tmp_h > 0 && tmp_h > height ) { /// 达到某个高度后开始合并操作 ///
				
				File to = new File( src_f, src_f.getName() + "-merge-" + DTUtil.formatA(2) + "." + formatName );
				System.out.println( to.getAbsolutePath() );
				
				buf.append( to.getAbsolutePath() + "\r\n" );
				
				BufferedImage save_img = mergeVertical( 40, 40, temporary.toArray( new BufferedImage[0] ) );
				ImageIO.write( save_img, formatName, to );
				
				temporary.clear();
				tmp_h = 0;
				
				Thread.sleep(1000);
			}
		}
		
		/// 若存在最后未达到高度的图片,则仍要合并 ///
		if ( temporary.isEmpty() == false && temporary.size() > 0 ) {
			
			File to = new File( src_f, src_f.getName() + "-merge-" + DTUtil.formatA(2) + "." + formatName );
			System.out.println( to.getAbsolutePath() );
			
			buf.append( to.getAbsolutePath() + "\r\n" );
			
			BufferedImage save_img = mergeVertical( 40, 40, temporary.toArray( new BufferedImage[0] ) );
			ImageIO.write( save_img, formatName, to );
		}
		
		JOptionPane.showMessageDialog( null, buf.toString() );
		
	}
	
	
	/**
	 * /// 图片纵向切割, 分为左半与右半, 再上下顺序合并 ///
	 * @param img_w 图片宽度
	 * @param add_w 图片左右留白宽度
	 * @param img_h 图片高度
	 * @param add_h 图片上下留白高度
	 * @param images 要合并的图片数组
	 * @return 合成后的图片
	 */
	public BufferedImage mergeDoubleVertical( int img_w, int add_w, int img_h, int add_h, BufferedImage... images ) {
		
			img_h += add_h;
			img_w += add_w;
		
			int final_w = img_w / 2, final_h = images.length * img_h * 2;
			
			BufferedImage save_img = new BufferedImage( final_w, final_h, BufferedImage.TYPE_INT_RGB );
			
			int[] white = new int[ img_h * img_w ];
			for ( int i = 0; i < white.length; ++i ) {
					white[i] = 0xffffff;
			}
			
			for ( int i = 0; i < images.length; ++i ) {
				
					save_img.setRGB( 0, i * 2 * img_h, final_w, img_h * 2, white, 0, final_w );
				
					BufferedImage img0 = images[i];
					
					if ( img0 == null ) 
						continue;
					
					int img0_w = img0.getWidth();
					int img0_h = img0.getHeight();
					
					int[] buf = new int[ img0_w * img0_h ];
					
					buf = img0.getRGB( 0, 0, img0_w / 2, img0_h, buf, 0, img0_w / 2 );
					
					save_img.setRGB( 0, i * 2 * img_h, img0_w / 2, img0_h, buf, 0, img0_w / 2 );
					
					buf = new int[ img0_w * img0_h ];
					
					buf = img0.getRGB( img0_w / 2, 0, img0_w / 2, img0_h, buf, 0, img0_w / 2 );
					
					save_img.setRGB( add_w / 2, i * 2 * img_h + img_h, img0_w / 2, img0_h, buf, 0, img0_w / 2 );
				
			}
			
			return save_img;
		
	}
	
	/**
	 * /// 图片纵向上下顺序合并 ///
	 * @param add_w 每张图片左右留白宽度
	 * @param add_h 每张图片上下留白高度
	 * @param images 要合并的图片数组
	 * @return 合成后的图片
	 */
	public BufferedImage mergeVertical( int add_w, int add_h, BufferedImage... images ) {
		
			/// 单个图片的最大宽度 ///
			int max_w = 0;
		
			/// 最终合成的图片宽度与高度 ///
			int final_h = 0;
			
			for ( BufferedImage img0 : images ) {
				final_h += img0.getHeight() + add_h;
				max_w = ( img0.getWidth() > max_w ) ? img0.getWidth() : max_w;
			}
			
			/// 合成图片后的最终宽度 ///
			int final_w = max_w + add_w;
			
			/// 最终合成的图片对象，并初始化为白色背景 ///
			BufferedImage final_img = new BufferedImage( final_w, final_h, BufferedImage.TYPE_INT_RGB );
			
			int[] white = new int[ final_w * final_h ];
			for ( int i = 0; i < white.length; ++i ) {
					white[i] = 0xffffff;
			}
			final_img.setRGB( 0, 0, final_w, final_h, white, 0, final_w );
			
			/// 当前写图片到了横纵坐标的多少像素 ///
			int calc_h = 0;
			
			int half_add_h = add_h / 2;
			
			for ( BufferedImage img0 : images ) {
					
					int img0_w = img0.getWidth();
					int img0_h = img0.getHeight();
					
					int[] buf = new int[ img0_w * img0_h ];
					buf = img0.getRGB( 0, 0, img0_w, img0_h, buf, 0, img0_w );
					
					int startX = 0;
					if ( max_w > img0_w ) {
						startX = ( max_w - img0_w + add_w ) / 2;
					}
					
					final_img.setRGB( startX, calc_h + half_add_h, img0_w, img0_h, buf, 0, img0_w );
				
					calc_h += img0_h + add_h;
			}
			
			return final_img;
		
	}
	
	/**
	 * /// 图片横向左右顺序合并 ///
	 * @param img_w 图片宽度
	 * @param add_w 图片左右留白宽度
	 * @param img_h 图片高度
	 * @param add_h 图片上下留白高度
	 * @param images 要合并的图片数组
	 * @return 合成后的图片
	 */
	public BufferedImage mergeHorizontal( int img_max_width, int img_max_height, BufferedImage... images ) {
		
			int final_w = images.length * img_max_width, final_h = img_max_height;
			
			BufferedImage save_img = new BufferedImage( final_w, final_h, BufferedImage.TYPE_INT_RGB );
			
			int[] white = new int[ img_max_height * img_max_width ];
			for ( int i = 0; i < white.length; ++i ) {
					white[i] = 0xffffff;
			}
			
			for ( int i = 0; i < images.length; ++i ) {
				
					save_img.setRGB( i * img_max_width, 0, img_max_width, img_max_height, white, 0, img_max_width );
				
					BufferedImage img0 = images[i];
					
					int img0_w = img0.getWidth();
					int img0_h = img0.getHeight();
					
					int[] buf = new int[ img0_w * img0_h ];
					
					buf = img0.getRGB( 0, 0, img0_w, img0_h, buf, 0, img0_w );
					
					save_img.setRGB( i * img_max_width, 0, img0_w, img0_h, buf, 0, img0_w );
				
			}
			
			return save_img;
		
	}
	
	
	public static final String suffix( File f ) {
		return suffix( f.getName() );
	}
	
	public static final String suffix( String n ) {
		int i = n.lastIndexOf( "." );
		return n.substring( i + 1 );
	}
	
	

}
