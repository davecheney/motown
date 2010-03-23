package net.cheney.motown.common.api;

public enum Scheme {

	HTTP,
	HTTPS;
	
	public static Scheme parse(String name) {
		return valueOf(name.toUpperCase());
	}
}
