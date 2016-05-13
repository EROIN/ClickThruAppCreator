import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.*;

class ClickableObject
{
  Point p1,p2;
  String targetImageName=null;

  ClickableObject(String targetImageName,Point p1,Point p2)
  {
    this.targetImageName=targetImageName;
    this.p1=p1;
    this.p2=p2;
  }
}
