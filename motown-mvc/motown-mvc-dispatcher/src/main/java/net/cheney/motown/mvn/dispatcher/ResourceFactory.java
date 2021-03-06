package net.cheney.motown.mvn.dispatcher;

public abstract class ResourceFactory {

	public abstract Class<?> resourceClass();
	
	public abstract Object resource();
	
	public static ResourceFactory factoryForResource(Object resource) {
		return new SingletonResourceFactory(resource);
	}

}
