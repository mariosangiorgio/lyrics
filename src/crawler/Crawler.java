package crawler;

import org.apache.http.HttpHost;

import crawler.webClient.ContentDownloader;

public abstract class Crawler {
	protected ContentDownloader downloader;
	protected HttpHost host;
	
	public Crawler() {
		downloader = new ContentDownloader();
	}
	
	public Crawler(String proxyHostname, int proxyPort) {
		downloader = new ContentDownloader();
		downloader.setupProxy(proxyHostname, proxyPort);
	}

	public String getLyrics(String author, String title) throws LyricsNotFoundException {
		String address = search(author, title);
		return getLyrics(address);
	}
	protected abstract String getLyrics(String address) throws LyricsNotFoundException;
	protected abstract String search(String author, String title) throws LyricsNotFoundException;
}
