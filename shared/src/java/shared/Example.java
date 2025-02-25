package shared;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import shared.generation.RandomMethod;
public class Example {

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main() {
        System.out.println(new Example().getGreeting());
        Point A = new Point(0,0);
        Point B = new Point(-1,1);
        Point C = new Point(8,1);
        Point D = new Point(9,2);
        Point E = new Point(-9,2);
        Point F = new Point(-8,1);
        Point G = new Point(1,1);
        Point H = new Point(2,2);
        Point I = new Point(5,5);
        Point J = new Point(2,4);
        Point K = new Point(4,2);
        Color color = Color.RED;
        
        Segment AB = new Segment(A,B,"red");
        Segment CD = new Segment(C,D,"red");
        Segment EF = new Segment(E,F,"red");
        Segment GH = new Segment(G,H,"red");
        Segment JK = new Segment(J,K,"red");

        Segment IA = new Segment(A,I,"red");
        
        
        Segment[] data = new Segment[]{AB,CD,EF,GH,JK};
        ArrayList<Segment> segments = new ArrayList<>(Arrays.asList(data));  
        BSP bsp = new BSP(segments, new RandomMethod());
        System.out.println(bsp.getHead());
    }

}
