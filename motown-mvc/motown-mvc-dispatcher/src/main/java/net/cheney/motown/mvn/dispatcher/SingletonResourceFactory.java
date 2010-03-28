package net.cheney.motown.mvn.dispatcher;


public class SingletonResourceFactory extends ResourceFactory {

	private final Object resource;

	public SingletonResourceFactory(Object resource) {
		this.resource = resource;
	}
	
	public final Object resource() {
		return resource;
	}

	public final Class<?> resourceClass() {
		return resource.getClass();
	}

}
