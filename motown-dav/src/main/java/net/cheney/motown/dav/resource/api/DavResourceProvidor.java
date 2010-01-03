package net.cheney.motown.dav.resource.api;

import java.net.URI;

import net.cheney.motown.dav.LockManagerProvidor;
import net.cheney.motown.webservice.controller.Resource;
import net.cheney.uri.Path;

public interface DavResourceProvidor extends LockManagerProvidor {

	DavResource resolveResource(Path path);

	<V extends Resource> URI relativizeResource(V resource);
	
}
