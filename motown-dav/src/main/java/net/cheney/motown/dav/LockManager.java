package net.cheney.motown.dav;

import net.cheney.motown.dav.Lock.Scope;
import net.cheney.motown.dav.Lock.Type;
import net.cheney.motown.dav.resource.api.DavResource;

public interface LockManager {

	Lock lock(DavResource resource, Type type, Scope scope);
	
	Lock unlock(DavResource resource);

	boolean isLocked(DavResource resource);
}