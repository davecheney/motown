package net.cheney.motown.mvc.resource.api;

import net.cheney.motown.mvc.resource.api.Lock.Scope;
import net.cheney.motown.mvc.resource.api.Lock.Type;

public interface LockManager {

	Lock lock(Resource resource, Type type, Scope scope);
	
	Lock unlock(Resource resource);

	boolean isLocked(Resource resource);
}