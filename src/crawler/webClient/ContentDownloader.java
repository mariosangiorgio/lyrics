package crawler.webClient;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class ContentDownloader {
	private DefaultHttpClient client;
	
	public ContentDownloader(){
		client = new DefaultHttpClient();
	}
	
	public void setupProxy(String proxyHostname, int proxyPort){
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(proxyHostname, proxyPort));
	}
	
	public void setupProxyWithCredentials(String proxyHostname, int proxyPort, String username, String password){
		setupProxy(proxyHostname, proxyPort);
		
		client.getCredentialsProvider().setCredentials(
                new AuthScope(proxyHostname, proxyPort),
                new UsernamePasswordCredentials(username, password));
	}
	
	private HttpEntity getContent(HttpHost targetHost, String content) throws DownloadException, IOException{
		HttpGet request = new HttpGet(content);
        request.setHeader(HTTP.USER_AGENT,"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_1; en-us) AppleWebKit/532.3+ (KHTML, like Gecko) Version/4.0.3 Safari/531.9");
        request.setHeader("Accept-Language","en-us,en;q=0.5");
        // Other possibly useful headers
        //request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		//request.setHeader("Accept-Encoding","gzip,deflate");
		//request.setHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        
        HttpResponse response = client.execute(targetHost, request);
        
        if(response.getStatusLine().getStatusCode() != 200){
        	throw new DownloadException(response.getStatusLine().getStatusCode());
        }
        
        HttpEntity entity = response.getEntity();
    	if (entity != null){
    		return entity;
		}
    	else{
    		throw new DownloadException();
    	}
	}
	
	public String getPage(HttpHost targetHost, String content) throws DownloadException, IOException{
		HttpEntity entity = getContent(targetHost, content);
		return EntityUtils.toString(entity);
	}
	
	public byte[] getBinaryData(HttpHost targetHost, String content) throws DownloadException, IOException{
		HttpEntity entity = getContent(targetHost, content);
		return EntityUtils.toByteArray(entity);
	}
}
