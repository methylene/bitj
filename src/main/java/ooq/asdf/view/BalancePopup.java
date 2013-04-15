package ooq.asdf.view;

import static org.slf4j.LoggerFactory.getLogger;

import java.awt.GridBagConstraints;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.slf4j.Logger;

import ooq.asdf.tools.Base58Tools;
import ooq.asdf.tools.JPanelTools;
import ooq.asdf.tools.RunnableFactory;

public final class BalancePopup extends JPanel implements ActionListener {

	private static final int COLUMNS = 40;

	private static final long serialVersionUID = 1L;

	private final static String LF = "\n";
	
	private final JTextField textField;
	private final JTextArea textArea;

	public static final RunnableFactory FACTORY = new RunnableFactory() {
			@Override public Runnable newRunnable(final Map<String, String> params) {
				for (final Entry<String, String> e: params.entrySet()) {
					final Logger log = getLogger(BalancePopup.class);
					log.info("ignoring unrecognized param `-{}'", e.getKey());
				}
				return JPanelTools.show(new BalancePopup(), "Enter public key ...");
			}
		};

	public BalancePopup() {
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
		textArea.append(Base58Tools.getBalance(text) + LF);
		textField.selectAll();

		//Make sure the new text is visible, even if there
		//was a selection in the text area.
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

}
