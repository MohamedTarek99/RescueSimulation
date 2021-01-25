package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle, WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}

	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0 || target.getOccupants().size() == 0) {
			jobsDone();
			return;
		}

		for (int i = 0; getPassengers().size() != getMaxCapacity() && i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}

		setDistanceToBase(target.getLocation().getX() + target.getLocation().getY());

	}

	@Override
	public boolean canTreat(Rescuable r) {
		ResidentialBuilding target = (ResidentialBuilding) r;
		if (target.getFoundationDamage()==0)
			return false;
		return true;
	}

}
