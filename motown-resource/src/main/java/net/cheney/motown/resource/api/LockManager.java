package net.cheney.motown.resource.api;

import net.cheney.motown.resource.api.Lock.Scope;
import net.cheney.motown.resource.api.Lock.Type;

public interface LockManager {

	Lock lock(Resource resource, Type type, Scope scope);
	
	Lock unlock(Resource resource);

	boolean isLocked(Resource resource);
}