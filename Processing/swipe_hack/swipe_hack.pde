import de.voidplus.leapmotion.*;
import muthesius.net.*;
import org.webbitserver.*;

LeapMotion leap;
WebSocketP5 socket;

void setup()
{
  size(800, 500, P3D);
  background(255);
  socket = new WebSocketP5(this,8080);
    
  leap = new LeapMotion(this).withGestures("swipe");
}

void draw()
{
  background(255);
}

void stop()
  {
    socket.stop();
  }

void leapOnSwipeGesture(SwipeGesture g, int state)
{
  int       id               = g.getId();
  Finger    finger           = g.getFinger();
  PVector   position         = g.getPosition();
  PVector   position_start   = g.getStartPosition();
  PVector   direction        = g.getDirection();
  float     speed            = g.getSpeed();
  long      duration         = g.getDuration();
  float     duration_seconds = g.getDurationInSeconds();

  switch(state)
  {
    case 1: // Start
      break;
    case 2: // Update
      break;
    case 3: // Stop
      socket.broadcast("hi");
      break;
  }
}
  
  void mousePressed()
  {
    socket.broadcast("hello from processing!");
  }

  void websocketOnMessage(WebSocketConnection con, String msg)
  {
    println(msg);
  }

  void websocketOnOpen(WebSocketConnection con)
  {
    println("A client joined");
  }

  void websocketOnClosed(WebSocketConnection con)
  {
    println("A client left");
  }


