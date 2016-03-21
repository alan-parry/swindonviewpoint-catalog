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
	private int qrSize = 150;

		
	@Before
    public void setUp() throws IOException {
        System.out.println("@Before - setUp");
		
		System.out.println("os="+System.getProperty("os.name"));

		if (System.getProperty("os.name").indexOf("Linux") >= 0) { 
			arg0 = File.separator+"tmp"+File.separator;
		} else {
			arg0 = "C:"+File.separator+"temp"+File.separator;
		}

		File tempFolder = new File(arg0);
		File[] files = tempFolder.listFiles();
		if(files!=null) {
	        for(File f: files) {
	            if(!f.isDirectory()) {
	                f.delete();
	            }
	        }
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
	public void testMain() throws IOException {
        SVPCatalog.main(args);
    }

	@Test
	public void testGenerateCatalog(){
		File tempFolder = new File(arg0 + File.separator);
		assertTrue(catalog.size() == Integer.parseInt(arg2));
		assertTrue(tempFolder.listFiles().length > 0);
	}

	@Test
	public void testGenerateCatalogBadArguments() throws IOException {
		args = new String[0];
		catalog = SVPCatalog.generateCatalog(args);
		assertNull(catalog);
	}

	@Test
	public void testOutputCatalog(){
		SVPCatalog.outputCatalog(catalog, args);
		File outputFolder = new File(arg0 + File.separator);
		assertTrue(outputFolder.listFiles().length > 0);
	}

	@Test
	public void testGenerateQrImage(){
		BufferedImage qr = SVPCatalog.generateQrImage(testURL, qrSize);
		assertTrue(qr.getWidth() == qrSize);
		assertTrue(qr.getHeight() == qrSize);
	}

	@Test
	public void testSaveImage(){
		String filename = arg0+"testQr.jpg";
		File testFile = new File(filename);
		BufferedImage qr = SVPCatalog.generateQrImage(testURL, qrSize);
		SVPCatalog.saveImage(qr, filename);
		assertTrue(testFile.exists());
	}
	
	@Test
	public void testSaveImageByUrl(){
		String filename = arg0+"testQrByUrl.jpg";
		File testFile = new File(filename);
		SVPCatalog.saveImage(testURL, filename);
		assertTrue(testFile.exists());
	}

	@Test
	public void testGetQRUrl(){
		String qRUrl = SVPCatalog.getQRUrl(testURL);
		assertTrue(qRUrl != null);
	}

	// To ensure coverage
	@Test
	public void testGetQRUrlBadUrl(){
		String qRUrl = SVPCatalog.getQRUrl(testBadURL);
		asertNull(qRUrl);
	}

}