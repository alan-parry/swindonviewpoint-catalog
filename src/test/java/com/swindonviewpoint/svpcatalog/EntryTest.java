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

	@Test
	public void testGetTitle(){
		assertEquals(string, entry.getTitle());
	}

	@Test
	public void testGetDescription(){
		assertEquals(string, entry.getDescription());
	}

	@Test
	public void testGetProducedDate(){
		assertEquals(string, entry.getProducedDate());
	}

	@Test
	public void testGetThumbnailUrl(){
		assertEquals(string, entry.getThumbnailUrl());
	}

	@Test
	public void testGetId(){
		assertEquals(integer, entry.getId());
	}

	@Test
	public void testGetNid(){
		assertEquals(integer, entry.getNid());
	}
}