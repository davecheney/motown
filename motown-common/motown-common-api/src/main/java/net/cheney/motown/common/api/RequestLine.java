package net.cheney.motown.common.api;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import net.cheney.motown.common.api.Message.Method;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Immutable
public final class RequestLine extends StartLine {

	private final Method method;
	private final URI uri;
	
	public RequestLine(@Nonnull Method method, @Nonnull String uri, @Nonnull Version version) throws URISyntaxException {
		this(method, uriFromString(uri), version);
	}

	public RequestLine(@Nonnull Method method, @Nonnull URI uri, @Nonnull Version version) {
		super(version);
		this.method = method;
		this.uri = uri;
	}
	
	private static URI uriFromString(String uri) throws URISyntaxException {
		return new URI(null, null, uri, null);
	}

	public Method method() {
		return this.method;
	}

	public URI uri() {
		return this.uri;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
