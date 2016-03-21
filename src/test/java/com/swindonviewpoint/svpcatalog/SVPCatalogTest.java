package com.swindonviewpoint.svpcatalog;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.List;
import java.awt.image.BufferedImage;
public class SVPCatalogTest {

	private List<Entry> catalog;
	private String arg0;
	private String arg1;
	private String arg2;
	private String[] args;
	private String testURL = "http://www.swindonviewpoint.com/";

	@Before
    public void setUp() throws IOException {
        System.out.println("@Before - setUp");
		
		System.out.println("os="+System.getProperty("os.name"));

		if (System.getProperty("os.name").indexOf("Linux") >= 0) { 
			arg0 = File.separator+"tmp"+File.separator;
		} else {
			arg0 = "C:"+File.separator+"temp"+File.separator;
		}


        arg1 = "0";
        arg2 = "24";

        args = new String[3];
		args[0] = arg0;
		args[1] = arg1;
		args[2] = arg2;


        catalog = SVPCatalog.generateCatalog(args);
    }

	
	@Test
	public void testGenerateCatalog(){
		File tempFolder = new File(arg0 + File.separator);
		assertTrue(catalog.size() == Integer.parseInt(arg2));
		assertTrue(tempFolder.listFiles().length > 0);
	}

	@Test
	public void testOutputCatalog(){
		SVPCatalog.outputCatalog(catalog, args);
		File outputFolder = new File(arg0 + File.separator);
		assertTrue(outputFolder.listFiles().length > 0);
	}

	/*
	@Test
	public void testSaveThumbnails(){
		
	}
	*/

	@Test
	public void testGenerateQrImage(){
		int size = 150;

		
		BufferedImage qr = SVPCatalog.generateQrImage(testURL, size);
		assertTrue(qr.getWidth() == size);
		assertTrue(qr.getHeight() == size);
	}

	/*
	@Test
	public void testSaveImageUrl(){
		
	}

	@Test
	public void testSaveImageBufferedImage(){
		
	}

*/

	@Test
	public void testGetQRUrl(){
		String qRUrl = SVPCatalog.getQRUrl(testURL);
		assertTrue(qRUrl != null);
	}

/*
	@Test
	public void testdrawPageNumber(){
		
	}
	*/
	
}