/**
 * 
 */
package com.iam_vip.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.Test;

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
			
		//MergeEnglish1();
			
		MergePPT1();	
		
	}
	
	
	
	/**
	 * @throws IOException
	 */
	public void MergePPT1() throws IOException {
		
		File src_f = new File( "/", "‎⁨Volumes/ddd/University-English-Others/通用英语1-姜芸/通用1-U4-PPT-截图/⁩" );
		System.out.println( src_f.getAbsolutePath() );
		
		File[] imgs = src_f.listFiles();
		
		for ( File f : imgs ) {
			System.out.println( f.getAbsolutePath() );
		}
		
	}



	/**
	 * @throws IOException
	 */
	public void MergeEnglish1() throws IOException {
		
		String image_folder = "Y:\\University-Class1-to-6-Words";
		String to_folder = "Y:\\University-Class1-to-6-Words-2";
		String[] suffix = { ".jpg", ".png", ".bmp" };
		
		File[] folders = new File( image_folder ).listFiles();
		
		for ( int i = 0; i < folders.length; ++i ) {
				
				File cur_folder = folders[i];
				
				File[] files = cur_folder.listFiles( ( f ) -> {
						boolean r = false;
						for ( String s0 : suffix ) {
								if ( f.getName().endsWith( s0 ) == true ) {
									 r = true;
									 break;
								}
						}
						return r;
				});
				
				if ( files == null )
					continue;
				
				int files_len = files.length;
				
				BufferedImage[] images = new BufferedImage[ files_len ];
				int max_width = 0;
				int max_height = 0;
			
				for ( int j = 0; j < files_len; ++j ) {
					
						BufferedImage cur_img = ImageIO.read( files[j] );
						images[j] = cur_img;
						
						int cur_w = cur_img.getWidth();
						int cur_h = cur_img.getHeight();
						
						max_width = cur_w > max_width ? cur_w : max_width;
						max_height = cur_h > max_height ? cur_h : max_height;
				}
				
				
				int include = 5;
				int group_len = files_len % include == 0 ? files_len / include : files_len / include + 1;
				
				for ( int k = 0; k < group_len; ++k ) {
						BufferedImage[] buf = Arrays.copyOfRange( images, k * include, k * include + include );
						///BufferedImage save_img = mergeHorizontal( max_width, max_height, images );
						///BufferedImage save_img = mergeVertical( max_width, max_height, images );
						BufferedImage save_img = mergeDoubleVertical( max_width, 160, max_height, 0, buf );
						ImageIO.write( save_img, "JPG", new File( to_folder, "class1-6-" + cur_folder.getName() + "-" + ( k + 1 ) + "-2height.jpg" ) );
				}
				
		}
		
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
	 * @param img_w 图片宽度
	 * @param add_w 图片左右留白宽度
	 * @param img_h 图片高度
	 * @param add_h 图片上下留白高度
	 * @param images 要合并的图片数组
	 * @return 合成后的图片
	 */
	public BufferedImage mergeVertical( int img_max_width, int img_max_height, BufferedImage... images ) {
		
			int final_w = img_max_width, final_h = images.length * img_max_height;
			
			BufferedImage save_img = new BufferedImage( final_w, final_h, BufferedImage.TYPE_INT_RGB );
			
			int[] white = new int[ img_max_height * img_max_width ];
			for ( int i = 0; i < white.length; ++i ) {
					white[i] = 0xffffff;
			}
			
			for ( int i = 0; i < images.length; ++i ) {
				
					save_img.setRGB( 0, i * img_max_height, img_max_width, img_max_height, white, 0, img_max_width );
				
					BufferedImage img0 = images[i];
					
					int img0_w = img0.getWidth();
					int img0_h = img0.getHeight();
					
					int[] buf = new int[ img0_w * img0_h ];
					
					buf = img0.getRGB( 0, 0, img0_w, img0_h, buf, 0, img0_w );
					
					save_img.setRGB( 0, i * img_max_height, img0_w, img0_h, buf, 0, img0_w );
				
			}
			
			return save_img;
		
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
	
	
	
	
	

}
