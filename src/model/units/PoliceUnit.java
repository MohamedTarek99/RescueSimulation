package model.units;

import java.util.ArrayList;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

public abstract class PoliceUnit extends Unit {
	private ArrayList<Citizen> passengers;
	private int maxCapacity;
	private int distanceToBase;

	public PoliceUnit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener,
			int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener);
		passengers = new ArrayList<Citizen>();
		this.maxCapacity = maxCapacity;
	}

	public int getDistanceToBase() {
		return distanceToBase;
	}

	public void setDistanceToBase(int distanceToBase) {
		this.distanceToBase = distanceToBase;
		if (this.distanceToBase <= 0)
			this.distanceToBase = 0;
	}

	@Override
	public void cycleStep() {
		if (distanceToBase != 0) {
			setDistanceToBase(getDistanceToBase() - getStepsPerCycle());
			if (distanceToBase == 0)
				getWorldListener().assignAddress(this, 0, 0);
		} else {
			if (passengers.size() != 0) {

				for (int i = 0; i < passengers.size(); i++) {
					Citizen c = passengers.get(i);
					if (c.getState() != CitizenState.DECEASED)
						c.setState(CitizenState.RESCUED);
					c.getWorldListener().assignAddress(c, 0, 0);
				}
				passengers.clear();
				Address location = ((ResidentialBuilding) getTarget()).getLocation();
				setDistanceToTarget(location.getX() + location.getY());
			} else
				super.cycleStep();
		}
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public ArrayList<Citizen> getPassengers() {
		return passengers;
	}

	public String[] getDesc() {
		return new String[] { "State: " + this.getState(), "MaximumCapacity: " + maxCapacity,
				"Passengers: " + passengers.size(), "ID: " + getUnitID(),
				"Location: (" + getLocation().getX() + "," + getLocation().getY() + ")",
				"StepsPerCycle: " + getStepsPerCycle(), "DistanceToBase: " + getDistanceToBase() };
	}

	public String getPassengersInfo() {
		if (passengers.size() == 0) {
			return "Evacuator is empty!";
		}
		String citizenInfo = "";
		for (int i = 1; i <= passengers.size(); i++) {
			citizenInfo += "================================================\n";

			citizenInfo += "Citizen #" + i + ":\n";
			citizenInfo += passengers.get(i - 1).getCitizenInfo(false) + "\n";
			citizenInfo += "================================================\n";
		}
		return citizenInfo;
	}
}
