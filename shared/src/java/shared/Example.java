package shared;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
public class Example {

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main() {
        System.out.println(new Example().getGreeting());
        Point A = new Point(-2,0);
        Point B = new Point(-1,1);
        Point C = new Point(8,1);
        Point D = new Point(9,2);
        Point E = new Point(-9,2);
        Point F = new Point(-8,1);
        Point G = new Point(1,1);
        Point H = new Point(2,2);
        Point I = new Point(2,0);
        Point J = new Point(2,4);
        Point K = new Point(4,2);
        Color color = Color.BLUE;
        
        Segment AB = new Segment(A,B,color);
        Segment CD = new Segment(C,D,color);
        Segment EF = new Segment(E,F,color);
        Segment GH = new Segment(G,H,color);
        Segment JK = new Segment(J,K,color);

        Segment IA = new Segment(A,I,color);
        Segment test = new Segment(B,G,Color.RED);
        Segment CG = new Segment(G,C,Color.BLUE);

        Segment[] data = new Segment[]{AB,GH,JK};
        Segment[] data2 = new Segment[]{IA,test};
        
          
        Point eue = new Point(0,6);
        Eye p = new Eye(eue,270,90);
        ArrayList<Segment> segments = new ArrayList<>(Arrays.asList(data2));  
        BSP bsp = new BSP(segments, new FirstMethod());
        double[] range = new double[]{4,3};

        System.out.println(bsp.getHead());
        EyeSegment pSeg = bsp.painterAlgorithm(p,range);
    }

}
