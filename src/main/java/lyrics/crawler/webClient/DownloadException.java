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

package lyrics.crawler.webClient;

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
