package com.exist.util2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringGeneratorUtilTest {
//1
	@Test
	public void testGetStringLengthEqualsThree() {
		String actual = StringGeneratorUtil.getString(3);
		assertEquals(3, actual.length());
	}
//2
	@Test
	public void testGetStringIsNotNull() {
		assertNotNull(StringGeneratorUtil.getString(3));
	}
//3
	@Test
	public void testGetStringIsEmptyIsTrue() {
		assertTrue(StringGeneratorUtil.getString(0).isEmpty());
	}
//4
	@Test
	public void testGetStringAAA() {
		RandomIntGenerator mockRandomIntGenerator = mock(RandomIntGenerator.class);
		when(mockRandomIntGenerator.getRandomInt()).thenReturn(0.519685);
		StringGeneratorUtil.setRandomIntGenerator(mockRandomIntGenerator);
		assertEquals("AAA", StringGeneratorUtil.getString(3));
	}
//5
	@Test
	public void testGetStringNotEqualsAAA() {
		RandomIntGenerator mockRandomIntGenerator = mock(RandomIntGenerator.class);
		when(mockRandomIntGenerator.getRandomInt()).thenReturn(0.511811);
		StringGeneratorUtil.setRandomIntGenerator(mockRandomIntGenerator);
		assertNotEquals("AAA", StringGeneratorUtil.getString(3));
	}
	
}
