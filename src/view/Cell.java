package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.disasters.Disaster;

public class Cell extends JPanel {
	JPanel type, units;
	JLabel typeLabel, evc, ft, dcu, gcu, amb;

	Cell() {
		units = new JPanel();
		units.setLayout(new FlowLayout(5));
		type = new JPanel();
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		renderTypePanel();
		renderUnitPanel();

	}

	public void renderTypePanel() {
		GridBagConstraints c = new GridBagConstraints();


		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 0.75;
		c.weighty = 0.75;
		c.gridx = 0;
		c.gridy = 0;

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		typeLabel = new JLabel("");
		typeLabel.setHorizontalTextPosition(JLabel.LEFT);
		typeLabel.setVerticalTextPosition(JLabel.TOP);

		type.add(typeLabel);
		this.add(type, c);
	}

	public void renderUnitPanel() {
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 0.25;
		c.weighty = 0.25;
		c.gridx = 0;
		c.gridy = 1;

		c.fill = GridBagConstraints.BOTH;
		units.setLayout(new GridLayout(1, 5));
//		amb = new JLabel("0");
//		amb.setBackground(Color.red);
//		amb.setOpaque(true);
//		amb.setHorizontalAlignment(JLabel.CENTER);
//
//		dcu = new JLabel("0");
//		dcu.setBackground(Color.blue);
//		dcu.setOpaque(true);
//		dcu.setHorizontalAlignment(JLabel.CENTER);
//
//		gcu = new JLabel("0");
//		gcu.setBackground(Color.green);
//		gcu.setOpaque(true);
//		gcu.setHorizontalAlignment(JLabel.CENTER);
//
//		ft = new JLabel("0");
//		ft.setBackground(Color.orange);
//		ft.setOpaque(true);
//		ft.setHorizontalAlignment(JLabel.CENTER);
//
//		evc = new JLabel("0");
//		evc.setBackground(Color.gray);
//		evc.setOpaque(true);
//		evc.setHorizontalAlignment(JLabel.CENTER);
//
//		units.add(amb);
//		units.add(dcu);
//		units.add(gcu);
//		units.add(ft);
//		units.add(evc);
		this.add(units, c);
	}
	public void updateCell(String cellType) {
		ImageIcon icon = new ImageIcon("resources/" + cellType + ".png");
		Image image = icon.getImage();
		Image newimg = image.getScaledInstance(90, (80 * 75) / 100, java.awt.Image.SCALE_SMOOTH);
		typeLabel.setIcon(new ImageIcon(newimg));
	}
	public void updateCell(String cellType, int c, int evc, int ft, int dcu, int amb, int gcu) {
		ImageIcon icon = new ImageIcon("resources/" + cellType + ".png");
		Image image = icon.getImage();
		Image newimg = image.getScaledInstance(90, (80 * 75) / 100, java.awt.Image.SCALE_SMOOTH);
		typeLabel.setIcon(new ImageIcon(newimg));
//		typeLabel.setText(c + "");
//		this.evc.setText(evc + "");
//		this.ft.setText(ft + "");
//		this.dcu.setText(dcu + "");
//		this.amb.setText(amb + "");
//		this.gcu.setText(gcu + "");
	}

}
