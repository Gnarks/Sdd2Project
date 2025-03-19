package shared;

import java.lang.Math;
import shared.geometrical.*;

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
  
  public VisionEnum seeNode(Segment segment) {
    double angleRadians = Math.toRadians(this.angle);
    double angleLeft = angle+fov;
    double angleRight = angle-fov;

    double lineSlope = segment.getLine().slope;
    PartitionEnum loc = segment.relativePosition(this.position);
    double alpha = Math.toDegrees(Math.atan(lineSlope)); 
    double limit = 180+alpha;

    if(loc == PartitionEnum.HPLUS){
      if(segment.isVertical()){
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
    if (loc == PartitionEnum.HMINUS){
      if(segment.isVertical()){
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
  
    double angleRight = (angle-fov+360)%360;
    double angleLeft = (angle+fov)%360;
    
    double hypo = Math.sqrt(Math.pow(Math.tan(Math.toRadians(fov))*distance,2)+Math.pow(distance,2));

    Segment fovLeft = new Segment(this.position.x,this.position.y,this.position.x+hypo*Math.cos(Math.toRadians(angleLeft)),this.position.y+hypo*Math.sin(Math.toRadians(angleLeft)),seg.getColor());
    Segment fovRight =new Segment(this.position.x,this.position.y,this.position.x+hypo*Math.cos(Math.toRadians(angleRight)),this.position.y+hypo*Math.sin(Math.toRadians(angleRight)),seg.getColor());

    double angleStart = (anglePoint(seg.getStart())+360)%360;
    double angleEnd = (anglePoint(seg.getEnd())+360)%360;

    boolean seeStart = (angleRight < angleLeft && angleStart > angleRight && angleStart < angleLeft) || (angleRight > angleLeft && (angleStart > angleRight || angleStart < angleLeft));
    boolean seeEnd = (angleRight < angleLeft && angleEnd > angleRight && angleEnd < angleLeft) || (angleRight > angleLeft && (angleEnd > angleRight || angleEnd < angleLeft));

    if(seeStart && seeEnd){
      if (Utils.areEqual(seg.getStart(),seg.getEnd()))
        return null;
      
      return seg;
    }

    Point interRight = fovRight.SegmentIntersect(seg);
    Point interLeft = fovLeft.SegmentIntersect(seg);

    if(seeStart){
      if (interRight == null)
        return new Segment(seg.getStart(),interLeft,seg.getColor());

      return new Segment(seg.getStart(),interRight,seg.getColor());
    } 

    if(seeEnd){
      if (interRight == null)
        return new Segment(seg.getEnd(),interLeft,seg.getColor());

      return new Segment(seg.getEnd(),interRight,seg.getColor());
    }

    if (interRight != null && interLeft != null)
      return new Segment(interLeft,interRight,seg.getColor());

    return null;
  }

  /**
   * Define the angle in the trigonometrical circle with the eye's position as center 
   */
  public double anglePoint(Point point){
    Segment seg = new Segment(point,this.position);
    if (seg.isVertical()){
      if (point.y > this.position.y){
        return 90;
      }
      else{
        return 270;
      }
    }

    double degree =  Math.toDegrees(Math.atan(seg.getLine().slope));
    if(point.x < this.position.x){
      return 180 + degree;
    }
    return degree;
    
  }

}
