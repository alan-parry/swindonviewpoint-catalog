package com.swindonviewpoint.svpcatalog;

import org.junit.*;
import static org.junit.Assert.*;

public class EntryTest {

	private static final int integer = 1;
	private static final String string = "string";
	private static final String qrPathString = "string" + File.separator + "temp" + File.separator + integer + Entry.thumbnailFilename;
	private static final String thumbnailPathString = "string" + File.separator + "temp" + File.separator + integer + Entry.qrFilename;

	private static final int setInteger = 2;
	private static final String setString = "setString";

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
	public void testSetPath(){
		entry.setPath(setString);
		assertEquals(setString, entry.getPath());
	}

	@Test
	public void testGetTitle(){
		assertEquals(string, entry.getTitle());
	}

	@Test
	public void testSetTitle(){
		entry.setTitle(setString);
		assertEquals(setString, entry.getTitle());
	}

	@Test
	public void testGetDescription(){
		assertEquals(string, entry.getDescription());
	}

	@Test
	public void testSetDescription(){
		entry.setDescription(setString);
		assertEquals(setString, entry.getDescription());
	}

	@Test
	public void testGetProducedDate(){
		assertEquals(string, entry.getProducedDate());
	}

	@Test
	public void testSetProducedDate(){
		entry.setProducedDate(setString);
		assertEquals(setString, entry.getProducedDate());
	}

	@Test
	public void testGetThumbnailUrl(){
		assertEquals(string, entry.getThumbnailUrl());
	}

	@Test
	public void testSetThumbnailUrl(){
		entry.setThumbnailUrl(setString);
		assertEquals(setString, entry.getThumbnailUrl());
	}

	@Test
	public void testGetDuration(){
		assertEquals(string, entry.getDuration());
	}

	@Test
	public void testSetDuration(){
		entry.setDuration(setString);
		assertEquals(setString, entry.getDuration());
	}

	@Test
	public void testGetId(){
		assertEquals(integer, entry.getId());
	}

	@Test
	public void testSetId(){
		entry.setId(setInteger);
		assertEquals(setInteger, entry.getId());
	}

	@Test
	public void testGetNid(){
		assertEquals(integer, entry.getNid());
	}

	@Test
	public void testSetNid(){
		entry.setNid(setInteger);
		assertEquals(setInteger, entry.getNid());
	}

	@Test
	public void testGetBaseDir(){
		assertEquals(string, entry.getBaseDir());
	}

	@Test
	public void testSetBaseDir(){
		entry.setBaseDir(setString);
		assertEquals(setString, entry.getBaseDir());
	}

	@Test
	public void testGetQRPath(){
		assertEquals(string, entry.getQRPath());
	}


	@Test
	public void testGetThumbnailPath(){
		assertEquals(string, entry.getThumbnailPath());
	}

}