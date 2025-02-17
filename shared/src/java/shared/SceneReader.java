package shared;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;

public class SceneReader{

  public ArrayList<Segment> read  (String fileName) throws IOException{

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String read;
      boolean first= true;

      ArrayList<Segment> segList = new ArrayList<>();

      while ((read = reader.readLine()) != null){

        String[] line = read.split(" ");  
        if (first){
          first = false;
          continue;
        }


        Color color;
        //Bleu, Rouge, Orange, Jaune, Noir, Violet, Marron, Vert, Gris et Rose
        switch (line[4]){
          case "Bleu":
            color = Color.BLUE;
            break;
          case "Rouge":
            color = Color.RED;
            break;
          case "Orange":
            color = Color.ORANGE;
            break;
          case "Jaune":
            color = Color.YELLOW;
            break;
          case "Noir":
            color = Color.BLACK;
            break;
          case "Violet":
            color = Color.MAGENTA;
            break;
          case "Marron":
            color = new Color(102,51,0);
            break;
          case "Vert":
            color = Color.GREEN;
            break;
          case "Gris":
            color = Color.GRAY;
            break;
          case "Rose":
            color = Color.PINK;
            break;
          default:
            // ERROR COLOR = CYAN
            color = Color.CYAN;
            break;
        }

        segList.add(new Segment(Double.parseDouble(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2]),Double.parseDouble(line[3]), color));
      }
      reader.close();

      return segList;

    }
    catch (IOException a) {
      throw a;
    }
  }

}
