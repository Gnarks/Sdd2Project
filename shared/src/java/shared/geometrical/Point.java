package shared.geometrical;

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
}
