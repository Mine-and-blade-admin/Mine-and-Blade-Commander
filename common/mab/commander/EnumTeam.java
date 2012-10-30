package mab.commander;

public enum EnumTeam {
	black,
	red,
	green,
	brown,
	blue,
	purple,
	cyan,
	silver,
	grey,
	pink,
	lime,
	yellow,
	lightblue,
	magenta,
	orange,
	white;

	@Override
	public String toString() {
		return "colour."+name();
	}
}
