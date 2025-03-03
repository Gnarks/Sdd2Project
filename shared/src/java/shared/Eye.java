package shared;

import java.lang.Math;

public class Eye {
  private Point position;
  private double angle;
  private double fov;

  public Eye(Point position, double angle, double fov){
    this.position = position;
    this.angle = angle%360;
    this.fov = fov%90;
  }

  public Point getPos(){return position.copy();}
  public void setPos(Point pos){this.position = pos;}

  public double getAngle(){return this.angle;}
  public void setAngle(double angle){ this.angle = angle; }

  public double getFov(){ return fov; }
  public void setFov(double fov){ this.fov = fov; }

  
  public int seeNode(Segment line) {
    int loc = line.locationPoint(this.position);
    double alpha = Math.atan(Math.toRadians(line.getLine()[0])); 
    double limit = 180+alpha;


    if(loc == 1){
      if(line.isVertical()){
        if(angle > 90 && angle < 270){
          return 0;
        }
        return 1;
      }
      if (line.getLine()[0] >= 0){
        if(angle > limit && ((angle-fov)%360 >= limit || angle+fov <= alpha+360)){return 1;} 
      }
      if (line.getLine()[0] < 0){
        if(angle <= limit && ((angle+fov)%360 <= limit || angle-fov >= alpha)){return 1;} 
      }
    }
    if (loc == -1){
      if(line.isVertical()){
        if(Math.cos(Math.toRadians(angle)) > 1){
          return 0;
        }
        return -1;
      }
      if(line.getLine()[0]>=0){
        if(angle <= limit && ((angle+fov)%360 <= limit || angle-fov >= alpha)){return -1;} 
      }
      if (line.getLine()[0] < 0){
        if(angle > limit && ((angle-fov)%360 >= limit || angle+fov <= alpha+360)){return -1;} 
      }
    }
    return 0;

  }

  public Segment seeSegment(Segment seg){
    int[] fovLine = fovLine();
    double angleRight = Math.toRadians(angle-fov);
    double angleLeft = Math.toRadians(angle+fov);

    double yLeft;
    double yRight;
    if (angle+fov==270 || angle+fov == 90){
      yLeft = Math.sin(angleLeft);
    }else{
      yLeft= Math.tan(angleLeft)*(position.x + fovLine[0]); 
    }
    if (angle-fov ==270 || angle-fov == 90){
      yRight = Math.sin(angleRight);
    }else{
      yRight= Math.tan(angleRight)*(position.x + fovLine[1]); 
    }
    Point fovLeftPoint = new Point(position.x + fovLine[0],yLeft + position.y);
    Point fovRightPoint = new Point(position.x + fovLine[1],yRight + position.y);
    
    Segment fovLeft = new Segment(this.position,fovLeftPoint,seg.getColor());
    Segment fovRight = new Segment(this.position,fovRightPoint,seg.getColor());

    int locationStartRight= fovRight.locationPoint(seg.getStart());
    int locationEndRight = fovRight.locationPoint(seg.getEnd());

    int locationStartLeft= fovLeft.locationPoint(seg.getStart());
    int locationEndLeft = fovLeft.locationPoint(seg.getEnd());
    
    boolean seeStart = seePoint(fovLeft, fovRight, seg.getStart());
    boolean seeEnd = seePoint(fovLeft, fovRight, seg.getEnd());

    if(seeStart && seeEnd){
      if (Utils.areEqual(seg.getStart(),seg.getEnd())){
        return null;
      }
        return seg;}

    if(seeStart || seeEnd){
      Point inter;
      if(locationStartLeft != locationEndLeft){
        inter = fovLeft.interSeg(seg); 
      }else{
        inter = fovRight.interSeg(seg);
      }
      if(seeStart){ 
        return new Segment(seg.getStart(),inter,seg.getColor());}
      return new Segment(inter,seg.getEnd(),seg.getColor());
    }
    Point interRight = fovRight.interSeg(seg);
    Point interLeft = fovLeft.interSeg(seg);
    if(interRight == null || interLeft == null){
      return null;
    }
    return new Segment(interLeft,interRight,seg.getColor());

  }

  public int[] fovLine(){
    int[] toReturn = new int[2];
    double angleRight = (angle - fov + 360)%360;
    double angleLeft = (angle + fov)%360;

    if (angleRight == 90 || angleRight == 270){
      toReturn[1] = 0;
    }
    else if (angleRight >90 && angleRight<270) {
      toReturn[1] = -1;      
    }
    else{
      toReturn[1] = 1;
    }

    if (angleLeft == 90 || angleLeft == 270){
      toReturn[0] = 0;
    }
    else if (angleLeft >90 && angleLeft<270) {
      toReturn[0] = -1;      
    }else{
      toReturn[0] = 1;
    }

    return toReturn;
  }

  public boolean seePoint(Segment fovLeft, Segment fovRight,Point p){
    double angleRight = Math.toRadians(angle-fov);
    double angleLeft = Math.toRadians(angle+fov);
    
    int locationPointLeft = fovLeft.locationPoint(p);
    int locationPointRight =fovRight.locationPoint(p);

    
    if((angle-fov == 0 || angle-fov == 180)&&(Math.cos(angleRight) == locationPointRight)){
      return false;
    }  
  
    else if((angle+fov == 0 || angle+fov == 180)&&(Math.cos(angleRight) == locationPointRight)){
        return false;
    }

    if((!Utils.areEqual(Math.sin(angleRight), 0) && !Utils.areEqual(Math.sin(angleLeft),0))&&((locationPointRight == 0 || locationPointLeft == 0)||
    (Math.sin(angleLeft)*locationPointLeft < 0 || Math.sin(angleRight)*locationPointRight > 0))){
      return false;
    } 
    return true;
  }
}
