package view;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Information extends JPanel {
	JTextArea textArea;
	JLabel title;

	public Information(String Title) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		textArea = new JTextArea(5, 20);
		textArea.setText("");
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		title = new JLabel(Title);
		add(title);
		add(scrollPane);
	}

	public void updateInfo(String text) {
		textArea.append("\n" + text);
	}

	public void setInfo(String text) {
		textArea.setText(text);
	}
}
