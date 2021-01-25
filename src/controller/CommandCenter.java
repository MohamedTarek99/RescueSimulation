package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulator;
import view.Cell;
import view.UnitCell;
import view.View;

public class CommandCenter implements SOSListener {
	private int currentCycle, casualities, lastClickedCell;
	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private View view;
	@SuppressWarnings("unused")
	private ArrayList<Unit> emergencyUnits;
	private Unit currentlySelectedUnit;

	public CommandCenter() throws Exception {
		engine = new Simulator(this);
		currentCycle = 0;
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		view = new View(this);
		int i = 0;
		for (Unit u : emergencyUnits) {
			UnitCell uc = null;
			if (u instanceof Ambulance) {
				Ambulance a = (Ambulance) u;
				uc = new UnitCell("Ambulance", a.getDesc(), i);
				view.addUnitCell(uc);
				view.getAMBPanel().add(uc);
			} else if (u instanceof DiseaseControlUnit) {
				DiseaseControlUnit a = (DiseaseControlUnit) u;
				uc = new UnitCell("DiseaseControlUnit", a.getDesc(), i);
				view.addUnitCell(uc);
				view.getDCUPanel().add(uc);
			} else if (u instanceof GasControlUnit) {
				GasControlUnit a = (GasControlUnit) u;
				uc = new UnitCell("GasControlUnit", a.getDesc(), i);
				view.addUnitCell(uc);
				view.getGCUPanel().add(uc);
			} else if (u instanceof FireTruck) {
				FireTruck a = (FireTruck) u;
				uc = new UnitCell("FireTruck", a.getDesc(), i);
				view.addUnitCell(uc);
				view.getFTPanel().add(uc);
			} else if (u instanceof Evacuator) {
				Evacuator a = (Evacuator) u;
				uc = new UnitCell("Evacuator", a.getDesc(), i);
				uc.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						if (getEngine().checkGameOver()) {
							JOptionPane.showMessageDialog(null, "Game is already over");
							return;
						}
						view.getInfoPanel().setInfo(a.getPassengersInfo());
					}
				});
				view.addUnitCell(uc);
				view.getEVCPanel().add(uc);
			}
			if (uc != null) {
				uc.getRespond().addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						if (getEngine().checkGameOver()) {
							JOptionPane.showMessageDialog(null, "Game is already over");
							return;
						}
						currentlySelectedUnit = u;
					}
				});
				i++;
			}
		}
		view.revalidate();
		view.repaint();
	}

	public void handleNextCycle() throws BuildingAlreadyCollapsedException, CitizenAlreadyDeadException {
		currentlySelectedUnit=null;
		engine.nextCycle();
		currentCycle++;
		casualities = 0;
		Cell[] cells = view.getCells();
		String log = "Cycle #" + currentCycle + ": \n";
		for (ResidentialBuilding rb : visibleBuildings) {
			int cellIndex = getCellIndexFromAddress(rb.getLocation());
			String disasterName = rb.getDisaster().getClass().getName().split("\\.")[2];
			if (rb.getDisaster().getStartCycle() == currentCycle) {
				log += "Building in location " + rb.getLocation().toString() + " was struck by a" + disasterName + "\n";
			}
			for (Citizen c : rb.getOccupants()) {
				if (c.getState() == CitizenState.DECEASED) {
					casualities++;
				}
			}
			cells[cellIndex].updateCell(rb.getStructuralIntegrity() == 0 ? "Destroyed"
					: !(rb.getDisaster().isActive()) ? "Building" : disasterName);

			if (cellIndex == lastClickedCell) {
				view.getInfoPanel().setInfo(getDescFromIndex(cellIndex));
			}
		}
		for (Citizen c : visibleCitizens) {
			int cellIndex = getCellIndexFromAddress(c.getLocation());
			String disasterName = c.getDisaster().getClass().getName().split("\\.")[2];
			if (c.getDisaster().getStartCycle() == currentCycle) {
				log += "Citizen " + c.getName() + " in location " + c.getLocation().toString() + " was struck by an"
						+ disasterName + "\n";
			}
			if (c.getState() == CitizenState.DECEASED) {
				casualities++;
			}
			cells[cellIndex].updateCell(c.getState() == CitizenState.DECEASED ? "Dead"
					: !(c.getDisaster().isActive()) ? "Citizen" : disasterName);

			if (cellIndex == lastClickedCell) {
				view.getInfoPanel().setInfo(getDescFromIndex(cellIndex));
			}
		}

		int i = 0;
		for (Unit u : emergencyUnits) {
			if (u instanceof Ambulance) {
				Ambulance a = (Ambulance) u;
				view.setUnitCellDesc(i, a.getDesc());
			} else if (u instanceof DiseaseControlUnit) {
				DiseaseControlUnit a = (DiseaseControlUnit) u;
				view.setUnitCellDesc(i, a.getDesc());
			} else if (u instanceof GasControlUnit) {
				GasControlUnit a = (GasControlUnit) u;
				view.setUnitCellDesc(i, a.getDesc());
			} else if (u instanceof FireTruck) {
				FireTruck a = (FireTruck) u;
				view.setUnitCellDesc(i, a.getDesc());
			} else if (u instanceof Evacuator) {
				Evacuator a = (Evacuator) u;
				view.setUnitCellDesc(i, a.getDesc());
			}
			i++;
		}

		view.getNextCycle().setText("Next Cycle (" + currentCycle + ")");
		view.getCasualties().setText("Casualties: (" + casualities + ")");
		view.getLogPanel().updateInfo(log + "\n");
		if (getEngine().checkGameOver()) {
			JOptionPane.showMessageDialog(null, "Game over at Cycle: " + currentCycle + " Casualties: " + casualities);
			return;
		}
	}

	int getCellIndexFromAddress(Address l) {
		return 10 * l.getX() + l.getY();
	}

	Address getCellAddressFromIndex(int i) {
		return new Address(i / 10, i % 10);
	}

	public Simulator getEngine() {
		return engine;
	}

	public ArrayList<ResidentialBuilding> getVisibleBuildings() {
		return visibleBuildings;
	}

	public ArrayList<Citizen> getVisibleCitizens() {
		return visibleCitizens;
	}

	@Override
	public void receiveSOSCall(Rescuable r) {

		if (r instanceof ResidentialBuilding) {

			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);

		} else {

			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}

	}

	public String getDescFromIndex(int i) {
		for (ResidentialBuilding rb : visibleBuildings) {
			if (getCellIndexFromAddress(rb.getLocation()) == i) {
				return rb.buildingInfo();
			}
		}
		for (Citizen c : visibleCitizens) {
			if (getCellIndexFromAddress(c.getLocation()) == i) {
				return c.getCitizenInfo(true);
			}
		}
		return "Nothing Here";
	}

	public void respondToCell(int i) {
		lastClickedCell = i;
		if (currentlySelectedUnit == null) {
			return;
		}
		try {
			for (ResidentialBuilding rb : visibleBuildings) {
				if (getCellIndexFromAddress(rb.getLocation()) == i) {
					currentlySelectedUnit.respond(rb);
					currentlySelectedUnit=null;
					return;
				}
			}
			for (Citizen c : visibleCitizens) {
				if (getCellIndexFromAddress(c.getLocation()) == i) {
					currentlySelectedUnit.respond(c);
					currentlySelectedUnit=null;
					return;
				}
			}
		} catch (CannotTreatException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			currentlySelectedUnit=null;
			return;
			// e.printStackTrace();
		} catch (IncompatibleTargetException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			currentlySelectedUnit=null;
			return;
			// e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "This cell is empty");
		currentlySelectedUnit=null;
	}

}
