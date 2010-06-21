/*
 * 	Mario Sangiorgio - mariosangiorgio AT gmail DOT com
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

package crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;

import crawler.webClient.ContentDownloader;

/**
 * Abstract class that defines the behavior of a lyrics crawler. All the
 * site-specific crawlers have to implement it
 * 
 * @author mariosangiorgio
 * 
 */
public abstract class Crawler {
	/**
	 * The object that is in charge of downloading the pages from the web
	 */
	protected ContentDownloader downloader;
	/**
	 * The host where the crawler has to search for the lyrics
	 */
	protected HttpHost host;

	/**
	 * Initializes the crawler with a new downloader. Please note that the
	 * subclasses will have to redefine it, adding the host definition.
	 */
	public Crawler() {
		downloader = new ContentDownloader();
	}

	/**
	 * Initializes the crawler with the specified proxy settings. Please note
	 * that the subclasses will have to redefine it, adding the host definition.
	 * 
	 * @param proxyHostname
	 *            is the address of the proxy
	 * @param proxyPort
	 *            is the port of the proxy
	 */
	public Crawler(String proxyHostname, int proxyPort) {
		this();
		downloader.setupProxy(proxyHostname, proxyPort);
	}

	/**
	 * Searches on the web for the lyrics and returns them (If found)
	 * 
	 * @param author
	 *            is the author of the song
	 * @param title
	 *            is the title of the song
	 * @return is the lyrics of the song
	 * @throws LyricsNotFoundException
	 *             when the website can't provide the lyrics
	 */
	public String getLyrics(String author, String title)
			throws LyricsNotFoundException {
		String address;

		// Removing text in parentheses from the title
		Pattern textInParentheses = Pattern.compile("\\s*\\([^\\)]*\\)\\s*");
		Matcher parentesesMatcher = textInParentheses.matcher(title);
		title = parentesesMatcher.replaceAll("");

		address = search(author, title);
		return getLyrics(address);
	}

	/**
	 * Method that will actually download the lyrics
	 * 
	 * @param address
	 *            the address of the page that contains teh lyricss
	 * @return the lyrics found at the specified address
	 * @throws LyricsNotFoundException
	 *             if the specified page does not contain any lyrics
	 */
	protected abstract String getLyrics(String address)
			throws LyricsNotFoundException;

	/**
	 * Method to retrieve the address of the page containing the lyrics
	 * 
	 * @param author
	 *            the author of the song
	 * @param title
	 *            the title of the song
	 * @return the address of the page containing the lyrics
	 * @throws LyricsNotFoundException
	 *             if the crawler is not able to find the address
	 */
	protected abstract String search(String author, String title)
			throws LyricsNotFoundException;

	/**
	 * An utility method to make the song artist name and title HTTP friendly
	 * 
	 * @param plainString
	 *            the string to encode
	 * @return the encoded version of the string
	 */
	protected String encodeSpecialCharacters(String plainString) {
		plainString = plainString.replace("$", "%24");
		plainString = plainString.replace("&", "%26");
		plainString = plainString.replace("+", "%2B");
		plainString = plainString.replace(",", "%2C");
		plainString = plainString.replace("/", "%2F");
		plainString = plainString.replace("$", "%24");
		plainString = plainString.replace(":", "%3A");
		plainString = plainString.replace(";", "%3B");
		plainString = plainString.replace("=", "%3D");
		plainString = plainString.replace("?", "%3F");
		plainString = plainString.replace("@", "%40");
		return plainString;
	}

	/**
	 * Method to decode HTML encoding that sometimes prevent the user to copy
	 * and paste the lyrics
	 * 
	 * @param encodedString the HTML-encoded strings
	 * @return the decoded version of the string
	 */
	protected String decodeHTML(String encodedString) {
		StringBuffer ostr = new StringBuffer();
		int i1 = 0;
		int i2 = 0;
		while (i2 < encodedString.length()) {
			i1 = encodedString.indexOf("&", i2);
			if (i1 == -1) {
				ostr
						.append(encodedString.substring(i2, encodedString
								.length()));
				break;
			}
			ostr.append(encodedString.substring(i2, i1));
			i2 = encodedString.indexOf(";", i1);
			if (i2 == -1) {
				ostr
						.append(encodedString.substring(i1, encodedString
								.length()));
				break;
			}

			String tok = encodedString.substring(i1 + 1, i2);
			if (tok.charAt(0) == '#') {
				tok = tok.substring(1);
				try {
					int radix = 10;
					if (tok.trim().charAt(0) == 'x') {
						radix = 16;
						tok = tok.substring(1, tok.length());
					}
					ostr.append((char) Integer.parseInt(tok, radix));
				} catch (NumberFormatException exp) {
					ostr.append('?');
				}
			}
			i2++;
		}
		return ostr.toString();
	}
}
