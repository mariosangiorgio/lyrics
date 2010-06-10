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

	public LibraryExplorer(String path) {
		this.path = path;
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
						Crawler crawler = new MetroLyricsCrawler();
						lyrics = crawler.getLyrics(artist, title);
						tag.setField(FieldKey.LYRICS, lyrics);
						audioFile.commit();
						logger.info("Lyrics found for "+title+" by "+artist);
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

	public static void main(String[] args) {
		String[] loggers = { "org.jaudiotagger.audio",
				"org.jaudiotagger.tag.id3", "org.jaudiotagger.tag.datatype" };
		for (String loggerName : loggers) {
			Logger.getLogger(loggerName).setLevel(Level.OFF);
		}

		LibraryExplorer explorer = new LibraryExplorer(
				"/Users/mariosangiorgio/Music/iTunes/iTunes Media/Music/Friendly Fires");
		explorer.explore();
	}
}
