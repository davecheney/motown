package net.cheney.motown.dav;

import java.util.UUID;

import net.cheney.motown.dav.resource.api.DavResource;

public final class Lock {

	public enum Scope {	NONE, SHARED, EXCLUSIVE }
	
	public enum Type { NONE, READ, WRITE }
	
	private final Type type;
	private final Scope scope;
	private final String token;
	private final DavResource resource;
	
	public Lock(Type type, Scope scope, DavResource resource) {
		this.type = type;
		this.scope = scope;
		this.token = generateToken();
		this.resource = resource;
	}

	private final String generateToken() {
		return "opaquelocktoken:"+UUID.randomUUID().toString();
	}
	
	public final DavResource resource() {
		return resource;
	}
	
	public final Lock.Type type() {
		return type;
	}
	
	public final Lock.Scope scope() {
		return scope;
	}
	
	public final String token() {
		return token;
	}

}
