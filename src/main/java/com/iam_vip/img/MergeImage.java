/**
 * 
 */
package com.iam_vip.img;

import java.awt.image.BufferedImage;
import java.io.File;
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
							//BufferedImage save_img = mergeVertical( max_width, max_height, images );
							BufferedImage save_img = mergeDoubleVertical( max_width, 160, max_height, 0, buf );
							ImageIO.write( save_img, "JPG", new File( to_folder, "class1-6-" + cur_folder.getName() + "-" + ( k + 1 ) + "-2height.jpg" ) );
					}
					
			}
			
			
			
		
	}
	
	
	public BufferedImage mergeDoubleVertical( int img_max_width, int add_w, int img_max_height, int add_h, BufferedImage... images ) {
		

			///{
			///		BufferedImage img0 = images[3];
			///		
			///		int img0_w = img0.getWidth();
			///		int img0_h = img0.getHeight();
			///		
			///		int[] white = new int[ img0_w * img0_h / 2 ];
			///		for ( int i = 0; i < white.length; ++i ) {
			///				white[i] = 0xffffff;
			///		}
			///		
			///		BufferedImage save_img = new BufferedImage( img0_w / 2, img0_h, BufferedImage.TYPE_INT_RGB );
			///		save_img.setRGB( 0, 0, img0_w / 2, img0_h, white, 0, img0_w / 2 );
			///		
			///		int[] buf = new int[ img0_w * img0_h / 2 ];
			///		buf = img0.getRGB( 0, 0, img0_w / 2, img0_h, buf, 0, img0_w / 2 );
			///		
			///		save_img.setRGB( 0, 0, img0_w / 2, img0_h, buf, 0, img0_w / 2 );
			///		
			///		ImageIO.write( save_img, "JPG", new File( "Y:\\" + System.currentTimeMillis() + "-1in2.jpg" ) );
			///}
		
		
			img_max_height += add_h;
			img_max_width += add_w;
		
			int final_w = img_max_width / 2, final_h = images.length * img_max_height * 2;
			
			BufferedImage save_img = new BufferedImage( final_w, final_h, BufferedImage.TYPE_INT_RGB );
			
			int[] white = new int[ img_max_height * img_max_width ];
			for ( int i = 0; i < white.length; ++i ) {
					white[i] = 0xffffff;
			}
			
			for ( int i = 0; i < images.length; ++i ) {
				
					save_img.setRGB( 0, i * 2 * img_max_height, final_w, img_max_height * 2, white, 0, final_w );
				
					BufferedImage img0 = images[i];
					
					if ( img0 == null ) 
						continue;
					
					int img0_w = img0.getWidth();
					int img0_h = img0.getHeight();
					
					int[] buf = new int[ img0_w * img0_h ];
					
					buf = img0.getRGB( 0, 0, img0_w / 2, img0_h, buf, 0, img0_w / 2 );
					
					save_img.setRGB( 0, i * 2 * img_max_height, img0_w / 2, img0_h, buf, 0, img0_w / 2 );
					
					buf = new int[ img0_w * img0_h ];
					
					buf = img0.getRGB( img0_w / 2, 0, img0_w / 2, img0_h, buf, 0, img0_w / 2 );
					
					save_img.setRGB( add_w / 2, i * 2 * img_max_height + img_max_height, img0_w / 2, img0_h, buf, 0, img0_w / 2 );
				
			}
			
			return save_img;
		
	}
	
	
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
