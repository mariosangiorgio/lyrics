package libraryExplorer;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import crawler.Crawler;
import crawler.MetroLyricsCrawler;

public class LibraryExplorer {
	private static Logger logger = Logger.getLogger("LibraryExplorer");
	private String path;
	private Crawler crawler;
	private boolean override = false;

	public LibraryExplorer(String path) {
		this.path = path;
		crawler = new MetroLyricsCrawler();
	}
	
	public LibraryExplorer(String path, String proxyHostname, int proxyPort) {
		this.path = path;
		crawler = new MetroLyricsCrawler(proxyHostname,proxyPort);
	}
	
	public LibraryExplorer(String path, boolean override){
		this(path);
		this.override = override;
	}
	
	public LibraryExplorer(String path, String proxyHostname, int proxyPort, boolean override) {
		this(path,proxyHostname,proxyPort);
		this.override = override;
	}

	public void explore() {
		explore(path);
	}

	public void explore(String path) {
		File current = new File(path);
		if (current.isFile()) {
			if (path.toLowerCase().endsWith(".mp3")) {
				try {
					AudioFile audioFile = AudioFileIO.read(current);
					Tag tag = audioFile.getTag();
					if (tag == null) {
						logger.warning("The file " + current
								+ " doesn't have any tag");
					}
					String lyrics = tag.getFirst(FieldKey.LYRICS);
					if(lyrics.equals("")){
						String artist = tag.getFirst(FieldKey.ARTIST);
						String title = tag.getFirst(FieldKey.TITLE);
						logger.info("Missing lyrics for "+title+" by "+artist+" ("+current+")");
						lyrics = crawler.getLyrics(artist, title);
						tag.setField(FieldKey.LYRICS, lyrics);
						audioFile.commit();
						System.out.println("Lyrics found for "+title+" by "+artist);
					}
					else{
						if(override == true){
							tag.setField(FieldKey.LYRICS,lyrics);
							audioFile.commit();
						}
					}
				} catch (Exception e) {
					logger.severe("Error getting the lyrics for " + current);
				}
			} else {
				logger.warning("Unrecognized file type: " + current);
			}
		} else {
			for (String f : current.list()) {
				if (!f.startsWith(".")) {
					explore(current.getAbsolutePath() + "/" + f);
				}
			}
		}
	}
}
