package shared.scene;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SceneFinder{

  /** finds the scenes in the $PROJECTDIRECTORY/shared/ressources/scenes directory
   *
   * @return the full paths of every scene
   */
  public ArrayList<String> findScenes(){

    try {
      Enumeration<URL> e = ClassLoader.getSystemResources(".");

      URL url = null;
      while (e.hasMoreElements() ){
        url = e.nextElement();
        if (url.getPath().contains("ressources")){
          break; 
        }
      }

      ArrayList<File> scenes = new ArrayList<>();
      listf(url.getFile(), scenes);

      ArrayList<String> scenesString = new ArrayList<>();
      for (File f : scenes){
        scenesString.add(f.toString());
      }
      return scenesString;
    }
    catch (IOException e){
      e.printStackTrace();
      return null;
    }

  }

  public void listf(String directoryName, List<File> files) throws IOException {
    try {
      File directory = new File(directoryName).getCanonicalFile();

      // Get all files from a directory.
      File[] fList = directory.listFiles();
      if(fList != null)
      for (File file : fList) {      
        if (file.isFile()) {
          files.add(file);
        } else if (file.isDirectory()) {
          listf(file.getAbsolutePath(), files);
        }
      }
    }
    catch (IOException e) {
      throw e;
    }
  }
}
