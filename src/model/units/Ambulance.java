package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();

	}

	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException{
		if (!(r instanceof Citizen)) {
			throw new IncompatibleTargetException(this, r, "This unit cannot treat a Building");
		}
		else if (!this.canTreat(r)) {
			throw new CannotTreatException(this, r, "Target doesn't require this unit");
		}
		if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0 && getState() == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
	}

	@Override
	public boolean canTreat(Rescuable r) {
		Citizen target = (Citizen) r;
		if (target.getBloodLoss() == 0 && target.getHp() == 100)
			return false;
		return true;
	}

}
