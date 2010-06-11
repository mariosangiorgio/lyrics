package commandLineInterface;
import java.util.logging.Level;
import java.util.logging.Logger;

import libraryExplorer.LibraryExplorer;


public class Lyrics {
	public static void main(String[] args) {
		String[] loggers = { "org.jaudiotagger.audio",
				"org.jaudiotagger.tag.id3", "org.jaudiotagger.tag.datatype" };
		for (String loggerName : loggers) {
			Logger.getLogger(loggerName).setLevel(Level.OFF);
		}

		LibraryExplorer explorer = new LibraryExplorer(
				"/Users/mariosangiorgio/Music/iTunes/iTunes Media/Music");
		explorer.explore();
	}
}
