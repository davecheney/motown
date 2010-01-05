package net.cheney.motown.uri;

public abstract class URN extends URI {

	public enum Scheme implements URI.Scheme {
		URN;

		@Override
		public Builder builder(net.cheney.motown.uri.URI.Scheme scheme) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	@Override
	public final boolean isDereferenceable() {
		return false;
	}

}
