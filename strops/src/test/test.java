package test;

import static org.junit.Assert.*;

import org.junit.Test;

import string_operations.StrOps;

public class test {

	@Test
	public void test() {
		assertEquals("get_substring_between_patterns(Hello World, He, ld) = llo Wor", "llo Wor", StrOps.get_substring_between_patterns("Hello World", "He", "ld"));
		assertEquals("get_substring_between_patterns(Hello World, sd, ld) = ", "", StrOps.get_substring_between_patterns("Hello World", "sd", "ld"));
		assertEquals("get_substring_between_patterns(Hello World, He, aa) = ", "", StrOps.get_substring_between_patterns("Hello World", "He", "aa"));
		
		assertEquals("trimString( ) = ", "", StrOps.trimString(" "));
	}

}
