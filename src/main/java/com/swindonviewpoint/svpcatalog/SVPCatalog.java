package com.swindonviewpoint.svpcatalog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.csvreader.*;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import java.net.*;
import java.io.*;

public class SVPCatalog {

	public static int A4width = 3250;
	public static int A4height = 2480;
	public static int cols = 4;
	public static int rows = 3;
	
	public static final String catalogCSVUrl = "http://www.swindonviewpoint.com/all_video.csv";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		List<Entry> catalog = SVPCatalog.generateCatalog(args);
		outputCatalog(catalog, args);
	}
	
	public static List<Entry> generateCatalog(String args[]) throws IOException {
		if (args.length < 3) {
			System.out.println("Usage : SVPCatalog <workingPath> <startId> <itemCount>");
		}
		
		int count = 0;
		String tempFolder = args[0];

		int catalogStartId = Integer.parseInt(args[1]);
		int catalogItemCount = Integer.parseInt(args[2]);
		List<Entry> catalog = new ArrayList<Entry>();
		URL catalogURL = new URL("http://www.swindonviewpoint.com/catalog.csv");
		URLConnection connection = catalogURL.openConnection();
        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

		System.out.println("Parsing CSV file: " + catalogURL);
		try {
			CsvReader reader = new CsvReader(bufferedReader);
				
			reader.readHeaders();
		
			for (int i = 0; i < catalogStartId; i++) {
				reader.readRecord();
			}
			while (reader.readRecord() && count < catalogItemCount){
				Entry entry = new Entry(count++,
										Integer.parseInt(reader.get("Nid")),
										reader.get("Duration"),
										reader.get("Title"),
										reader.get("Description"),
										reader.get("Large Thumbnail"),
										reader.get("Produced"),
										reader.get("Path"));
				catalog.add(entry);
			}
			System.out.println("Read "+catalog.size() + " catalog entries");
			
			for (int j = 0; j < catalog.size() ; j++){
				Entry entry = catalog.get(j);
				saveImage(entry.getThumbnailUrl(), tempFolder+entry.getThumbnailFilename());
				File qr = new File(tempFolder+entry.getQRFilename());
				
				if (!qr.exists()){
					BufferedImage im = generateQrImage(entry.getPath(), 150);
					saveImage(im, tempFolder+entry.getQRFilename());
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return catalog;
	}
		
	public static void outputCatalog(List<Entry> catalog, String args[]) {
		//render pages
		int catalogStartId = Integer.parseInt(args[1]);
		int catalogItemCount = Integer.parseInt(args[2]);
		int end = catalogStartId+catalogItemCount;
		String tempFolder = args[0];

		try {
			ClassLoader classLoader = SVPCatalog.class.getClassLoader();
			File svpBoldFontFile = new File(classLoader.getResource("svp-bold.ttf").getFile());
			InputStream is = new FileInputStream(svpBoldFontFile);
		    Font font = Font.createFont(Font.TRUETYPE_FONT, is);
		    Font subFont = font.deriveFont(22.0f);
		    Font titleFont = font.deriveFont(30.0f);
		    
		    File svpFontFile = new File(classLoader.getResource("svp.ttf").getFile());
		    is = new FileInputStream(svpFontFile);
		    Font medi = Font.createFont(Font.TRUETYPE_FONT, is);
		    Font medi18 = medi.deriveFont(20.0f);
		    
			Color orange = new Color(249, 111, 18);
			
			final int textAreaWidth = 625;
			final int border = 15;
			final int imageWidth = (SVPCatalog.A4width-(border*2))/SVPCatalog.cols;
			final int imageHeight = (SVPCatalog.A4height-(border*2))/SVPCatalog.rows;
			final int numRows = 3;
			final int numCols = 4;
			boolean leftOrRight = false;
			int pageNumberOffset = 6; //number of manually created pages before catalog
			int effectivePageNum;
			
			int numPages = (int)(catalog.size()/(numRows * numCols));
			if (catalog.size() > (numRows * numCols) * numPages){
				numPages++;
			}
			
			System.out.println("Generating "+numPages+ " pages");
			System.out.println("Using window size : "+imageWidth+ " x " + imageHeight);
			
			//process static pages
			for (int page = 1; page < pageNumberOffset; page++) {
				System.out.println("generating static page "+ page);	
				File staticPageFile = new File(classLoader.getResource("static-page.jpeg").getFile());
				System.out.println("processing "+ staticPageFile);
				BufferedImage pageImage = ImageIO.read(staticPageFile);
				Graphics2D staticGraphics = pageImage.createGraphics();
				SVPCatalog.drawPageNumber(page, staticGraphics, args, leftOrRight, font);
				File outputFile = new File(args[0]+File.separator+"page"+page+".jpeg");
				
				//set jpeg quality
				Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
				ImageWriter writer = iter.next();
				ImageWriteParam iwp = writer.getDefaultWriteParam();
				iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				iwp.setCompressionQuality(1); 
				
				FileImageOutputStream output = new FileImageOutputStream(outputFile);
				writer.setOutput(output);
				IIOImage image = new IIOImage(pageImage, null, null);
				writer.write(null, image, iwp);
				writer.dispose();
				
				leftOrRight = !leftOrRight;
			}
			
			
			for (int page = catalogStartId; page >= 0 && page <= end && page <= numPages; page++) {
				effectivePageNum = page+pageNumberOffset;
				if (leftOrRight) {
					System.out.println("generating dynamic page "+ effectivePageNum + " (left page num)");	
				} else {
					System.out.println("generating dynamic page "+ effectivePageNum + " (right page num)");
				}
				
				BufferedImage pageImage = new BufferedImage(SVPCatalog.A4width,
														SVPCatalog.A4height,
														BufferedImage.TYPE_INT_RGB);
				Graphics2D pageg2 = pageImage.createGraphics();
				for (int k = 0; k < numRows; k++ ){ //rows
					for(int l = 0; l < numCols; l++){ //cols
						int imageId = ((page * numRows * numCols) + (k*numCols))+l;
						Entry entry;
						if (imageId < catalogItemCount){
							
							entry = catalog.get(imageId);
							
							BufferedImage thumbnail = null;
							BufferedImage qr = null;
							try {
							    thumbnail = ImageIO.read(new File(tempFolder+entry.getThumbnailFilename()));
							    System.out.println("imageId is "+imageId+ ", catalogItemCount"+ catalogItemCount);
							    System.out.println("file is " + tempFolder+entry.getQRFilename());
							    qr = ImageIO.read(new File(tempFolder+entry.getQRFilename()));
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							
							BufferedImage image = new BufferedImage(imageWidth,
																	imageHeight,
																	BufferedImage.TYPE_INT_RGB);
							
							Graphics2D g2 = image.createGraphics();
							g2.setRenderingHint(
							        RenderingHints.KEY_TEXT_ANTIALIASING,
							        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
							g2.setColor(orange);
							
							float scaleFactor = 1.92f;
							float aspectRatio = (float)(thumbnail.getWidth())/(float)(thumbnail.getHeight());
							
							int thumbWidth;
							int thumbHeight;
							
							//draw thumbnail
							if (aspectRatio > 1.5) {
								scaleFactor = 1.92f;
								thumbWidth = (int)(thumbnail.getWidth() * scaleFactor);
								thumbHeight = (int)(thumbnail.getHeight() * scaleFactor);
								System.out.println("	- Processing 16:9 entry "+ imageId + ", ("+thumbWidth+"x"+thumbHeight+"), aspectRatio="+aspectRatio + ", "+entry.getTitle());
								
								//16:9
								g2.drawImage(thumbnail, -210,
													150,
													thumbWidth,
													thumbHeight,
													null);
							} else {
								scaleFactor = 1.6f;
								thumbWidth = (int)(thumbnail.getWidth() * scaleFactor);
								thumbHeight = (int)(thumbnail.getHeight() * scaleFactor);
								System.out.println("	- Processing 4:3 entry "+ imageId + ", ("+thumbWidth+"x"+thumbHeight+"), aspectRatio="+aspectRatio + ", " + entry.getTitle());
								
								//4:3
								g2.drawImage(thumbnail, -109,
										150,
										thumbWidth,
										thumbHeight,
										null);
							}
							
							
							//draw title
							String title = entry.getTitle();
							boolean trimmed = false;
							
							//trim title while too long
							g2.setFont(titleFont);
							while (g2.getFontMetrics().stringWidth(title) > textAreaWidth){
								title = title.substring(0, title.length()-1);
								trimmed = true;
							}
							if (trimmed) { //also trim back to a space if it was too long
								while (title.charAt(title.length()-1) != ' '){
									title = title.substring(0, title.length() - 1);
								}
							}
							
							g2.drawString(title, 20, 45);
							
							
							//draw produced date and duration
							g2.setFont(subFont);
							g2.drawString(entry.getProducedDate() + "  ( "+entry.getDuration()+" )", 20, 75);
							
							
							//draw description
							trimmed = false;
							g2.setColor(Color.white);
							g2.setFont(medi18);
							String description = entry.getDescription();
							String currentLine = new String(description);
							
							//if line is too long
							while (g2.getFontMetrics().stringWidth(currentLine) > textAreaWidth){
								currentLine = currentLine.substring(0, currentLine.length()-1);
								trimmed = true;
							}
							if (trimmed) { //clean up back to white space
								while (currentLine.charAt(currentLine.length()-1) != ' '){
									currentLine = currentLine.substring(0, currentLine.length() - 1);
								}
							}
							g2.drawString(currentLine, 20, 100);
							
							//process second description line
							trimmed = false;
							if (currentLine.length() < description.length()) {
								currentLine = description.substring(description.indexOf(currentLine)+currentLine.length(),
										description.length());
								
								//trim back if line too long
								while (g2.getFontMetrics().stringWidth(currentLine) > textAreaWidth){
									currentLine = currentLine.substring(0, currentLine.length()-1);
									trimmed = true;
								}
								if (trimmed) { //clean up back to last white space
									if (currentLine.length() > 0) {
										if (currentLine.indexOf(' ') == -1) {
											currentLine = "";
										} else {
											while (currentLine.charAt(currentLine.length()-1) != ' '){
										
												currentLine = currentLine.substring(0, currentLine.length() - 1);
											}
										}
									}
								}
								
								g2.drawString(currentLine, 20, 125);	
							}
							
							
							//draw qr code
							g2.drawImage(qr, imageWidth-qr.getWidth(),
											 0,
											 null);
							
							pageg2.drawImage(image, (l * imageWidth)+border, (k * imageHeight)+border, null);
						}
					}
				}
				
				
				//draw page numbers
				SVPCatalog.drawPageNumber(effectivePageNum, pageg2, args, leftOrRight, font);
				leftOrRight = !leftOrRight;
				
				File outputFile = new File(args[0]+File.separator+"page"+effectivePageNum+".jpeg");
	
				//set jpeg quality
				Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
				ImageWriter writer = iter.next();
				ImageWriteParam iwp = writer.getDefaultWriteParam();
				iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				iwp.setCompressionQuality(1); 
				
				FileImageOutputStream output = new FileImageOutputStream(outputFile);
				writer.setOutput(output);
				IIOImage image = new IIOImage(pageImage, null, null);
				writer.write(null, image, iwp);
				writer.dispose();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void drawPageNumber(int pageNumber, Graphics2D graphics, String[] args,
										boolean leftOrRight, Font font){
		BufferedImage logo;
		int fontX = 30;
	    int fontY = 72;
	    int pageNumX;
	    int pageNumY = 2290;
	    
	    Font pageNumFont = font.deriveFont(40.0f);
	    
	    try {
			ClassLoader classLoader = SVPCatalog.class.getClassLoader();
			File logoFile = new File(classLoader.getResource("logo.png").getFile());
			logo = ImageIO.read(logoFile);
			
			if (leftOrRight) {
				pageNumX = 60;
			} else {
				pageNumX = 3060;
			}
			
			graphics.drawImage(logo, pageNumX, pageNumY, 120, 120, null);
			leftOrRight = !leftOrRight;
			graphics.setFont(pageNumFont);
			graphics.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
			
			if (pageNumber < 10) {
				fontX = 42;
			} else {
				fontX = 26;
			}
			graphics.drawString(Integer.toString(pageNumber), pageNumX+fontX, pageNumY+fontY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		
	}
	public static String getQRUrl(String url){
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		System.out.println("getting url:" + url);
		
		try {
			client.executeMethod(method);
			String body = method.getResponseBodyAsString();
			System.out.println("--URL--\n"+url);
			System.out.println(body);
			int index = body.indexOf("QRCode");
			index = body.indexOf("src=", index);
			int startIndex = index + 5;
			int endIndex = body.indexOf(".png", startIndex)+4;
			method.releaseConnection();
			method = null;
			client = null;
			
			
			return "http://www.swindonviewpoint.com"+body.substring(startIndex, endIndex);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		return "";
	}
	
	
	public static void saveImage(String url, String file) {

		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		
		File image = new File(file);
		
			if (!image.exists()) {
		    try {
		        client.executeMethod(method);
		        byte[] responseBody = method.getResponseBody();
		        
		        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		        bos.write(responseBody);
		        bos.flush();
		        bos.close();
		        
		        System.out.println("Saved url:"+url+ " to " + file);
		        
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			} else {
				System.out.println("Did not save url:" + url + ", Exists file:"+ file);
			}
			
		
	}

	
	public static void saveImage(BufferedImage image, String file) {
		
		File imageFile = new File(file);
		
		try {
	        if (imageFile.exists()) {
	        	//do nothing
	        } else {
	        	ImageIO.write(image, "png", imageFile);
	        }
	        
	        System.out.println("Saved qr image to:"+file);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }	
		
	}

	
	public static BufferedImage generateQrImage(String dataToEncode, int size){
		Charset charset = Charset.forName("UTF-8");
		
	    CharsetEncoder encoder = charset.newEncoder();
	    byte[] b = new byte[0];
	    BufferedImage image = null;
	    
	    try {
	        // Convert a string to UTF-8 bytes in a ByteBuffer
	        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(dataToEncode));
	        b = bbuf.array();
	    } catch (CharacterCodingException CCe) {
	        CCe.printStackTrace();
	    }
	
	    String data;
	    try {
	        data = new String(b, "UTF-8");
	        // get a byte matrix for the data
	        BitMatrix matrix = null;
	        com.google.zxing.Writer writer = new MultiFormatWriter();
	        try {
	            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
	            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	            
	            matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, size, size, hints);
	        } catch (com.google.zxing.WriterException e) {
	            System.out.println(e.getMessage());
	        }
	        
	        image = MatrixToImageWriter.toBufferedImage(matrix);
	    
	        
	    } catch (UnsupportedEncodingException UEe) {
	        UEe.printStackTrace();
		}
	    
	    return image;
	}
}