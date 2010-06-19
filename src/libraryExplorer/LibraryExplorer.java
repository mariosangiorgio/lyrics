package libraryExplorer;

import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import utils.AlternateNames;
import crawler.Crawler;
import crawler.LyricsNotFoundException;
import crawler.MetroLyricsCrawler;
import crawler.SongLyricsCrawler;

public class LibraryExplorer {
	private static Logger logger = Logger.getLogger("LibraryExplorer");
	private String path;
	private Vector<Crawler> crawlers = new Vector<Crawler>();
	private boolean override = false;
	private AlternateNames alternateNames = AlternateNames
			.getAlternateNames("/resources/AlternateNames");

	public LibraryExplorer(String path) {
		this.path = path;
		crawlers.add(new MetroLyricsCrawler());
		crawlers.add(new SongLyricsCrawler());
	}

	public LibraryExplorer(String path, String proxyHostname, int proxyPort) {
		this.path = path;
		crawlers.add(new MetroLyricsCrawler(proxyHostname, proxyPort));
		crawlers.add(new SongLyricsCrawler(proxyHostname, proxyPort));
	}

	public LibraryExplorer(String path, boolean override) {
		this(path);
		this.override = override;
	}

	public LibraryExplorer(String path, String proxyHostname, int proxyPort,
			boolean override) {
		this(path, proxyHostname, proxyPort);
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

					// This option is used when we want to override the lyrics
					// in order to fix their format
					if (override == true) {
						tag.setField(FieldKey.LYRICS, lyrics);
						audioFile.commit();
					}

					// We want to look for the lyrics in the web
					else {
						String artist = tag.getFirst(FieldKey.ARTIST);
						String title = tag.getFirst(FieldKey.TITLE);
						for (String artistName : alternateNames
								.getAlternateNameList(artist)) {

							if (!lyrics.equals("")) {
								break;
							}

							for (Crawler crawler : crawlers) {
								logger.info("Searching lyrics for " + title
										+ " by " + artistName + " with "
										+ crawler.getClass());
								try {
									lyrics = crawler.getLyrics(artistName,
											title);
									tag.setField(FieldKey.LYRICS, lyrics);
									audioFile.commit();
									break;
								} catch (LyricsNotFoundException ex) {
								}
							}
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
