package net.cheney.motown.resource.api;

import static net.cheney.motown.resource.api.Elements.getContentLength;
import static net.cheney.motown.resource.api.Elements.getLastModified;
import static net.cheney.motown.resource.api.Elements.resourceType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.cheney.motown.common.api.Request.Method;
import net.cheney.motown.resource.api.Lock.Scope;
import net.cheney.motown.resource.api.Lock.Type;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.QName;

public abstract class Resource {
	
	public enum ComplianceClass {

		LEVEL_1("1"),
		LEVEL_2("2"),
		ACCESS_CONTROL("access-control");
		
		private final String name;
		
		private ComplianceClass(String name) {
			this.name = name;
		}
		 
		@Override
		public String toString() {
			return name;
		}
	}

	public abstract boolean mkcol();

	public abstract void copyTo(Resource destination) throws IOException;

	public abstract void moveTo(Resource destination) throws IOException;
	
	public abstract Collection<ComplianceClass> davOptions();

	public boolean isLocked() {
		return providor().lockManager().isLocked(this);
	}
	
	protected abstract ResourceProvidor providor();

	public void unlock() {
		providor().lockManager().unlock(this);
	}
	
	public Lock lock(final Type type, final Scope scope) {
		return providor().lockManager().lock(this, type, scope);
	}

	public Element getProperty(QName property) {
		if (property.equals(Property.GET_CONTENT_LENGTH)) {
			return getContentLength(size());
		}

		if (property.equals(Property.RESOURCE_TYPE)) {
			return resourceType(isCollection());
		}

		if (property.equals(Property.GET_LAST_MODIFIED)) {
			return getLastModified(lastModified());
		}

		 if (property.equals(Property.DISPLAY_NAME)) {
			 return Elements.displayName(displayName());
		 }
		 
		 return null;
	}
	
	public abstract boolean exists();
	
	public abstract Date lastModified();
	
	public abstract Collection<Method> supportedMethods();

	public abstract ByteBuffer entity() throws IOException;
	
	public abstract FileChannel channel() throws IOException;

	public abstract void put(ByteBuffer entity) throws IOException;
	
	public abstract String etag();
	
	public abstract long size();

	public abstract String displayName();

	public abstract boolean delete();

	public abstract Resource parent();
	
	public abstract boolean isCollection();

	public abstract List<Resource> members();

}