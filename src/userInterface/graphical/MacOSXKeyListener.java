/*
 *	Mario Sangiorgio - mariosangiorgio AT gmail DOT com
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

package userInterface.graphical;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MacOSXKeyListener implements KeyListener {
	private MainWindow frame;

	public MacOSXKeyListener(MainWindow frame) {
		this.frame = frame;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.isMetaDown() && (e.getKeyChar()=='w' || e.getKeyChar()=='W')){
			frame.dispose();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
