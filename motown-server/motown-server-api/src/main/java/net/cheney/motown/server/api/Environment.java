package net.cheney.motown.server.api;

import java.net.URI;

import com.google.common.collect.Multimap;

import net.cheney.motown.common.api.Header;
import net.cheney.motown.common.api.Request;
import net.cheney.motown.common.api.Version;
import net.cheney.motown.common.api.Message.HeaderAccessor;
import net.cheney.motown.common.api.Request.Method;

public abstract class Environment {

	public static Environment fromRequest(final Request req) {
		return new Environment() {
			
			@Override
			public Method method() {
				return req.method();
			}
			
			@Override
			public Version version() {
				return req.version();
			}
			
			@Override
			public URI uri() {
				return req.uri();
			}
			
			@Override
			public HeaderAccessor<Request> header(Header header) {
				return req.header(header);
			}
			
			@Override
			public Multimap<Header, String> headers() {
				return req.headers();
			}
		};
	}

	public abstract Method method();

	public abstract Version version();

	public abstract URI uri();

	public abstract HeaderAccessor<Request> header(Header header);

	public abstract Multimap<Header, String> headers();
	
}

