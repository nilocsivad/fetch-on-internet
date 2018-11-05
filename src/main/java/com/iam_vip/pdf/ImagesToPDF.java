/**
 * 
 */
package com.iam_vip.pdf;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * @author Colin
 *
 */
public class ImagesToPDF {

	/**
	 * 
	 */
	public ImagesToPDF() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		ImagesToPDF itp = new ImagesToPDF();
		itp.scanToPDF();

	}
	
	/**
	 * /// 把多个图片保存为连续的HTML网页, 之后通过浏览器保存为PDF ///
	 * @throws Exception
	 */
	public void scanToPDF() throws Exception {

		File foldr = new File( "" );
		String[] names = foldr.list( new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith( "" );
			}
		});
		List<String> list = Arrays.asList( names );
		Collections.sort(list, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo( o2 );
			}
		});
		
		
		ClassLoader cl = this.getClass().getClassLoader();
		URL url = cl.getResource( "." );
		File f = new File(url.getPath());

		Properties properties = new Properties();
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, f.getAbsolutePath() );
		Velocity.init(properties);
		
		VelocityContext context = new VelocityContext();
		context.put( "collects", list );
		
		String templatePath = "template/img_to_pdf.html.vm";

		if (Velocity.resourceExists(templatePath)) {

			Template template = Velocity.getTemplate(templatePath, "UTF-8");

			File toFile = new File( foldr, "___" + foldr.getName() + ".html" );
			FileWriter writer = new FileWriter(toFile);

			template.merge(context, writer);

			writer.close();
		}
		else {
			System.err.format("Can't find resource '%s'. \r\n", templatePath);
		}
		
	}

}
