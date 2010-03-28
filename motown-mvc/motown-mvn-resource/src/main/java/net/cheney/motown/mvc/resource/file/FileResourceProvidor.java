package net.cheney.motown.mvc.resource.file;

import java.io.File;
import java.net.URI;

import net.cheney.motown.mvc.resource.api.Lock;
import net.cheney.motown.mvc.resource.api.LockManager;
import net.cheney.motown.mvc.resource.api.Resource;
import net.cheney.motown.mvc.resource.api.ResourceProvidor;
import net.cheney.motown.mvc.resource.api.Lock.Scope;
import net.cheney.motown.mvc.resource.api.Lock.Type;
import net.cheney.motown.uri.Path;

public class FileResourceProvidor implements ResourceProvidor {

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
		public boolean isLocked(Resource resource) {
			return false;
		}

		@Override
		public Lock lock(Resource resource, Type type, Scope scope) {
			return new Lock(type, scope, resource);
		}

		@Override
		public Lock unlock(Resource resource) {
			return new Lock(Type.NONE, Scope.NONE, resource);
		}
		
	}

	public URI relativizeResource(Resource resource) {
		return root.toURI().relativize(((FileResource) resource).file().toURI());
	}

}
