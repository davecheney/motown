package net.cheney.motown.api;

import static org.apache.commons.lang.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Immutable
public final class StatusLine extends StartLine {

	private final Status status;

	public StatusLine(@Nonnull Version version, @Nonnull Status status) {
		super(version);
		this.status = status;
	}
	
	public Status status() {
		return this.status;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public boolean equals(Object that) {
		return reflectionEquals(this, that);
	}
	
	@Override
	public int hashCode() {
		return reflectionHashCode(this);
	}
}
