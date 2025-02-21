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
  public Point getPos(){
    return position.copy();
  }
  public void setPos(Point pos){
    this.position = pos;
  }

  public double getAngle(){ return angle; }
  public void setAngle(double angle){ this.angle = angle; }

  public double getFov(){ return fov; }
  public void setFov(double fov){ this.fov = fov; }


}
