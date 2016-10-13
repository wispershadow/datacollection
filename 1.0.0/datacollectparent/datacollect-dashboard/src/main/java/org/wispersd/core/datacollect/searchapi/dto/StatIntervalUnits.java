package org.wispersd.core.datacollect.searchapi.dto;

public enum StatIntervalUnits {
	DAY("d"),
	HOUR("h"),
	MINUTE("m");
	
	private final String val;
	StatIntervalUnits(String val) {
		this.val = val;
	}
	public String getVal() {
		return val;
	}
	
	public static StatIntervalUnits fromVal(String val) {
		for(StatIntervalUnits nextUnit: StatIntervalUnits.values()) {
			if (nextUnit.getVal().equals(val)) {
				return nextUnit;
			}
		}
		return null;
	}
}
