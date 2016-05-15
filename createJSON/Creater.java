import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.nio.file.*;


class Creater
{
static ClickableAdder adder;
public static void main(String args[])throws Exception
{
Toolkit kit=Toolkit.getDefaultToolkit();
Dimension d=kit.getScreenSize();
JSONObject jsonFileContentObject=null;
JFrame frame=new JFrame("Click-Thru App Creator");
writeImageAssets();
frame.setSize(1024,768);
frame.setLocation(d.width/8,d.height/20);
frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
frame.addWindowListener(new WindowAdapter() {
    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        System.out.println("closing");
        adder.writeJson();
        // System.out.println(adder);
        adder.copyFileToPath();
        System.out.println("file copied");
        System.exit(0);
    }
});
frame.setVisible(true);
frame.setResizable(false);
//if test.json is Found
String initialImageName=null;
int dialogResult=0;
if(new File("test.json").exists())
{
  dialogResult = JOptionPane.showConfirmDialog (frame, "Continue with the previous project?","A small Doubt :( !!",dialogResult);
  System.out.println(dialogResult);
  if(dialogResult==1)
  {
    initialImageName=askHomeScreenImageName(frame);
  }
  else
  {
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(new FileReader("test.json"));
    jsonFileContentObject = (JSONObject) obj;
    initialImageName="images/"+(String) jsonFileContentObject.get("startImageKey");
  }
}
else
{
  initialImageName=askHomeScreenImageName(frame);
}
//if test.json not Found

DisplayImage firstImage=new DisplayImage(initialImageName);
adder=new ClickableAdder(frame,firstImage,initialImageName,jsonFileContentObject);
frame.add(adder);
frame.setVisible(true);
frame.add(firstImage);
frame.addMouseListener(adder);
frame.addMouseMotionListener(adder);
frame.setVisible(true);
}

private static void writeImageAssets()  //for writing image assets to Xcode
{
  File imagesFolder=new File("images/");
  File [] listOfImageFiles=imagesFolder.listFiles();
  int noOfFiles=listOfImageFiles.length;
  String reactNativeAppPath="../ReactNativeJson/ios/ReactNativeJson/Images.xcassets/";
  for(int loop=0;loop<noOfFiles;loop++)
  {
    System.out.println("src="+listOfImageFiles[loop]);
    String imageFileName=listOfImageFiles[loop].toString().substring(7);
    // System.out.println("dest="+reactNativeAppPath+imageFileName+"/"+imageFileName);
    String folderName=reactNativeAppPath+imageFileName.substring(0,imageFileName.length()-4)+".imageset";
    File creatFolder=new File(folderName);
    creatFolder.mkdir();
    String jsonContentString="{\"images\" : [{\"idiom\" : \"universal\",\"filename\" : \""+imageFileName+"\",\"scale\" : \"1x\"},{\"idiom\" : \"universal\",\"scale\" : \"2x\"},{\"idiom\" : \"universal\",\"scale\" : \"3x\"}],\"info\" : {\"version\" : 1,\"author\" : \"xcode\"}}";
    try
    {
      Files.copy(listOfImageFiles[loop].toPath(),new File(folderName+"/"+imageFileName).toPath() , StandardCopyOption.REPLACE_EXISTING);
      FileWriter jsonFileWriter=new FileWriter(folderName+"/Contents.json");
      jsonFileWriter.write(jsonContentString);
      jsonFileWriter.flush();
      jsonFileWriter.close();
    }
    catch(IOException e){}
  }
}

private static String askHomeScreenImageName(JFrame frame)
{
  File imageFile=null;
  String initialImageName=null;
  String invalidString="";
  do
  {
      initialImageName=(String) JOptionPane.showInputDialog(frame,"Please select a home screen.\n"+ "\""+invalidString+"Enter an image name\"","Home Screen Image",JOptionPane.PLAIN_MESSAGE,null,null,"fleetapp(ipad)-1.jpg");
      initialImageName="images/"+initialImageName;
      // System.out.println(initialImageName);
      if(initialImageName!=null)
      {
        invalidString="File Not Found :(  ";
        imageFile=new File(initialImageName);
        System.out.println(imageFile.exists()&&!imageFile.isDirectory());
      }
      else
      {
        invalidString="";
      }
  }
  while(initialImageName==null||!(imageFile.exists()&&!imageFile.isDirectory()));
  return initialImageName;
}

}
