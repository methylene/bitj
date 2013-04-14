package ooq.asdf.tools;

import static javax.swing.SwingUtilities.invokeLater;

import javax.swing.JFrame;
import javax.swing.JPanel;


public final class JPanelTools {
	
	public static Runnable show(final JPanel panel, final String label) {
		return new Runnable() {
			@Override public void run() {
				invokeLater(new Runnable() {
					@Override public void run() {
						//Create and set up the window.
						final JFrame frame = new JFrame(label);
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

						//Add contents to the window.
						frame.add(panel);

						//Display the window.
						frame.pack();
						frame.setVisible(true);
					}
				});
			}
		};
	};

}
