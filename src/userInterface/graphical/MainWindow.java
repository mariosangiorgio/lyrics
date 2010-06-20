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

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 7216505715440605172L;

	// Components
	JLabel libraryLabel, fixLibraryLabel;
	JButton chooseLocationButton, goButton;
	JCheckBox fixCheckBox;

	public MainWindow() {
		super("Lyrics");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(3, 2));

		libraryLabel = new JLabel("Library location:");
		chooseLocationButton = new JButton("Choose location");
		fixLibraryLabel = new JLabel("Fix libraries:");
		fixCheckBox = new JCheckBox();
		goButton = new JButton("Go!");

		getContentPane().add(libraryLabel);
		getContentPane().add(chooseLocationButton);
		getContentPane().add(fixLibraryLabel);
		getContentPane().add(fixCheckBox);
		getContentPane().add(goButton);

		addListeners();

		pack();
	}

	private void addListeners() {
		MacOSXKeyListener listener = new MacOSXKeyListener(this);
		addKeyListener(listener);
		libraryLabel.addKeyListener(listener);
		chooseLocationButton.addKeyListener(listener);
		fixLibraryLabel.addKeyListener(listener);
		fixCheckBox.addKeyListener(listener);
		goButton.addKeyListener(listener);
	}

	public void changeLabel(String newLabel) {
		libraryLabel.setText(newLabel);
		repaint();
	}
}
