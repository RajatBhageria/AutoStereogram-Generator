/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Kinect;

import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import static processing.core.PApplet.println;

/**
 *
 * @author crisscrosskao
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Iterator;
import SimpleOpenNI.*;
import processing.core.*;
import static processing.core.PApplet.println;

public class DepthMap extends PApplet 
{
  static boolean recordFlag = false;
  public DepthMap() 
  {
      SimpleOpenNI context = new SimpleOpenNI (this);
      ContextTest = context;
  }
  public static void main(String args[]) 
  {
    PApplet.main(new String[] {"Kinect.DepthMap"});
  }
  private SimpleOpenNI ContextTest;
  
  public static void changeRecordFlag(boolean b)
  {
      recordFlag = b;
  }
  
  public static boolean getRecordFlag()
  {
      return recordFlag;
  }
  
  
  public void setup() 
  {
    size(640*2, 480);
    frameRate(30);
    if (ContextTest.isInit() == false)
    {
        println("Can't init SimpleOpenNI, maybe the camera is not connected!"); 
        exit();
        return;
    }
    ContextTest.setMirror(true);

    // enable depthMap generation 
    ContextTest.enableDepth();

    // enable ir generation
    ContextTest.enableRGB();
  }

  // mirror is by default enabled

 public void draw() 
  {
    ContextTest.update();

     background(200, 0, 0);

  // draw depthImageMap
    image(ContextTest.depthImage(), 0, 0);

  // draw irImageMap
    image(ContextTest.rgbImage(), ContextTest.depthWidth() + 10, 0);
    if (recordFlag)
    {
        saveFrame("frames/img-####.tiff");
    }
  }
 
}






