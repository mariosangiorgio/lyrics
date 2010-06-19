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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;

import crawler.webClient.DownloadException;

public class SongLyricsCrawler extends Crawler {
	private static final Pattern lyrics = Pattern
			.compile("<p id=\"songLyricsDiv\"[^>]*>\\s*([&#\\d;<br\\s/>]*)\\[[^\\]]*\\]([&#\\d;<br\\s/>]*)</p>");

	public SongLyricsCrawler() {
		super();
	}

	public SongLyricsCrawler(String proxyHostname, int proxyPort) {
		super();
	}

	@Override
	protected String getLyrics(String address) throws LyricsNotFoundException {
		try {
			String content = downloader.getPage(host, address);
			Matcher lyricsMatcher = lyrics.matcher(content);
			if (lyricsMatcher.find()) {
				String lyrics = lyricsMatcher.group(1) + lyricsMatcher.group(2);
				lyrics = lyrics.replace("<br />", "");
				lyrics = decodeHTML(lyrics);
				lyrics = lyrics.replace("<br />", "");
				return lyrics;
			}
		} catch (DownloadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new LyricsNotFoundException();
	}

	@Override
	protected String search(String author, String title)
			throws LyricsNotFoundException {
		String contentAddress = "/" + author + "/" + title + "-lyrics/";
		contentAddress = contentAddress.replace(" ", "-");
		contentAddress = encodeSpecialCharacters(contentAddress);

		try {
			String content = downloader.getPage(host, contentAddress);
			if (!content.contains("Sorry, we have no " + author + " - " + title
					+ " lyrics at the moment.")) {
				return contentAddress;
			}
		} catch (Exception e) {
		}

		throw new LyricsNotFoundException();

	}

	@Override
	protected void setHostAddress() {
		host = new HttpHost("www.songlyrics.com");
	}

}
