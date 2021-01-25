package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getGasLevel() > 0)
			target.setGasLevel(target.getGasLevel() - 10);

		if (target.getGasLevel() == 0)
			jobsDone();

	}

	@Override
	public boolean canTreat(Rescuable r) {
		ResidentialBuilding target = (ResidentialBuilding) r;
		if (target.getGasLevel() == 0||target.getStructuralIntegrity()==0)
			return false;
		return true;

	}
	public String[] getDesc() {
		return new String[] { "State: " + this.getState(), "ID: " + getUnitID(),
				"Location: (" + getLocation().getX() + "," + getLocation().getY() + ")",
				"StepsPerCycle: " + getStepsPerCycle() };
	}
}
