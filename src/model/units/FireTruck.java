package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getFireDamage() > 0)

			target.setFireDamage(target.getFireDamage() - 10);

		if (target.getFireDamage() == 0)

			jobsDone();

	}

	@Override
	public boolean canTreat(Rescuable r) {
		ResidentialBuilding target = (ResidentialBuilding) r;
		if (target.getFireDamage() == 0 || target.getStructuralIntegrity() == 0)
			return false;
		return true;
	}

	public String[] getDesc() {
		return new String[] { "State: " + this.getState(), "ID: " + getUnitID(),
				"Location: (" + getLocation().getX() + "," + getLocation().getY() + ")",
				"StepsPerCycle: " + getStepsPerCycle() };
	}
}
