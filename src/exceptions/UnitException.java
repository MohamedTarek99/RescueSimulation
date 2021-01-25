package exceptions;

import model.units.Unit;
import simulation.Rescuable;

public abstract class UnitException extends SimulationException {
	private Unit unit;

	public Unit getUnit() {
		return unit;
	}

	public Rescuable getTarget() {
		return target;
	}

	private Rescuable target;

	public UnitException(Unit unit, Rescuable target) {
		super();
		this.target = target;
		this.unit = unit;
	}

	public UnitException(Unit unit, Rescuable target, String message) {
		super(message);
		this.target = target;
		this.unit = unit;
	}
}
