package mab.commander;

public enum EnumTeam {
	black(0x000000),
	red(0xFF0000),
	green(0x005d00),
	brown(0x804000),
	blue(0x0000FF),
	purple(0x8000FF),
	cyan(0x00FFA0),
	silver(0xA8A8A8),
	grey(0x545454),
	pink(0xff80ff),
	lime(0x00FF00),
	yellow(0xFFFF00),
	lightblue(0x8080FF),
	magenta(0xD000D0),
	orange(0xFF8000),
	white(0xFFFFFF);

	@Override
	public String toString() {
		return "colour."+name();
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