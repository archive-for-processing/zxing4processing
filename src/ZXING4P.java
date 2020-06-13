/*************************************************************************************
 *
 *	ZXing4P3: barcode library for Processing v2.x/3.x [v3.2, 07/31/2016]
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

import com.google.zxing.MonochromeBitmapSource;

import com.google.zxing.ReaderException;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/Result.html
import com.google.zxing.Result;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/ResultPoint.html
import com.google.zxing.ResultPoint;

import com.google.zxing.client.j2se.BufferedImageMonochromeBitmapSource;
import com.google.zxing.common.*;
import com.google.zxing.qrcode.*;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/qrcode/QRCodeReader.html
import com.google.zxing.qrcode.QRCodeReader;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/qrcode/decoder/Decoder.html
import com.google.zxing.qrcode.decoder.*;

// https://zxing.github.io/zxing/apidocs/com/google/zxing/qrcode/encoder/Encoder.html
import com.google.zxing.qrcode.encoder.*;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.util.Hashtable;
import java.util.Vector;


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
 *	v3.2 :: 07/31/2016<br>
 *	- Removed deprecated method: 'decodeWeb()'<br>
 *	- Added a new method: 'version()'<br>
 *	- Several minor changes<br><br>
 * 
 *	v3.1 :: 07/21/2016<br>
 *	- Renamed the library to zxing4p3, a Processing 2.x/3.x compatible version<br>
 *	- New method: getPositionMarkers()<br><br>
 *
 *	NOTE: Compiled with Java 1.6.0_45 (because of Processing 2.x compatibility)
 *
 *************************************************************************************/
 
public class ZXING4P {
	/*********************************************************************************
	 *
	 *	Properties
	 *
	 *********************************************************************************/
	String thisVersion = "3.2";
	String thisReleaseDate = "07/31/2016";
	
	Hashtable<DecodeHintType, Object> hints;
	
	// RESULT OBJECT
	Result theResult;
	ResultPoint[] resultPoints;
	
	// RESULT STRING
	String resString = "";
	Vector<String> resVector = new Vector<String>();
	
	// POSITION MARKERS FOR THE QRCode
	PVector[] positionMarkers;
	
	/*********************************************************************************
	 *
	 *	Constructor
	 *
	 *********************************************************************************/
	public ZXING4P() {
		hints = new Hashtable<DecodeHintType, Object>(3);
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
	public String decodeImage(boolean tryHarder, PImage img)
	{
		Decoder decoder = new Decoder();
		
		BufferedImage source = new BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < img.width; x++)
			for (int y = 0; y < img.height; y++) source.setRGB(x,y,img.get(x,y));
	
		if (tryHarder) hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

		try
		{
			MonochromeBitmapSource monoImg = new BufferedImageMonochromeBitmapSource(source);
			theResult = new QRCodeReader().decode(monoImg, hints);
			
			resString = theResult.getText();
			
			// PREVENT THE 'GLYPH NOT FOUND' MESSAGE
			resString = resString.replace("\r\n", "\n");

			// UPDATE THE MARKERS			
			resultPoints = theResult.getResultPoints();
			positionMarkers = new PVector[resultPoints.length];
			for(int i=0; i<resultPoints.length; i++)
				positionMarkers[i] = new PVector (resultPoints[i].getX(), resultPoints[i].getY());
		} catch (ReaderException e)
		{
			// No QRCode found...
			return "";
		}
		
		return resString;
	} // String decodeImage()

	
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
	public PImage generateQRCode(String content, int width, int height)
	{
		PImage myPImage = new PImage(width,height);

		QRCodeWriter myWriter;
		myWriter = new QRCodeWriter();

		ByteMatrix myByteMatrix = null;

		Byte myPixel;

		int myColor;

		try {
			myByteMatrix = myWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

			// COPY THE BYTEMATRIX TO THE PIMAGE
			for(int i=0; i<width; i++)
				for(int j=0; j<height; j++)
				{ 
					myPixel = myByteMatrix.get(j,i);
					myColor = 0; // BLACK
					if(myPixel!=0) myColor = 16777215;	// WHITE
					// ADD THE PIXEL TO THE IMAGE
					myPImage.set(i,j,myColor);
				} // for(int j=0; j<height; j++)
		}
		catch ( Exception e )
		{
			System.out.println("Error generating QRCode image (PImage generateQRCode)");
		}
		
		return myPImage;
	} // PImage generateQRCode()
	
	
	/*********************************************************************************
	 *
	 *	Returns a PVector array with the position markers for the latest detected QRCode
	 *
	 *	@return	PVector with the position markers for the latest detected QRCode
	 *
	 *********************************************************************************/
	public PVector[] getPositionMarkers()
	{
		return positionMarkers;
	} // PVector[] getPositionMarkers()
	

	/*********************************************************************************
	 *
	 *	Displays version information of this library in the console
	 *
	 *********************************************************************************/
	public void version()
	{
		System.out.println("Zxing4processing v"+thisVersion+" ("+thisReleaseDate+"), by Rolf van Gelder (cagewebdev.com)\n");
	} // void version()	
} // public class ZXING4P