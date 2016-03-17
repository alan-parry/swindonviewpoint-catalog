package com.swindonviewpoint.svpcatalog;

import org.junit.*;
import static org.junit.Assert.*;

public class EntryTest {

	private static final int integer = 1;
	private static final String string = "string";

	private Entry entry;

	@Before
    public void setUp() {
        entry = new Entry(integer, integer, string, string, string, string, string, string, string);
        System.out.println("@Before - setUp");
    }

	@Test
	public void testGetPath(){
		assertEquals(string, entry.getPath());
	}
}