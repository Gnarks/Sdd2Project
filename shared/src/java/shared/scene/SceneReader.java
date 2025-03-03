package shared.scene;
import java.io.*;
import java.util.ArrayList;
import shared.*;

public class SceneReader{

  public Scene read (String fileName){

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String read;
      boolean first= true;

      ArrayList<Segment> segList = new ArrayList<>();
      Point topLeft = null;
      Point topRight = null;
      Point bottomLeft = null;
      Point bottomRight = null;

      while ((read = reader.readLine()) != null){

        String[] line = read.split(" ");  
        if (first){
          first = false;
          double a = Double.parseDouble(line[1]);
          double b = Double.parseDouble(line[2]);
          topLeft = new Point(-a, -b);
          topRight = new Point(a, -b);
          bottomLeft = new Point(-a, b);
          bottomRight = new Point(a, b);
          continue;
        }


        String color;
        //Bleu, Rouge, Orange, Jaune, Noir, Violet, Marron, Vert, Gris et Rose
        switch (line[4]){
          case "Bleu":
            color = "blue";
            break;
          case "Rouge":
            color = "red";
            break;
          case "Orange":
            color = "orange";
            break;
          case "Jaune":
            color = "yellow";
            break;
          case "Noir":
            color = "black";
            break;
          case "Violet":
            color = "magenta";
            break;
          case "Marron":
            color = "brown";
            break;
          case "Vert":
            color = "green";
            break;
          case "Gris":
            color = "gray";
            break;
          case "Rose":
            color = "pink";
            break;
          default:
            // ERROR COLOR = CYAN
            color = "cyan";
            break;
        }

        segList.add(new Segment(Double.parseDouble(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2]),Double.parseDouble(line[3]), color));
      }
      reader.close();

      return new Scene(segList, topLeft, topRight, bottomLeft, bottomRight);

    }
    catch (IOException a) {
      a.printStackTrace();
    }
    return null;
  }

}
