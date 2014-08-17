package de.cgarbs.knittr;

import java.awt.EventQueue;

import de.cgarbs.knittr.view.MainWindow;

public class Knittr
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
