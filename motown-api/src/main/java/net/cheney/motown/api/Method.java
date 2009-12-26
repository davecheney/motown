package net.cheney.motown.api;

import javax.annotation.Nonnull;

public enum Method {
	// RFC 2516
	
	OPTIONS,
	GET,
	DELETE,
	HEAD, 
	PUT,
	POST,
	TRACE,

	// RFC 2518

	// DAV Level 1
	
	COPY,
	MOVE, 
	MKCOL,
	PROPFIND,
	PROPPATCH,
	
	// DAV Level 2
	
	LOCK,
	UNLOCK,
	
	// RFC 3744
	
	ACL, 
	
	// DeltaV RFC 3253
	
	REPORT,
	MKACTIVITY,
	MERGE,
	CHECKIN,
	UNCHECKOUT,
	UPDATE,
	LABEL,
	MKWORKSPACE, VERSION_CONTROL, CHECKOUT, SEARCH;
	
	public static Method parse(@Nonnull CharSequence method) {
		return valueOf(method.toString());
	}
}