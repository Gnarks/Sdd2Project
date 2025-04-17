package shared.geometrical;

import java.lang.Math;

import shared.DoubleUtils;

public class Point {
  public double x;
  public double y;

  public Point(double x, double y){
    this.x = x;
    this.y = y;
  }

  public String toString(){
    return String.format("(%s,%s)" ,x,y);
  }

  public Point copy(){
    return new Point(x,y);
  }

  public boolean equals(Point p){
    return DoubleUtils.areEqual(x, p.x) && DoubleUtils.areEqual(y, p.y);
  }

  public boolean lowerOrEqual(Point p2){
    return equals(p2) || this.x < p2.x;
  }

  public double distanceTo(Point p){
    return Math.sqrt(Math.pow((p.x - this.x),2)+Math.pow((p.y - this.y),2));

  }
}
