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


public class MetroLyricsCrawler extends Crawler {
	private static final Pattern lyricsURL = Pattern
			.compile("<p>Exact Title Match - <a href=\"([^\"]*)\"");
	private static final Pattern lyrics = Pattern
			.compile("<div id=\"lyrics\">([&#\\d;<br\\s/>]*)</div>");

	public MetroLyricsCrawler() {
		super();
		host = new HttpHost("www.metrolyrics.com");
	}

	public MetroLyricsCrawler(String proxyHostname, int proxyPort) {
		super();
		host = new HttpHost("www.metrolyrics.com");
	}

	@Override
	protected String search(String author, String title)
			throws LyricsNotFoundException {
		author = author.replace(' ', '+');
		title = title.replace(' ', '+');
		try {
			String contentPage = "/search.php?search=" + author + "+" + title
					+ "&category=artisttitle";
			String content = downloader.getPage(host, contentPage);
			Matcher lyricsURLMatcher = lyricsURL.matcher(content);
			if (lyricsURLMatcher.find()) {
				return "/" + lyricsURLMatcher.group(1);
			}
		} catch (DownloadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new LyricsNotFoundException();
	}

	@Override
	protected String getLyrics(String address) throws LyricsNotFoundException {
		try {
			String content = downloader.getPage(host, address);
			Matcher lyricsMatcher = lyrics.matcher(content);
			if (lyricsMatcher.find()) {
				String lyrics = lyricsMatcher.group(1);
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
}
