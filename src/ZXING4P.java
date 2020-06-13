/*************************************************************************************
 *
 *	ZXing4P3: barcode library for Processing v2.x/3.x [v3.3, 04/14/2018]
 *
 *	http://cagewebdev.com/zxing4processing-processing-library/
 *
 *************************************************************************************/
package com.cage.zxing4p3;

// PROCESSING CORE CLASSES
import processing.core.PVector;
import processing.core.PImage;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/BarcodeFormat.html
import com.google.zxing.BarcodeFormat;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/DecodeHintType.html
import com.google.zxing.DecodeHintType;

import com.google.zxing.ReaderException;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/Result.html
import com.google.zxing.Result;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/ResultPoint.html
import com.google.zxing.ResultPoint;

import com.google.zxing.common.*;
import com.google.zxing.qrcode.*;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/qrcode/QRCodeReader.html
import com.google.zxing.qrcode.QRCodeReader;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/qrcode/decoder/Decoder.html
import com.google.zxing.qrcode.decoder.*;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/multi/qrcode/QRCodeMultiReader.html
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/qrcode/encoder/Encoder.html
import com.google.zxing.qrcode.encoder.*;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.LuminanceSource;

// NATIVE JAVA
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.util.Hashtable;
import java.util.Vector;
import java.util.ArrayList;


/*************************************************************************************
 *
 *	This library was created to integrate the open source <a href="http://code.google.com/p/zxing/" target="_blank">ZXING barcode library</a> with
 *	Processing.<br><br>
 *
 *	By Rolf van Gelder ::
 *	<a href="http://rvg.cage.nl/" target="_blank">http://rvg.cage.nl/</a> ::
 *	<a href="http://cagewebdev.com/" target="_blank">http://cagewebdev.com/</a> ::
 *	<a href="mailto:info@cagewebdev.com">info@cagewebdev.com</a><br><br>
 *
 *	Library page:
 *	<a href="http://cagewebdev.com/zxing4processing-processing-library/" target="_blank">http://cagewebdev.com/zxing4processing-processing-library/</a><br><br>
 *
 *	v3.3 :: 04/14/2018<br>
 *	- Added Multi QRCode support<br>
 *	- New mothod: decodeMultipleQRCodes()<br>
 *	- New method: getPositionMarkers(i)<br>
 *<br>
 *	v3.2 :: 07/31/2016<br>
 *	- Removed deprecated method: 'decodeWeb()'<br>
 *	- Added a new method: 'version()'<br>
 *	- Several minor changes<br><br>
 *<br> 
 *	v3.1 :: 07/21/2016<br>
 *	- Renamed the library to zxing4p3, a Processing 2.x/3.x compatible version<br>
 *	- New method: getPositionMarkers()<br><br>
 *
 *	NOTE: Compiled with Java jdk1.6.0_45
 *
 *************************************************************************************/
 
public class ZXING4P {
	/*********************************************************************************
	 *
	 *	Properties
	 *
	 *********************************************************************************/
	String thisVersion     = "3.3";
	String thisReleaseDate = "04/14/2016";
	
	Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(3);;
	
	// RESULT OBJECT
	Result theResult;
	ResultPoint[] resultPoints;
	
	// RESULT STRING
	String resString = "";
	Vector<String> resVector = new Vector<String>();
	
	// POSITION MARKERS FOR THE QRCode
	PVector[] positionMarkers;
	
	// ARRAY LIST WITH THE POSITION MARKERS FOR MULTIPLE QR CODES
	ArrayList<PVector[]> alMarkers = new ArrayList<PVector[]>();

	
	/*********************************************************************************
	 *
	 *	Constructor
	 *
	 *********************************************************************************/
	public ZXING4P() {
	} // ZXING4P()


	/*********************************************************************************
	 *
	 *	Decode the QRCode from a PImage
	 *
	 *	@param tryHarder	if set to true, it tells the software to spend a little
	 *						more time trying to decode the image
	 *	@param img			PImage containing the image to be examinded
	 *	@return				String with the found QRCode (empty if nothing found)
	 *
	 *********************************************************************************/
	public String decodeImage(boolean tryHarder, PImage img) {
		
		BufferedImage bufferedImage = new BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_ARGB);
		
		resString = "";

		for (int x = 0; x < img.width; x++)
			for (int y = 0; y < img.height; y++) bufferedImage.setRGB(x,y,img.get(x,y));

		
		if (tryHarder) hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

		try {			
			LuminanceSource lumiancesource = new BufferedImageLuminanceSource(bufferedImage);
			BinaryBitmap monoImg = new BinaryBitmap(new GlobalHistogramBinarizer(lumiancesource));
			
			theResult = new QRCodeReader().decode(monoImg, hints);
			
			// GET THE DECODED TEXT
			resString = theResult.getText();
			
			// PREVENT THE 'GLYPH NOT FOUND' MESSAGE
			resString = resString.replace("\r\n", "\n");

			// UPDATE THE MARKERS			
			resultPoints = theResult.getResultPoints();
			positionMarkers = new PVector[resultPoints.length];
			for(int i=0; i<resultPoints.length; i++)
				positionMarkers[i] = new PVector (resultPoints[i].getX(), resultPoints[i].getY());
			
		} catch (Exception e) {
			// No QRCode found...
			// System.out.println("Error reading QRCode image (PImage decodeImage) " + e);
			return "";
		}
	
