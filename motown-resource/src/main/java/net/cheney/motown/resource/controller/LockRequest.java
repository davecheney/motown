package net.cheney.motown.resource.controller;

import net.cheney.motown.resource.api.Lock.Scope;
import net.cheney.motown.resource.api.Lock.Type;

public final class LockRequest {

	private final Type lockType;
	private final Scope lockScope;
	
	LockRequest(Type lockType, Scope lockScope) {
		this.lockType = lockType;
		this.lockScope = lockScope;
	}
	
	public final Type type() {
		return lockType;
	}
	
	public final Scope scope() {
		return lockScope;
	}
	
	public static final LockRequest sharedLock(final Type lockType) {
		return new LockRequest(lockType, Scope.SHARED);
	}
	
	public static final LockRequest exclusiveLock(final Type lockType) {
		return new LockRequest(lockType, Scope.EXCLUSIVE);
	}
}