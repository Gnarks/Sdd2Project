package shared;

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
}
