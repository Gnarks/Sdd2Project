package shared;

import java.lang.Math;

public class DoubleUtils{
  
  /** returns if the doubles are equal with 1E-5 precision 
 * @param x1
 * @param x2
 * @return
   */
  public static boolean areEqual(double x1, double x2){
    return Math.abs(x1 - x2) < 1E-5;
  }


  /** returns if the doubles are lower or equal
   * @see DoubleUtils.areEqual
   */
  public static boolean lowerOrEqual(double x1, double x2){
    return areEqual(x1,x2) || x1 < x2;
  }
}
