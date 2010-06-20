/*
 *	Mario Sangiorgio - mariosangiorgio AT gmail DOT com
 *
 *  This file is part of lyrics.
 * 
 *  lyrics is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  lyrics is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with lyrics.  If not, see <http://www.gnu.org/licenses/>.
 */

package userInterface.commandLine;

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
