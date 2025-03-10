package shared;

import java.util.ArrayList;
import java.util.Arrays;
import shared.generation.*;
import shared.scene.*;
public class Example {

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main() {
        System.out.println(new Example().getGreeting());
        
        Point A = new Point(0,0);
        Point B = new Point(0,5);
        Point C = new Point(6,6);
        Point D = new Point(7,7);
        Point E = new Point(-2,-3);
        Point F = new Point(1,-3);


        Segment AB = new Segment(A,B,"red");
        Segment CD = new Segment(C,D,"black");
        Segment EF = new Segment(F,E,"blue");

        Segment[] data = new Segment[]{AB,CD,EF};

        SceneReader sceneReader = new SceneReader();
        Scene scene = sceneReader.read("/home/wal//Documents/Sdd2Project/shared/src/ressources/scenes/first/octogone.txt");
        ArrayList<Segment> segments = scene.getSegList();        

          
        Point eue = new Point(0,-100);
        Eye p = new Eye(eue,0,45);

        //ArrayList<Segment> segments = new ArrayList<>(Arrays.asList(data));  
        BSP bsp = new BSP(segments, new FirstMethod());
        double[] range = new double[]{200,200};

        System.out.println(bsp.getHead());
        System.out.println("Height = " + bsp.getHead().height);
        EyeSegment pSeg = bsp.painterAlgorithm(p,range);
        System.out.println(pSeg);
    }

}
