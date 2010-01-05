package net.cheney.motown.uri;

public abstract class URL extends URI {

	public enum Scheme implements URI.Scheme {
		FILE,
		HTTP,
		HTTPS,
		MAILTO;
		
		@Override
		public URL.Builder builder(URI.Scheme scheme) {
			return null;
		}
	}	
	
	@Override
	public final boolean isDereferenceable() {
		return true;
	}

	protected class Builder extends URI.Builder {

		public Builder(URI.Scheme scheme) {
			// TODO Auto-generated constructor stub
		}
		
	}
	
}
