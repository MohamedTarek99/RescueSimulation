package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UnitCell extends JPanel {
	private JLabel typeLabel, descLabels[];
	private JPanel description;
	private JButton respond;
	private int index;

	public int getIndex() {
		return index;
	}

	public JButton getRespond() {
		return respond;
	}

	String emptyClearFixLabel = "                                                                      ";

	public UnitCell(String unitType, String[] desc, int index) {
		this.index = index;

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(BorderFactory.createLineBorder(Color.black));

		typeLabel = new JLabel();
		ImageIcon icon = new ImageIcon("resources/" + unitType + ".png");
		Image image = icon.getImage();
		Image newimg = image.getScaledInstance(60, 50, java.awt.Image.SCALE_SMOOTH);
		typeLabel.setIcon(new ImageIcon(newimg));
		typeLabel.setOpaque(true);
		add(typeLabel, BorderLayout.WEST);

		description = new JPanel();
		description.setLayout(new BoxLayout(description, BoxLayout.Y_AXIS));
		descLabels = new JLabel[desc.length];
		int i = 0;
		for (String x : desc) {
			descLabels[i] = new JLabel(x);
			description.add(descLabels[i++]);
		}
		description.add(new JLabel(emptyClearFixLabel));
		add(description);

		respond = new JButton("Respond");
		add(respond);
	}

	public void updateCell(String[] desc) {
		int i = 0;
		for (String x : desc) {
			descLabels[i++].setText(x);
		}
	}
}
