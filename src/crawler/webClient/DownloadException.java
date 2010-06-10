package crawler.webClient;

public class DownloadException extends Exception {

	private static final long serialVersionUID = 7531442970679656093L;
	
	private int statusCode;
	
	public DownloadException(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public DownloadException(){
		statusCode = -1;
	}
	
	public String getMessage(){
		if(statusCode != -1){
			return "Download Exception. Status code: "+statusCode;
		}
		else{
			return "Download Exception. Response OK returned a null entity";
		}
	}

}
