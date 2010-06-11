package crawler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;

import crawler.webClient.DownloadException;

public class MetroLyricsCrawler extends Crawler {
	private static final Pattern lyricsURL = Pattern
			.compile("<p>Exact Title Match - <a href=\"([^\"]*)\"");
	private static final Pattern lyrics = Pattern
			.compile("<div id=\"lyrics\">([&#\\d;<br\\s/>]*)</div>");

	public MetroLyricsCrawler() {
		super();
	}

	public MetroLyricsCrawler(String proxyHostname, int proxyPort) {
		super();
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

	@Override
	protected void setHostAddress() {
		host = new HttpHost("www.metrolyrics.com");
	}
}
