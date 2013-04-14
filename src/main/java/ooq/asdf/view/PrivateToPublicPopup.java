package ooq.asdf.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ooq.asdf.tools.Base58Tools;
import ooq.asdf.tools.ParameterizedRunnable;
import ooq.asdf.tools.ParameterizedRunnableFactory;

public class PrivateToPublicPopup extends JPanel implements ActionListener {

	private static final int COLUMNS = 40;

	private static final long serialVersionUID = 1L;

	protected final JTextField textField;
	protected final JTextArea textArea;
	private final static String newline = "\n";
	
	public static final ParameterizedRunnable createRunnable() {
		return ParameterizedRunnableFactory.runnableJPanel(new PrivateToPublicPopup(), "Enter private key in base58 format ...");
	}

	public PrivateToPublicPopup() {
		super(new GridBagLayout());

		textField = new JTextField(COLUMNS);
		textField.addActionListener(this);

		textArea = new JTextArea(5, COLUMNS);
		textArea.setEditable(false);
		final JScrollPane scrollPane = new JScrollPane(textArea);

		//Add Components to this panel.
		final GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;
		add(textField, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);
	}

	@Override public void actionPerformed(final ActionEvent evt) {
		final String text = textField.getText();
		textArea.append(Base58Tools.publicFromPrivate(text) + newline);
		textField.selectAll();

		//Make sure the new text is visible, even if there
		//was a selection in the text area.
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

}
