package commandLineInterface;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.util.logging.Level;
import java.util.logging.Logger;

import libraryExplorer.LibraryExplorer;

public class Lyrics {
	public static void main(String[] args) {
		// Suppressing jaudiotagger loggers
		String[] loggers = { "org.jaudiotagger.audio",
				"org.jaudiotagger.tag.id3", "org.jaudiotagger.tag.datatype" };
		for (String loggerName : loggers) {
			Logger.getLogger(loggerName).setLevel(Level.OFF);
		}

		// Getting command line parameters
		String libraryPath = null, proxyHost = null;
		int proxyPort = 0;
		boolean override = false;
		StringBuffer buffer = new StringBuffer();
		LongOpt longOptions[] = new LongOpt[3];
		longOptions[0] = new LongOpt("libraryPath", LongOpt.REQUIRED_ARGUMENT,
				buffer, 'l');
		longOptions[1] = new LongOpt("proxyHost", LongOpt.OPTIONAL_ARGUMENT,
				buffer, 'p');
		longOptions[2] = new LongOpt("override", LongOpt.NO_ARGUMENT, null, 'o');

		Getopt options = new Getopt("lyrics", args, "", longOptions);
		while (options.getopt() != -1) {
			int option = options.getLongind();
			switch (option) {
			case 0:
				libraryPath = options.getOptarg();
				break;
			case 1:
				String temp = options.getOptarg();
				String[] proxy = temp.split(":");
				proxyHost = proxy[0];
				proxyPort = Integer.parseInt(proxy[1]);
				break;
			case 2:
				override = true;
				break;
			default:
				break;
			}
		}

		LibraryExplorer explorer;
		if (proxyHost == null) {
			explorer = new LibraryExplorer(libraryPath, override);
		} else {
			explorer = new LibraryExplorer(libraryPath, proxyHost, proxyPort, override);
		}
		explorer.explore();
	}
}
