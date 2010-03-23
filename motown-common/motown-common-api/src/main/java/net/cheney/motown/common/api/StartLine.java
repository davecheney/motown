package net.cheney.motown.common.api;

import javax.annotation.Nonnull;

abstract class StartLine {

	private final Version version;

	StartLine(@Nonnull Version version) {
		this.version = version;
	}
	
	public final Version version() {
		return this.version;
	}
	
	@Override
	public abstract int hashCode();
	
	@Override
	public abstract boolean equals(Object obj);

}
