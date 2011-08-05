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

package lyrics.libraryExplorer;

/**
 * Interface that has to be implemented by the classes that wants to be notified
 * about what is going on
 * 
 * @author mariosangiorgio
 * 
 */
public interface OutputListener {
	/**
	 * Method to display a message notifying a successful operation
	 * @param message the message that has to be displayed
	 */
	public void displaySuccessfulOperation(String message);

	/**
	 * Method to display a message notifying a failed operation
	 * @param message the message that has to be displayed
	 */
	public void displayUnsuccessfulOperation(String message);
}
