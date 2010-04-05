package net.cheney.motown.common.api;

import java.util.Date;

import junit.framework.Assert;

import net.cheney.motown.common.api.HeaderMap.HeaderKey;

import org.junit.Test;


public class HeaderMapTest {

	public static final HeaderKey<String> COOKIE = new HeaderKey<String>() {
		@Override
		public Class<String> type() {
			return String.class;
		}
	};
	
	public static final HeaderKey<Date> DATE = new HeaderKey<Date>() {

		@Override
		public Class<Date> type() {
			return Date.class;
		}
		
	};	
	
	@Test public void testHeaderMap() {
		HeaderMap map = new HeaderMap();
		map.put(COOKIE, "foo=bar");
		map.put(DATE, new Date());
		
		Assert.assertEquals(map.get(COOKIE).getClass(), String.class);
		Assert.assertEquals(map.get(COOKIE).getClass(), COOKIE.type());
		
		Assert.assertEquals(map.get(DATE).getClass(), Date.class);
		Assert.assertEquals(map.get(DATE).getClass(), DATE.type());
	}
}
