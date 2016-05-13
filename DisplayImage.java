import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.*;
import java.io.*;
import sun.audio.*;
class DisplayImage extends JComponent
{

  Image backgroundImage=null;
  DisplayImage(String backgroundImageString) throws Exception
  {
    backgroundImage=ImageIO.read(new File(backgroundImageString));
  }

  public void paintComponent(Graphics g)
  {
  Graphics2D g2=(Graphics2D) g;
  g2.drawImage(backgroundImage,0,0,1024,768,this);
  }

}
