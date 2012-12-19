package mab.common.commander;

public enum EnumTeam {
	black(0x000000),
	red(0xFF0000),
	green(0x00005d),
	brown(0x800040),
	blue(0x00FF00),
	purple(0x80FF00),
	cyan(0x00A0FF),
	silver(0xA8A8A8),
	grey(0x545454),
	pink(0xffff80),
	lime(0x0000FF),
	yellow(0xFF00FF),
	lightblue(0x80FF80),
	magenta(0xD0D000),
	orange(0xFF0080),
	white(0xFFFFFF);

	@Override
	public String toString() {
		return "team."+name();
	}
	
	private int rgb;
	
	private EnumTeam(int rgb){
		this.rgb = rgb;
	}
	
	public int getRGB(){
		return rgb;
	}
	
	public int getRGBA(int i){
		return rgb | i << 24;
	}
}