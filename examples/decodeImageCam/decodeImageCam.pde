/*****************************************************************************
 *
 *  decodeImageCam - v07/31/2016
 *
 *  An example of the use of the ZXING4P.decodeImage() method.
 *
 *  Opens a webcam and tries to find QRCodes in the cam captured images
 *  using the ZXING4P.decodeImage() method.
 *
 *  When a QRCode is detected it will display the decoded text.
 *
 *  Run this sketch and hold a printed copy of a QRCode in front of the cam.
 *
 *  Note: make sure your video image is NOT mirrored! It won't detect QRCodes
 *  that way...
 *
 *  Library page:
 *  http://cagewebdev.com/zxing4processing-processing-library/
 *
 *  (c) 2013-2016 Rolf van Gelder, http://cagewebdev.com, http://rvg.cage.nl
 * 
 *****************************************************************************/

// IMPORT THE ZXING4PROCESSING LIBRARY AND DECLARE A ZXING4P OBJECT
import com.cage.zxing4p3.*;
ZXING4P zxing4p;

// PROCESSING VIDEO LIBRARY
import processing.video.*;
Capture video;

String decodedText;
String latestDecodedText = "";

int tw;


/*****************************************************************************
 *
 *  SETUP
 *
 *****************************************************************************/
void setup()
{
  size(640, 480);

  // LAYOUT
  textAlign(CENTER);
  textSize(30);

  // CREATE CAPTURE
  video = new Capture(this, width, height);

  // START CAPTURING
  video.start();  

  // CREATE A NEW EN-/DECODER INSTANCE
  zxing4p = new ZXING4P();

  // DISPLAY VERSION INFORMATION
  zxing4p.version();
} // setup()


/*****************************************************************************
 *
 *  DRAW
 *
 *****************************************************************************/
void draw()
{ 
  background(0);

  // UPDATE CAPTURE
  if (video.available()) video.read();

  // DISPLAY VIDEO CAPTURE
  set(0, 0, video);

  // DISPLAY LATEST DECODED TEXT
  if (!latestDecodedText.equals(""))
  {
    tw = int(textWidth(latestDecodedText));
    fill(0, 150);
    rect((width>>1)-(tw>>1)-5, 15, tw+10, 36);
    fill(255);
    text(latestDecodedText, width>>1, 43);
  }

  // TRY TO DETECT AND DECODE A QRCODE IN THE VIDEO CAPTURE
  // decodeImage(boolean tryHarder, PImage img)
  // tryHarder: false => fast detection (less accurate)
  //            true  => best detection (little slower)
  try
  {  
    decodedText = zxing4p.decodeImage(false, video);
  }
  catch (Exception e)
  {  
    println("Zxing4processing exception: "+e);
    decodedText = "";
  }

  if (!decodedText.equals(""))
  { // FOUND A QRCODE!
    if (latestDecodedText.equals("") || (!latestDecodedText.equals(decodedText)))
      println("Zxing4processing detected: "+decodedText);
    latestDecodedText = decodedText;
  }
} // draw()