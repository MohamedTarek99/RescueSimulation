package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.CommandCenter;
import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.units.Unit;

public class View extends JFrame implements ActionListener {
	JPanel rescuePanel, actionsPanel;
	JPanel AMBPanel, DCUPanel, GCUPanel, FTPanel, EVCPanel;
	Information info, log;
	JButton nextCycle;
	ArrayList<UnitCell> unitCells;
	JLabel state, casualties;
	Cell[] cells;
	String emptyClearFixLabel = "                                                                      ";
	CommandCenter commander;
	
	public JPanel getAMBPanel() {
		return AMBPanel;
	}

	public JPanel getDCUPanel() {
		return DCUPanel;
	}

	public JPanel getGCUPanel() {
		return GCUPanel;
	}

	public JPanel getFTPanel() {
		return FTPanel;
	}

	public JPanel getEVCPanel() {
		return EVCPanel;
	}

	public ArrayList<UnitCell> getUnitCells() {
		return unitCells;
	}

	public CommandCenter getCommander() {
		return commander;
	}

	public JPanel getRescuePanel() {
		return rescuePanel;
	}

	public Information getInfoPanel() {
		return info;
	}

	public Information getLogPanel() {
		return log;
	}

	public JPanel getActionsPanel() {
		return actionsPanel;
	}

	public void addUnitCell(UnitCell u) {
		unitCells.add(u);
	}

	public void setUnitCellDesc(int index, String[] desc) {
		unitCells.get(index).updateCell(desc);
	}

	public JLabel getCasualties() {
		return casualties;
	}

	public Cell[] getCells() {
		return cells;
	}

	public String getEmptyClearFixLabel() {
		return emptyClearFixLabel;
	}

	public JButton getNextCycle() {
		return nextCycle;
	}

	public View(CommandCenter c) {
		super("Rescue Simulation Team 6");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		cells = new Cell[100];
		commander = c;
		for (int i = 0; i < 100; i++) {
			cells[i] = new Cell();
			final int tmpI = i;
			cells[i].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					if(!commander.getEngine().checkGameOver())
					commander.respondToCell(tmpI);
					info.setInfo(commander.getDescFromIndex(tmpI));
				}
			});
		}
		unitCells = new ArrayList<UnitCell>();
		setBounds(100, 100, 1200, 800);
		renderRescuePanel();
		renderUnitsPanel();
		renderInfoPanel();
		renderActionsPanel();
		setVisible(true);
	}

	void renderRescuePanel() {
		rescuePanel = new JPanel();
		rescuePanel.setBackground(Color.gray);
		rescuePanel.setLayout(new GridLayout(10, 10));
		rescuePanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		for (int i = 0; i < 100; i++) {
			Cell c = cells[i];
			rescuePanel.add(c);
		}

		add(rescuePanel, BorderLayout.CENTER);
	}

	void renderUnitsPanel() {
		JPanel unitsPanel = new JPanel(new GridLayout(5, 1));
		renderAmbulancePanel(unitsPanel);
		renderDCUPanel(unitsPanel);
		renderFTPanel(unitsPanel);
		renderGCUPanel(unitsPanel);
		renderEVCPanel(unitsPanel);
		add(unitsPanel, BorderLayout.LINE_END);
	}

	void renderAmbulancePanel(JPanel unitsPanel) {
		AMBPanel = new JPanel(new BorderLayout());
		AMBPanel.setBackground(Color.gray);
		AMBPanel.setLayout(new BoxLayout(AMBPanel, BoxLayout.PAGE_AXIS));
		JLabel title = new JLabel("Ambulances");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Color.white);
		AMBPanel.add(title);
		JScrollPane asp = new JScrollPane(AMBPanel);
		asp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		unitsPanel.add(asp);
	}

	void renderDCUPanel(JPanel unitsPanel) {
		DCUPanel = new JPanel();
		DCUPanel.setBackground(Color.gray);
		DCUPanel.setLayout(new BoxLayout(DCUPanel, BoxLayout.PAGE_AXIS));
		JScrollPane asp = new JScrollPane(DCUPanel);
		asp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JLabel title = new JLabel("Disease Control Units");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Color.white);
		DCUPanel.add(title);
		unitsPanel.add(asp);
	}

	void renderGCUPanel(JPanel unitsPanel) {
		GCUPanel = new JPanel();
		GCUPanel.setBackground(Color.gray);
		GCUPanel.setLayout(new BoxLayout(GCUPanel, BoxLayout.PAGE_AXIS));
		JScrollPane asp = new JScrollPane(GCUPanel);
		asp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JLabel title = new JLabel("Gas Control Units");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Color.white);
		GCUPanel.add(title);
		unitsPanel.add(asp);
	}

	void renderFTPanel(JPanel unitsPanel) {
		FTPanel = new JPanel();
		FTPanel.setBackground(Color.gray);
		FTPanel.setLayout(new BoxLayout(FTPanel, BoxLayout.PAGE_AXIS));
		JScrollPane asp = new JScrollPane(FTPanel);
		asp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JLabel title = new JLabel("Fire Trucks");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Color.white);
		FTPanel.add(title);
		unitsPanel.add(asp);
	}

	void renderEVCPanel(JPanel unitsPanel) {
		EVCPanel = new JPanel();
		EVCPanel.setBackground(Color.gray);
		EVCPanel.setLayout(new BoxLayout(EVCPanel, BoxLayout.PAGE_AXIS));
		JScrollPane asp = new JScrollPane(EVCPanel);
		asp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JLabel title = new JLabel("Evacuators");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Color.white);
		EVCPanel.add(title);
		unitsPanel.add(asp);
	}

	void renderInfoPanel() {
		JPanel infoPanel = new JPanel();
		infoPanel.setBackground(Color.gray);
		infoPanel.setLayout(new GridLayout(2, 1));
		log = new Information("Log");
		infoPanel.add(log);
		info = new Information("Info");
		infoPanel.add(info);
		add(infoPanel, BorderLayout.LINE_START);
	}

	void renderActionsPanel() {
		actionsPanel = new JPanel();
		actionsPanel.setBackground(Color.orange);

		nextCycle = new JButton("Next Cycle (" + 0 + ")");
		nextCycle.addActionListener(this);
		casualties = new JLabel("Casualties: (" + 0 + ")");
		casualties.setForeground(Color.red);
		actionsPanel.add(nextCycle);
		actionsPanel.add(casualties);
		add(actionsPanel, BorderLayout.PAGE_END);
	}

	public static void main(String[] args) throws Exception {
		CommandCenter x = new CommandCenter();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		// TODO Auto-generated method stub
		if(commander.getEngine().checkGameOver()) {
			JOptionPane.showMessageDialog(null, "Game is already over");
			return;
		}
		String action = ae.getActionCommand();
		if (action.startsWith("Next Cycle")) {
			try {
				commander.handleNextCycle();
			} catch (BuildingAlreadyCollapsedException e) {
//				JOptionPane.showMessageDialog(null, "Building already collapsed");
//				e.printStackTrace();
			} catch (CitizenAlreadyDeadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (action.equals("Respond")) {
			
		}
	}
}
