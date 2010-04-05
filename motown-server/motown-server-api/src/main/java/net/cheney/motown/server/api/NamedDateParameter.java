package net.cheney.motown.server.api;

import java.util.Date;

public class NamedDateParameter extends NamedParameter<java.util.Date> {

	public NamedDateParameter(String name) {
		super(name, java.util.Date.class);
	}

	@Override
	public Date decode(String string) {
		return new Date(string);
	}

	@Override
	public String encode(Date date) {
		return date.toGMTString();
	}

}
