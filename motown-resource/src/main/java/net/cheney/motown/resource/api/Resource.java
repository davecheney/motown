package net.cheney.motown.resource.api;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.cheney.motown.api.Method;
import net.cheney.motown.resource.api.Lock.Scope;
import net.cheney.motown.resource.api.Lock.Type;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.QName;

public interface Resource {
	
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

	boolean mkcol();

	void copyTo(Resource destination) throws IOException;

	void moveTo(Resource destination) throws IOException;
	
	Collection<ComplianceClass> davOptions();

	void unlock();
	
	Lock lock(Type type, Scope scope);

	Element getProperty(QName property);
	
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

	List<Resource> members();

	boolean isLocked();
}
