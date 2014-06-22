
import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class DrawLines extends GraphicsProgram {

/* Initializes the program by enabling the mouse listeners */
   public void init() {
      addMouseListeners();
   }

/* Called on mouse press to create a new line */
   public void mousePressed(MouseEvent e) {
      line = new GLine(e.getX(), e.getY(), e.getX(), e.getY());
      add(line);
   }

/* Called on mouse drag to extend the endpoint */
   public void mouseDragged(MouseEvent e) {
      line.setEndPoint(e.getX(), e.getY());
   }

/* Private instance variables */
   private GLine line;
}