		return resString;
	} // decodeImage()
	
	
	/*********************************************************************************
	 *
	 *	Decode the QRCode(s) from a PImage
	 *
	 *	@param tryHarder	if set to true, it tells the software to spend a little
	 *						more time trying to decode the image
	 *	@param img			PImage containing the image to be examinded
	 *	@return				String array with the found QRCode(s)
	 *						(null if nothing found)
	 *
	 *********************************************************************************/
	public String[] decodeMultipleQRCodes(boolean tryHarder, PImage img)
	{
		BufferedImage bufferedImage = new BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_ARGB);
		
		String[] resArr  = null;
		Result[] results = null;
		ArrayList<String> resList;

		for (int x = 0; x < img.width; x++)
			for (int y = 0; y < img.height; y++) bufferedImage.setRGB(x, y, img.get(x,y));

		if (tryHarder) hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

		try {
			// LOAD THE IMAGE
			LuminanceSource lumiancesource = new BufferedImageLuminanceSource(bufferedImage);
			BinaryBitmap monoImg = new BinaryBitmap(new GlobalHistogramBinarizer(lumiancesource));
			
			// SCAN FOR QR CODES
			results = new QRCodeMultiReader().decodeMultiple(monoImg, hints);
			
			// NO QR CODES FOUND
			if(results.length < 1) {
				System.out.println("No QR codes found");
				return null;
			} // if(results.length < 1)
				
			resList = new ArrayList<String>();

			for (int i = 0; i < results.length; i++) {
				String s = results[i].getText();
				// PREVENT THE 'GLYPH NOT FOUND' MESSAGE
				s = s.replace("\r\n", "\n");
				resList.add(s);
				
				// UPDATE THE MARKERS			
				resultPoints = results[i].getResultPoints();
				positionMarkers = new PVector[resultPoints.length];
				
				for(int r = 0; r < resultPoints.length; r++)
					positionMarkers[r] = new PVector (resultPoints[r].getX(), resultPoints[r].getY());
				
				alMarkers.add(positionMarkers);
			} // for (int i = 0; i < results.length; i++)

			// ARRAYLIST TO SIMPLE ARRAY
			resArr = new String[resList.size()];
			resList.toArray(resArr);
		} catch (Exception e) {
			// No QRCode found...
			// System.out.println("Error reading QRCode image (PImage decodeImage) " + e);
			return null;
		}
	
		return resArr;
	} // decodeMultipleQRCodes()	
		
	
	/*********************************************************************************
	 *
	 *	Generates a QRCode PImage from a string
	 *
	 *	@param content	string to encode
	 *	@param width	width of the PImage that will be returned
	 *	@param height	height of the PImage that will be returned
	 *	@return			PImage with the QRCode image
	 *
	 *********************************************************************************/
	public PImage generateQRCode(String content, int width, int height) {
		PImage pImage = new PImage(width, height);

		QRCodeWriter encoder = new QRCodeWriter();

		try {
			BitMatrix bitMatrix = encoder.encode(content, BarcodeFormat.QR_CODE, width, height);
			
			// COPY THE BYTEMATRIX TO THE PIMAGE
			for(int i=0; i<width; i++)
				for(int j=0; j<height; j++) {
					int colorValue = 0; // BLACK
					if(!bitMatrix.get(j, i)) colorValue = 16777215;	// WHITE
					// ADD THE PIXEL TO THE IMAGE
					pImage.set(i, j, colorValue);
				} // for(int j=0; j<height; j++)
		} catch ( Exception e ) {
			System.out.println("Error generating QRCode image (PImage generateQRCode) " + e);
		} // try
		
		return pImage;
	} // PImage generateQRCode()
	
	
	/*********************************************************************************
	 *
	 *	Returns a PVector array with the position markers for the latest detected QRCode
	 *
	 *	@return	PVector with the position markers for the latest detected QRCode
	 *
	 *********************************************************************************/
	public PVector[] getPositionMarkers() {
		return positionMarkers;
	} // getPositionMarkers()


	/*********************************************************************************
	 *
	 *	Returns a PVector array with the position markers for a specific QRCode
	 *
	 *	@param index	index of the detected QRCode
	 *
	 *	@return	PVector with the position markers for the detected QRCode
	 *
	 *********************************************************************************/
	public PVector[] getPositionMarkers(int index) {
		return alMarkers.get(index);
	} // getPositionMarkers()

	
	/*********************************************************************************
	 *
	 *	Displays version information of this library in the console
	 *
	 *********************************************************************************/
	public void version() {
		System.out.println("Zxing4processing v"+thisVersion+" ("+thisReleaseDate+"), by Rolf van Gelder (cagewebdev.com)\n");
	} // void version()	
} // ZXING4P
