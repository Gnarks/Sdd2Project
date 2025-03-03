package shared;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import shared.generation.*;
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


        Segment AB = new Segment(A,B,Color.RED);
        Segment CD = new Segment(C,D,Color.BLACK);
        Segment EF = new Segment(F,E,Color.BLUE);

        Segment[] data = new Segment[]{AB,CD,EF};
        
          
        Point eue = new Point(10,10);
        Eye p = new Eye(eue,225,45);
        ArrayList<Segment> segments = new ArrayList<>(Arrays.asList(data));  
        BSP bsp = new BSP(segments, new FirstMethod());
        double[] range = new double[]{10,10};

        System.out.println(bsp.getHead());
        EyeSegment pSeg = bsp.painterAlgorithm(p,range);
        System.out.println(pSeg);
    }

}
