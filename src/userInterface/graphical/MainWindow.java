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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import libraryExplorer.LibraryExplorer;
import libraryExplorer.OutputListener;
import userInterface.graphical.listeners.FileChooserListener;
import userInterface.graphical.listeners.MacOSXKeyListener;

public class MainWindow extends JFrame implements OutputListener {
	private static final long serialVersionUID = 7216505715440605172L;

	// Parameters
	private File libraryLocation;

	// Components
	private JPanel menuPanel;
	private JLabel libraryLabel, fixLibraryLabel;
	private JButton chooseLocationButton, goButton;
	private JCheckBox fixCheckBox;
	private JTextPane text;

	public MainWindow() {
		super("Lyrics");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		initializeComponents();
		addComponents();
		addListeners();
		
		getContentPane().add(menuPanel,BorderLayout.NORTH);
		getContentPane().add(text,BorderLayout.SOUTH);
		
		pack();
	}
	
	private void initializeComponents(){
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(3, 2));
		
		text = new JTextPane();
		text.setEditable(false);
		
		libraryLabel = new JLabel("Library location:");
		chooseLocationButton = new JButton("Choose location");
		fixLibraryLabel = new JLabel("Fix libraries:");
		fixCheckBox = new JCheckBox();
		goButton = new JButton("Go!");
	}

	private void addComponents() {
		menuPanel.add(libraryLabel);
		menuPanel.add(chooseLocationButton);
		menuPanel.add(fixLibraryLabel);
		menuPanel.add(fixCheckBox);
		menuPanel.add(goButton);
		menuPanel.add(text);
	}

	private void addListeners() {
		goButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				run();
			}
		});

		chooseLocationButton.addMouseListener(new FileChooserListener(this));

		// Listener for the system key chords
		MacOSXKeyListener listener = new MacOSXKeyListener(this);
		addKeyListener(listener);
		libraryLabel.addKeyListener(listener);
		chooseLocationButton.addKeyListener(listener);
		fixLibraryLabel.addKeyListener(listener);
		fixCheckBox.addKeyListener(listener);
		goButton.addKeyListener(listener);
		text.addKeyListener(listener);
	}

	public void changeLabel(String newLabel) {
		libraryLabel.setText(newLabel);
		repaint();
	}

	public void setLibraryLocation(File libraryLocation) {
		this.libraryLocation = libraryLocation;
	}

	private void run() {
		LibraryExplorer libraryExplorer;
		libraryExplorer = new LibraryExplorer(libraryLocation.toString(),
				fixCheckBox.isSelected());
		libraryExplorer.addOutputListener(this);

		libraryExplorer.explore();
	}

	@Override
	public void displaySuccessfulOperation(String message) {
		text.setText(message+"\n"+text.getText());
		repaint();
	}

	@Override
	public void displayUnsuccessfulOperation(String message) {
		text.setText(message+"\n"+text.getText());
		repaint();
	}
}
