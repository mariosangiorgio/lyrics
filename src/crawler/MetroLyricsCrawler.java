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

	private String decodeHTML(String encodedString) {
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
