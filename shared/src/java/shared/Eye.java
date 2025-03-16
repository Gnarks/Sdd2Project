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
  
  public VisionEnum seeNode(Segment line) {
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
          return VisionEnum.BOTH;
        }
        return VisionEnum.HPLUS;
      }
      if (lineSlope  >= 0 && (this.angle > limit && ((angleRight)%360 >= limit || angleLeft <= alpha+360))){
        return VisionEnum.HPLUS;
      }  
      if (lineSlope < 0 && (this.angle <= limit && ((angleLeft)%360 <= limit || angleRight >= alpha))){
        return VisionEnum.HPLUS;
      } 
    }
    if (loc == -1){
      if(line.isVertical()){
        if(Math.cos(angleRadians) > 0){
          return VisionEnum.BOTH;
        }
        return VisionEnum.HMINUS;
      }
      if(lineSlope>=0 && (angle <= limit && ((angleLeft)%360 <= limit || angleRight >= alpha))){
        return VisionEnum.HMINUS;
      } 
      if (lineSlope < 0 && (angle > limit && ((angleRight)%360 >= limit || angleLeft <= alpha+360))){
        return VisionEnum.HMINUS;
      } 
    }
    return VisionEnum.BOTH;

  }

  public Segment seenSegment(Segment seg,double distance, Segment line){
  
    double angleRight = Math.toRadians(angle-fov);
    double angleLeft = Math.toRadians(angle+fov);
    
    double adj = Math.tan(Math.toRadians(fov));
    double hypo = Math.sqrt(Math.pow(adj,2)+Math.pow(distance,2));

    Segment fovLeft = new Segment(this.position.x,this.position.y,this.position.x+hypo*Math.cos(angleLeft),this.position.y+hypo*Math.sin(angleLeft),seg.getColor());
    Segment fovRight =new Segment(this.position.x,this.position.y,this.position.x+hypo*Math.cos(angleRight),this.position.y+hypo*Math.sin(angleRight),seg.getColor());


//TODO calculer angle Start et End
//    check si entre angleLeft et angleRight
//    ni l'un ni l'autre => 
//    check angle avec les intersection 
//    if angle=Fov ok
//    else
//    return null
//    entre les 2 => return seg
//    voit start et pas end =>
//    checker angleEnd plus proche de angleRight ou angleLeft => prendre intersection entre 
//                                                               le fov et le segment et conserver start
//    same angleStart
//

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

    
    if((angle-fov == 0 || angle-fov == 180)&&(Utils.areEqual(Math.cos(angleLeft), locationPointLeft))){
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
