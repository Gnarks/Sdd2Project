package shared;

import java.util.ArrayList;
import java.lang.Math;
import shared.geometrical.*;

public class Utils {
  
  /**
   * Define if 2 values are equals with a biais
   */
  public static boolean areEqual(double x1, double x2){
    return Math.abs(x1 - x2) < 1E-10;
  }

  public static boolean lowerOrEqual(double x1, double x2){
    return areEqual(x1,x2) || x1 < x2;
  }

  public static void sortSegments(ArrayList<Segment> seg){ 
      seg.sort((a,b)->{
        return Double.compare(a.getStart().x, b.getStart().x);
      });
  }


}
