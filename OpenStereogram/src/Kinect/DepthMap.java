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
 * @author Christopher Kao
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
  String homeDir = System.getProperty("user.home");
  String saveFrameDir = homeDir + "/Pictures/Autostereogram Frames/img-#####.jpg";
  private SimpleOpenNI kinect;
  //private PImage liveMap;
  //private int maxDistance = 1500;
  
  public DepthMap() 
  {
      kinect = new SimpleOpenNI (this);
  }
  public static void main(String args[]) 
  {
    PApplet.main(new String[] {"Kinect.DepthMap"});
  }
  
  public static void changeRecordFlag(boolean b)
  {
      recordFlag = b;
  }
  
  public static boolean getRecordFlag()
  {
      return recordFlag;
  }
  
  @Override
  public void setup() 
  {
    size(640, 480);
    frameRate(30);
//    if (kinect.isInit() == false)
//    {
//        println("Can't init SimpleOpenNI, maybe the camera is not connected!"); 
//        exit();
//        return;
//    }
    kinect.setMirror(false);

    // enable depthMap generation 
    kinect.enableDepth();
    //liveMap = loadImage("./images/try_640_480.jpg");
  }

  // mirror is by default enabled

 @Override
 public void draw() 
  {
     background(color(0,0,0));
     kinect.update();
     //int[] depthValues = kinect.depthMap();
     //liveMap.width = 640;
     //liveMap.height = 480;
     //liveMap.loadPixels();
     
     //for (int y = 0; y < 480; y++) {
       // for (int x = 0; x < 640; x++) {
           //int i = x + (y * 640);
           //int currentDepthValue = depthValues[i];
           //if (currentDepthValue > maxDistance) {
             //  liveMap.pixels[i] = color(0, 0, 0);  
          // }       
         //  else {
           //    int lum = (int) (-0.2684 * currentDepthValue + 402.63);
          //     liveMap.pixels[i] = color(lum, lum, lum);
          // }
      //  }  
     //}
     
     //liveMap.updatePixels();
     
  // draw depthImageMap
    //image(liveMap, 0, 0);
    image(kinect.depthImage(), 0, 0);

   // draw irImageMap
   // image(ContextTest.rgbImage(), ContextTest.depthWidth() + 10, 0);
    if (recordFlag)
    {
        saveFrame(saveFrameDir);
    }
  }
 
}






