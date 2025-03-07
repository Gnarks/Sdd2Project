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


  /**
   * return 0 if see both side of the line
   *        1 if see only h+ side of the line
   *        -1 if see only h- side of the line
   */
  
  public LocationEnum seeNode(Segment line) {
    double angleRadians = Math.toRadians(this.angle);
    double angleLeft = angle+fov;
    double angleRight = angle-fov;

    double lineSlope = line.getLine()[0];
    int loc = line.locationPoint(this.position);
    double alpha = Math.toDegrees(Math.atan(lineSlope)); 
    double limit = 180+alpha;

    if(loc == 1){
      if(line.isVertical()){
        if(Math.cos(angleRadians) < 0){
          return LocationEnum.ALIGNED;
        }
        return LocationEnum.HPLUS;
      }
      if (lineSlope  >= 0 && (this.angle > limit && ((angleRight)%360 >= limit || angleLeft <= alpha+360))){
        return LocationEnum.HPLUS;
      }  
      if (lineSlope < 0 && (this.angle <= limit && ((angleLeft)%360 <= limit || angleRight >= alpha))){
        return LocationEnum.HPLUS;
      } 
    }
    if (loc == -1){
      if(line.isVertical()){
        if(Math.cos(angleRadians) > 0){
          return LocationEnum.ALIGNED;
        }
        return LocationEnum.HMINUS;
      }
      if(lineSlope>=0 && (angle <= limit && ((angleLeft)%360 <= limit || angleRight >= alpha))){
        return LocationEnum.HMINUS;
      } 
      if (lineSlope < 0 && (angle > limit && ((angleRight)%360 >= limit || angleLeft <= alpha+360))){
        return LocationEnum.HMINUS;
      } 
    }
    return LocationEnum.ALIGNED;

  }

  public Segment seenSegment(Segment seg){
    int[] fovLine = fovLine();
    double angleRight = Math.toRadians(angle-fov);
    double angleLeft = Math.toRadians(angle+fov);
  
    double yLeft;
    double yRight;
    if (Utils.areEqual(Math.cos(Math.toRadians(angle+fov)),0)){
      yLeft = Math.sin(angleLeft);
    }else{
      yLeft= Math.tan(angleLeft)*(position.x + fovLine[0]); 
    }
    if (Utils.areEqual(Math.cos(Math.toRadians(angle-fov)),0)){
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
    if((interBehind(interLeft,interRight)) || (!seg.onSeg(interRight) || !seg.onSeg(interLeft))){
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

    
    if((angle-fov == 0 || angle-fov == 180)&&(Utils.areEqual(Math.cos(angleRight), locationPointRight))){
      return false;
    }  
  
    else if((angle+fov == 0 || angle+fov == 180) && (Utils.areEqual(Math.cos(angleRight), locationPointRight))){
        return false;
    }

    if((Math.sin(angleLeft)*locationPointLeft < 0 || Math.sin(angleRight)*locationPointRight > 0)){
      return false;
    } 
    return true;
  }
  
  public boolean interBehind(Point interLeft, Point interRight){
    double angleRight = Math.toRadians(this.angle- this.fov);
    double angleLeft = Math.toRadians(this.angle+this.fov);

    if(Utils.areEqual(Math.cos(angleRight),0)){
      if((Math.sin(angleRight)>0 && this.position.y > interRight.y) || (Math.sin(angleRight)<0 && this.position.y < interRight.y)){
        return true;
      }
    }
  
    else if(Math.cos(angleRight) < 0){
      if (interRight.x > this.position.x){
        return true;
      }   
    }
    else if(Math.cos(angleRight) > 0){
      if(interRight.x < this.position.x){
        return true;
      }
    } 

    if(Utils.areEqual(Math.cos(angleLeft),0)){
      if((Math.sin(angleLeft)>0 && this.position.y > interLeft.y) || (Math.sin(angleLeft)<0 && this.position.y < interLeft.y)){
        return true;
      }
    }

    else if(Math.cos(angleLeft) > 0){
      if (interLeft.x < this.position.x){
        return true;
      }   
    }
    else if(Math.cos(angleLeft) < 0){
      if(interLeft.x > this.position.x){
        return true;
      }
    }
    return false;
    
  }
}
