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

package lyrics.crawler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lyrics.crawler.webClient.DownloadException;

import org.apache.http.HttpHost;


public class LyricsWikiaCrawler extends Crawler {
	private static final Pattern lyricsNotFound = Pattern
			.compile("This page needs content. You can help by adding a sentence or a photo!");
	private static final Pattern lyrics = Pattern
			.compile("</div>(&#[&#\\d;<br\\s/>]*)<!--");

	public LyricsWikiaCrawler() {
		super();
		host = new HttpHost("lyrics.wikia.com");
	}

	public LyricsWikiaCrawler(String proxyHostname, int proxyPort) {
		super();
		host = new HttpHost("lyrics.wikia.com");
	}

	@Override
	protected String getLyrics(String address) throws LyricsNotFoundException {
		try {
			String content = downloader.getPage(host, address);
			Matcher lyricsMatcher = lyrics.matcher(content);
			if (lyricsMatcher.find()) {
				String lyrics = lyricsMatcher.group(1);
				lyrics = lyrics.replace("<br />", "\n");
				lyrics = decodeHTML(lyrics);
				lyrics = lyrics.replace("<br />", "\n");
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
		author = author.replace(' ', '_');
		title = title.replace(' ', '_');
		try {
			StringBuilder contentPageBuilder = new StringBuilder("/" + author
					+ ":" + title);
			// Name convention: after the space everything is Uppercased
			int characterAfterUnderscore = 0;
			while ((characterAfterUnderscore = contentPageBuilder.indexOf("_",
					characterAfterUnderscore)) != -1) {
				characterAfterUnderscore++;
				contentPageBuilder.replace(characterAfterUnderscore,
						characterAfterUnderscore + 1, contentPageBuilder
								.substring(characterAfterUnderscore,
										characterAfterUnderscore + 1)
								.toUpperCase());
			}

			String contentPage = contentPageBuilder.toString();

			String content = downloader.getPage(host, contentPage);
			Matcher lyricsMatcher = lyricsNotFound.matcher(content);
			if (!lyricsMatcher.find()) {
				return contentPage;
			}
		} catch (DownloadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new LyricsNotFoundException();
	}

}
