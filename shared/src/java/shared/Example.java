package shared;

import java.util.ArrayList;
import shared.generation.*;
import shared.geometrical.*;
import shared.scene.*;
public class Example {

  public String getGreeting() {
    return "Hello World!";
  }

  public static void main() {

    SceneReader sceneReader = new SceneReader();
    Scene scene = sceneReader.read("/home/etude/Documents/Bac3/Q1/SDD2_/PSdd2/shared/src/ressources/scenes/ellipses/ellipsesLarge.txt");
    ArrayList<Segment> segments = scene.getSegList();


    Point eue = new Point(0,0);
    Eye p = new Eye(eue,0,45);

    //ArrayList<Segment> segments = new ArrayList<>(Arrays.asList(data));  
    BSP bsp = new BSP(segments, new RandomMethod());
    Point range = new Point(scene.getRange().x, scene.getRange().y);


    Projection pSeg = bsp.painterAlgorithm(p,range);
    //System.out.println(pSeg);
    //pSeg.flatten(200);
    //System.out.println(pSeg);
  }

}
