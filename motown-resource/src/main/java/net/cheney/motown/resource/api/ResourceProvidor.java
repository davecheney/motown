package net.cheney.motown.resource.api;

import java.net.URI;

import net.cheney.motown.uri.Path;

public interface ResourceProvidor extends LockManagerProvidor {

	Resource resolveResource(Path path);

	URI relativizeResource(Resource resource);
	
}
