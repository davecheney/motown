package net.cheney.motown.dav.resource.memory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.cheney.motown.dav.Lock;
import net.cheney.motown.dav.Lock.Scope;
import net.cheney.motown.dav.Lock.Type;
import net.cheney.motown.dav.resource.api.DavResource;
import net.cheney.motown.webservice.controller.Resource;
import net.cheney.motown.api.Method;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.QName;

public class MemoryDavResource implements DavResource {

	private ByteBuffer contents;
	private MemoryDavResource parent;
	
	public MemoryDavResource(MemoryDavResource parent) {
		this.parent = parent;
	}
	
	@Override
	public void copyTo(DavResource destination) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<ComplianceClass> davOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getProperty(QName property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Lock lock(Type type, Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DavResource> members() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mkcol() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void moveTo(DavResource destination) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unlock() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean delete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String displayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteBuffer entity() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String etag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCollection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Date lastModified() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource parent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(ByteBuffer body) throws IOException {
		this.contents = body;
	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<Method> supportedMethods() {
		// TODO Auto-generated method stub
		return null;
	}

}
