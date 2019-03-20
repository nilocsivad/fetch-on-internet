/**
 * 
 */
package com.iam_vip.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;

import com.google.gson.Gson;

/**
 * @author Colin
 *
 */
public class MP3Tags {

	/**
	 * 
	 */
	public MP3Tags() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new MP3Tags().cleanTags();
	}
	
	
	public void cleanTags() throws Exception {
		File dir = new File("");
		File[] fs = dir.listFiles((dir0, name0) -> {
			return name0.endsWith(".mp3");
		});
		for (File f : fs) {
			System.out.println(f.getName());
			MP3File mp3 = (MP3File) AudioFileIO.read(f);
			if (mp3.hasID3v1Tag() == true) {
				Tag tagIDv1 = mp3.getID3v1Tag(); /// track album artist comment title year genre loggingFilename ///
					tagIDv1.deleteField(FieldKey.TRACK);
					tagIDv1.deleteField(FieldKey.ALBUM);
					tagIDv1.deleteField(FieldKey.ARTIST);
					tagIDv1.deleteField(FieldKey.COMMENT);
					tagIDv1.deleteField(FieldKey.TITLE);
					tagIDv1.deleteField(FieldKey.YEAR);
					tagIDv1.deleteField(FieldKey.GENRE);
				mp3.commit();
				System.out.println("ID3v1:" + new Gson().toJson(tagIDv1));
			}
			if (mp3.hasID3v2Tag() == true) {
				Tag tagIDv2 = mp3.getID3v2Tag();
				List<String> fields = new ArrayList<>();
				Iterator<TagField> it = tagIDv2.getFields();
		        while (it.hasNext()) {
		        	TagField tf = it.next();
		        	fields.add(tf.getId());
		        }
		        for (String field : fields) {
		        		tagIDv2.deleteField(field);
		        }
				mp3.commit();
				System.out.println("ID3v2:" + new Gson().toJson(tagIDv2));
			}
		}
		System.out.println("====================END");
	}

}
