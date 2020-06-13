/*****************************************************************************
 *
 *  decodeMultipleCodes - v04/14/2018
 * 
 *  A simple example of the use of the ZXING4P.decodeMultipleQRCodes() and
 *  ZXING4P.getPositionMarkers() methods.
 *
 *  Opens a photo and uses the ZXING4P.decodeMultipleQRCodes() method to find
 *  and decode multiple QRCode images in that photo.
 *
 *  Library page:
 *  http://cagewebdev.com/zxing4processing-processing-library/
 *
 *  (c) 2013-2018 Rolf van Gelder, http://cagewebdev.com, http://rvg.cage.nl
 *
 *****************************************************************************/

// IMPORT THE ZXING4PROCESSING LIBRARY AND DECLARE A ZXING4P OBJECT
import com.cage.zxing4p3.*;
ZXING4P zxing4p;

// THE POSITION MARKERS OF THE DETECTED QR-CODES IN THE IMAGE
ArrayList<PVector[]> alMarkers = new ArrayList<PVector[]>();

// PICTURE FOR TESTING
PImage photo;

String decodedText  = "";
String decodedArr[] = null;
String txt;
int    txtWidth;

boolean decoded = false;

/*****************************************************************************
 *
 *  SETUP
 *
 *****************************************************************************/
void setup() {
  size(640, 480);

  // CREATE A NEW EN-/DECODER INSTANCE
  zxing4p = new ZXING4P();

  // DISPLAY VERSION INFO
  zxing4p.version();

  // TEST PICTURE
  photo = loadImage("multiple.png");

  textAlign(CENTER);
} // setup()


/*****************************************************************************
 *
 *  DRAW
 *
 *****************************************************************************/
void draw() { 
  background(255);

  pushStyle();
  if (decodedText.equals("")) { 
    // DISPLAY PHOTO AND WAIT FOR KEY PRESS
    set(0, 0, photo);
    fill(50);
    txt = "Press the <SPACE>-bar to detect and decode the QRCode";

    txtWidth = int(textWidth(txt));
    fill(0, 150);
    rect((width - (txtWidth)>>1) - 6, height - 40, txtWidth + 12, 30);
    fill(255);
    text(txt, width>>1, height-20);

    // SHOW THE POSITION MARKERS (IF QR-CODE DETECTED)
    if (decoded) {
      for (int p = 0; p < decodedArr.length; p++) {
        fill(255, 0, 0);
        stroke(255, 0, 0);
        rectMode(CENTER);
        PVector[] pvArr = alMarkers.get(p);
        for (int i = 0; i < pvArr.length; i++) {
          int j = i + 1;
          if (j > 3) j = 0;
          line(pvArr[i].x, pvArr[i].y, pvArr[j].x, pvArr[j].y);
        } // for (int i = 0; i < pvArr.length; i++)
      } // for (int p = 0; p < decodedArr.length; p++)
    } // if (decoded
  } // if (decodedText.equals(""))
  popStyle();
} // draw()


/*****************************************************************************
 *
 *  KEYBOARD HANDLER
 *
 *****************************************************************************/
void keyPressed() {
  if (key == ' ') {
    // TRY TO DETECT AND DECODE QRCode(s) IN PHOTO
    if (!decodedText.equals("")) {
      // RESET
      decodedText = "";
    } else {
      try {  
        // decodeMultipleQRCodes(boolean tryHarder, PImage img)
        // tryHarder: false => fast detection (less accurate)
        //            true  => best detection (little slower)
        decodedArr = zxing4p.decodeMultipleQRCodes(true, photo);
        //decodedArr = zxing4p.test(true, photo);
        println("Number of QR Code(s) detected: " + decodedArr.length);
        for (int i = 0; i < decodedArr.length; i++) {
          println((i + 1) + ". " + decodedArr[i]);
          alMarkers.add(zxing4p.getPositionMarkers(i));
        } // for (int i = 0; i < decodedArr.length; i++)
        println("");
        decoded = true;
      } 
      catch (Exception e) {  
        println("Zxing4processing exception: "+e);
        decodedText = "";
      } // try
    } // if (!decodedText.equals(""))
  } // if (key == ' ')
} // keyPressed()
