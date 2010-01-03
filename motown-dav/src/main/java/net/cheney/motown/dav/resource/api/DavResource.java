package net.cheney.motown.dav.resource.api;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.cheney.motown.dav.Lock;
import net.cheney.motown.dav.Lock.Scope;
import net.cheney.motown.dav.Lock.Type;
import net.cheney.motown.webservice.controller.Resource;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.QName;

public interface DavResource extends Resource {

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

	void copyTo(DavResource destination) throws IOException;

	void moveTo(DavResource destination) throws IOException;
	
	Collection<ComplianceClass> davOptions();

	void unlock();
	
	Lock lock(Type type, Scope scope);

	Element getProperty(QName property);
	
	List<DavResource> members();
}
