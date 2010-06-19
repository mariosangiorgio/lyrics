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

import org.apache.http.HttpHost;

import crawler.webClient.ContentDownloader;

public abstract class Crawler {
	protected ContentDownloader downloader;
	protected HttpHost host;

	public Crawler() {
		downloader = new ContentDownloader();
		setHostAddress();
	}

	public Crawler(String proxyHostname, int proxyPort) {
		this();
		downloader.setupProxy(proxyHostname, proxyPort);
	}

	public String getLyrics(String author, String title)
			throws LyricsNotFoundException {
		String address = search(author, title);
		return getLyrics(address);
	}

	protected abstract void setHostAddress();

	protected abstract String getLyrics(String address)
			throws LyricsNotFoundException;

	protected abstract String search(String author, String title)
			throws LyricsNotFoundException;

	protected String encodeSpecialCharacters(String plainString){
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
