package shared;

import java.util.ArrayList;
import java.lang.Math;

public class Utils {
  
  /**
   * Define if 2 values are equals with a biais
   */
  public static boolean areEqual(double x1, double x2){
    return Math.abs(x1 - x2) < 1E-10;
  }
  
  /**
   * Define if 2 points are equals with a biais
   */
  public static boolean areEqual(Point p1, Point p2){
    return Math.abs(p1.x - p2.x) < 1E-10 && Math.abs(p1.y - p2.y) < 1E-10;
  }


  public static void sortSegments(ArrayList<Segment> seg){ 
      seg.sort((a,b)->{
        return Double.compare(a.getStart().x, b.getStart().x);
      });
  }


}
