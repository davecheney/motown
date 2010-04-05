package net.cheney.motown.server.api;

import static net.cheney.motown.server.api.Path.fromUri;

import com.google.common.collect.Multimap;

import net.cheney.motown.common.api.Header;
import net.cheney.motown.common.api.Request;
import net.cheney.motown.common.api.Version;
import net.cheney.motown.common.api.Message.HeaderAccessor;
import net.cheney.motown.common.api.Request.Method;

public abstract class Environment {
	
	private Parameters params = new Parameters();

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
			public HeaderAccessor<Request> header(Header header) {
				return req.header(header);
			}
			
			@Override
			public Multimap<Header, String> headers() {
				return req.headers();
			}
						
			@Override
			public Path pathInfo() {
				return fromUri(req.uri());
			}
		};
	}

	public abstract Method method();

	public abstract Version version();

	public abstract HeaderAccessor<Request> header(Header header);

	public abstract Multimap<Header, String> headers();

	public abstract Path pathInfo();
	
	public Parameters params() {
		return params;
	}
	
	public <K> K param(Parameter<K> key) {
		return params.get(key);
	}
}

