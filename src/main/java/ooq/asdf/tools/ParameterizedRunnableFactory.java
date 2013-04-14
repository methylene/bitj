package ooq.asdf.tools;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class ParameterizedRunnableFactory {
	
	public static final ParameterizedRunnable runnableJPanel(final JPanel panel, final String label) {
		return new ParameterizedRunnable() {
			@Override public void run() {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
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
