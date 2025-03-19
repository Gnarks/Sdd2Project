package shared.geometrical;

import java.lang.Math;

public class Point {
  public double x;
  public double y;

  public Point(double x, double y){
    this.x = x;
    this.y = y;
  }

  public String toString(){
    return "(" + x + "," + y + ")";
  }

  public Point copy(){
    return new Point(x,y);
  }
  public boolean areEqual(Point p){
    return Math.abs(x - p.x) < 1E-10 && Math.abs(y - p.y) < 1E-10;
  }

  public boolean lowerOrEqual(Point p2){
    return areEqual(p2) || this.x < p2.x;
  }

  public double distanceTo(Point p){
    return Math.sqrt(Math.pow((p.x - this.x),2)+Math.pow((p.y - this.y),2));

  }
}
