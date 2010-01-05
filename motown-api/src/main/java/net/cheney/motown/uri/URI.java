package net.cheney.motown.uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class URI {

	private static final Pattern URI_PATTERN = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
	
	public abstract boolean isDereferenceable();
	
	public interface Scheme {

		URI.Builder builder(Scheme scheme);
		
	}
	
	public static URI parse(String s) {
		Matcher matcher = URI_PATTERN.matcher(s);
		if(!matcher.matches()) {	
			throw new IllegalArgumentException(String.format("[%s] does not match [%s]", s, matcher));
		}
		for(int i = 0 ; i < 10 ; ++i ) {
			System.out.println(i + " [" + matcher.group(i)+"]");
		}
		return null;
	}
	
	public static URI.Builder builder(URI.Scheme scheme) {
		return scheme.builder(scheme);
	}
	
	protected abstract static class Builder {
		
	}
	
	public abstract URI.Scheme scheme();
			
	public abstract String user();
	
	public abstract String password();

	public abstract String host();
	
	public abstract int port();

	public abstract Path path();
	
	public abstract String query();
	
	public abstract String fragment();
}
