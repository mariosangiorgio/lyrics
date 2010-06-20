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

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainWindow extends JFrame{
	private static final long serialVersionUID = 7216505715440605172L;
	
	public MainWindow() {
		super("Lyrics");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(3,2));
		
		getContentPane().add(new JLabel("Library location:"));
		getContentPane().add(new JButton("Choose location"));
		getContentPane().add(new JLabel("Fix libraries:"));
		getContentPane().add(new JCheckBox());
		getContentPane().add(new JButton("Go!"));
		
		pack();
	}
}
