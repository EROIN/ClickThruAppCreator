import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.*;
import java.util.List;
import java.io.*;
import sun.audio.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class ClickableAdder extends JComponent implements MouseListener,MouseMotionListener
{
  Point p1=null,p2=null;
  boolean start=true;
  String rectLinkToImage=null;
  String initialImageName=null;
  JFrame frame=null;
  DisplayImage previous=null;
  List <ClickableObject> clickableArray=new ArrayList<ClickableObject>();
  JSONObject jsonFileContentObject = null;

  ClickableAdder(JFrame frame,DisplayImage previous,String initialImageName,JSONObject jsonObject)
  {
    this.frame=frame;
    this.previous=previous;
    this.initialImageName=initialImageName;
    this.initialImageName=this.initialImageName.substring(7);
    if(jsonObject==null)
    {
      jsonFileContentObject=new JSONObject();
    }
    else
    {
      this.jsonFileContentObject=jsonObject;
      readJson();
      this.paint(this.getGraphics());
    }
    jsonFileContentObject.put("startImageKey",initialImageName.substring(7));
  }

  public void mousePressed(MouseEvent e)
  {
    // System.out.println(e.getX());
    start=false;
    rectLinkToImage=null;
    p1=p2=new Point(e.getX(),e.getY()-25);
  }

  public void mouseDragged(MouseEvent e)
  {
    p2=new Point(e.getX(),e.getY()-25);
    // System.out.println("drag"+e.getX());
    repaint();
  }

  public void mouseReleased(MouseEvent e)
  {
    if(p2.x<=p1.x+10&&p2.y<=p1.y+10)
    {
      start=true;
      repaint();
      return;
    }
    boolean addNewClickable=selectImageName();
    if(addNewClickable==true)
    clickableArray.add(new ClickableObject(rectLinkToImage,p1,p2));
    start=true;
    repaint();
    for(ClickableObject iterator:clickableArray)
    {
      System.out.println(iterator.p1);
    }
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseClicked(MouseEvent e)
  {
    int mouseX=e.getX();
    int mouseY=e.getY()-25;
    ClickableObject found=null;
    for(ClickableObject iterator:clickableArray)
    {
      if(mouseX>=iterator.p1.x&&mouseX<=iterator.p2.x&&mouseY>=iterator.p1.y&&mouseY<=iterator.p2.y)
      {
        // System.out.println(iterator.targetImageName);
        found=iterator;
      }
    }
    if(found!=null && e.getButton() == MouseEvent.BUTTON3)
    {
      this.rectLinkToImage=found.targetImageName.substring(7);
      String tempStore=this.initialImageName;
      this.initialImageName=found.targetImageName.substring(7);
      boolean modifyClickable=selectImageName();
      this.initialImageName=tempStore;
      if(modifyClickable==true)
      found.targetImageName=this.rectLinkToImage;
      else
      {
        clickableArray.remove(found);
        repaint();
      }
      // System.out.println(found.targetImageName);
    }
    else if(found!=null &&e.getButton() == MouseEvent.BUTTON1)
    {
      DisplayImage newScreen=null;
      try
      {
        // System.out.println(found.targetImageName);
        newScreen=new DisplayImage(found.targetImageName);
      }
      catch(Exception exp)
      {
        System.out.println("not done");
      }
      writeJson();
      initialImageName=found.targetImageName.substring(7);
      readJson();
      this.frame.remove(this.previous);
      this.previous=newScreen;
      this.frame.add(newScreen);
      this.frame.setVisible(true);
    }

  }

  public void mouseMoved(MouseEvent e)
  {
  }

  public void paintComponent(Graphics g)
  {
    //g.clearRect(0,0,getWidth(), getHeight());
    // try
    // {
    //   g.drawImage(ImageIO.read(new File("images/"+initialImageName)),0,0,getWidth(), getHeight(),null);
    // }
    // catch(Exception e){}
    // System.out.println("called");
    g.setColor(new Color(255,0,0,120));
    for(ClickableObject iterator:clickableArray)
    {
      g.fillRect(iterator.p1.x,iterator.p1.y,Math.abs(iterator.p2.x-iterator.p1.x),Math.abs(iterator.p2.y-iterator.p1.y));
    }
    if(!start)
    {
      // g.setColor(new Color(255,0,0,127));
      // System.out.println(p1);
      g.fillRect(p1.x,p1.y,Math.abs(p2.x-p1.x),Math.abs(p2.y-p1.y));
      // g.drawString(rectLinkToImage==null?"":rectLinkToImage,p1.x+10,p1.y+10);
    }
  }

  private boolean selectImageName()
  {
        String invalidString="";
        File imageFile=null;
        do
        {
            rectLinkToImage=(String) JOptionPane.showInputDialog(frame,"Please enter a target screen image name.\n"+ "\""+invalidString+"Enter an image name\"","Target Screen Image",JOptionPane.PLAIN_MESSAGE,null,null,this.initialImageName);
            if(rectLinkToImage!=null)
            {
              invalidString="File Not Found :(  ";
              rectLinkToImage="images/"+rectLinkToImage;
              imageFile=new File(rectLinkToImage);
            }
            else
            {
              invalidString="";
              System.out.println("else null");
              return false;
            }
        }
        while(rectLinkToImage==null||!(imageFile.exists()&&!imageFile.isDirectory()));
        return true;
  }

  public void writeJson()
  {
    JSONObject outerObject = new JSONObject();
    outerObject.put("image",initialImageName);
    JSONArray hotSpots = new JSONArray();
    for(ClickableObject iterator:clickableArray)
    {
      JSONObject hotspotObject = new JSONObject();
      JSONObject style = new JSONObject();
      // System.out.println("iterator"+iterator.targetImageName);
      style.put("left",new Integer(iterator.p1.x));
      style.put("top",new Integer(iterator.p1.y));
      style.put("width",new Integer(iterator.p2.x-iterator.p1.x));
      style.put("height",new Integer(iterator.p2.y-iterator.p1.y));
      style.put("position","absolute");
      hotspotObject.put("style",style);
      hotspotObject.put("click",iterator.targetImageName.substring(7));
      hotSpots.add(hotspotObject);
      // System.out.println("write"+hotSpots);
    }
    outerObject.put("hotspots",hotSpots);
    jsonFileContentObject.put(initialImageName,outerObject);
    // System.out.println(mainImageObject.toJSONString());
    try
    {
      FileWriter file = new FileWriter("test.json");
  		file.write(jsonFileContentObject.toJSONString());
  		file.flush();
  		file.close();
    }
    catch(Exception e){}
  }

  public void copyFileToPath()
  {
    try
    {
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec("cp test.json ../ReactNativeJson");
    }
    catch(Exception e){}
  }

  private void readJson()
  {
    try
    {
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(new FileReader("test.json"));
      jsonFileContentObject = (JSONObject) obj;
      // System.out.println("reader"+jsonFileContentObject.toJSONString());
      // System.out.println(jsonFileContentObject.get(initialImageName));
      clickableArray.clear();
      JSONObject completeObject=(JSONObject) jsonFileContentObject.get(initialImageName);
      JSONArray newHotspots=(JSONArray) completeObject.get("hotspots");
      // System.out.println("read"+newHotspots.toJSONString());
      Iterator <JSONObject> hotspotIterator= newHotspots.iterator();
      // System.out.println("read"+hotspotIterator);
      while(hotspotIterator.hasNext())
      {
        JSONObject hotspot=hotspotIterator.next();
        // System.out.println("loop"+hotspot.toJSONString());
        String targetImage=(String)hotspot.get("click");
        targetImage="images/"+targetImage;
        // System.out.println(targetImage);
        JSONObject styleObject=(JSONObject)hotspot.get("style");
        long top=(Long)styleObject.get("top");
        long left=(Long)styleObject.get("left");
        long height=(Long)styleObject.get("height");
        long width=(Long)styleObject.get("width");
        Point p1=new Point((int)left,(int)top);
        // System.out.println("read"+p1);
        Point p2=new Point((int)left+(int)width,  (int)top+(int)height);
        clickableArray.add(new ClickableObject(targetImage,p1,p2));
      }
      // for(ClickableObject iterator:clickableArray)
      // {
      //   System.out.println(iterator.p1);
      // }
    }
    catch(Exception e)
    {
      System.out.println("no hotspots yet");
    }
  }

}
