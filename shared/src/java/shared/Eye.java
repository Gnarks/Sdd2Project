package shared;

public class Eye {
  private Point position;
  private double angle;
  private double fov;

  public Eye(Point position, double angle, double fov){
    this.position = position;
    this.angle = angle%360;
    this.fov = fov%90;
  }

  public Point getPosition(){
    return this.position;
  }

  public double getAngle(){
    return this.angle;
  }
}
