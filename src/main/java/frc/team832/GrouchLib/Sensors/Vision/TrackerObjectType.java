package frc.team832.GrouchLib.Sensors.Vision;

public enum TrackerObjectType {
	GAME_ELEMENT_1,
	GAME_ELEMENT_2,
	GAME_ELEMENT_3,
	FIELD_TARGET_1,
	FIELD_TARGET_2,
	FIELD_TARGET_3,
	RED_ROBOT,
	BLUE_ROBOT;

	private String _name;

	TrackerObjectType() { _name = ""; }

	public void setName(String name) {
		if (!_name.equals("")) _name = name;
	}

	public String getName() { return _name; }
}
