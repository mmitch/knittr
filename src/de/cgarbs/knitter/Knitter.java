package de.cgarbs.knitter;

import java.awt.EventQueue;

import de.cgarbs.knitter.view.MainWindow;

public class Knitter
{
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
