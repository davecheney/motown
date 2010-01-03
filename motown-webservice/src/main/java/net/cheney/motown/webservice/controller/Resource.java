package net.cheney.motown.webservice.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.cheney.motown.api.Method;

public interface Resource {

	boolean exists();
	
	Date lastModified();
	
	Collection<Method> supportedMethods();

	ByteBuffer entity() throws IOException;

	void put(ByteBuffer entity) throws IOException;
	
	String etag();
	
	long size();

	String displayName();

	boolean delete();

	Resource parent();
	
	boolean isCollection();

	List<? extends Resource> members();

	boolean isLocked();
}
