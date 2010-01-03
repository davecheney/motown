package net.cheney.motown.dav.resource.file;

import java.io.File;
import java.net.URI;

import net.cheney.motown.dav.Lock;
import net.cheney.motown.dav.LockManager;
import net.cheney.motown.dav.Lock.Scope;
import net.cheney.motown.dav.Lock.Type;
import net.cheney.motown.dav.resource.api.DavResource;
import net.cheney.motown.dav.resource.api.DavResourceProvidor;
import net.cheney.motown.webservice.controller.Resource;
import net.cheney.uri.Path;

public class FileResourceProvidor implements DavResourceProvidor {

	private final File root;
	private final LockManager lockManager;

	public FileResourceProvidor(String root) {
		this(new File(root));
	}
	
	public FileResourceProvidor(File root) {
		this.root = root;
		this.lockManager = new FileResourceLockManager();
	}
	
	public final FileResource resolveResource(Path path) {
		return new FileResource(this, new File(root, path.toString()));
	}
	
	public final LockManager lockManager() {
		return lockManager;
	}
	
	private class FileResourceLockManager implements LockManager {
		
		@Override
		public boolean isLocked(DavResource resource) {
			return false;
		}

		@Override
		public Lock lock(DavResource resource, Type type, Scope scope) {
			return new Lock(type, scope, resource);
		}

		@Override
		public Lock unlock(DavResource resource) {
			return new Lock(Type.NONE, Scope.NONE, resource);
		}
		
	}

	public <V extends Resource> URI relativizeResource(V resource) {
		return root.toURI().relativize(((FileResource) resource).file().toURI());
	}

}
