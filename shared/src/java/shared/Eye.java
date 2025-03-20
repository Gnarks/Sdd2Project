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


  /** returns the the line's partition seen by the eye 
 * @param line partitionning the space
 * @return the partition of the line seen by the eye
   */
  public PartitionEnum getSeenPartition(Line line) {
    double angleRadians = Math.toRadians(this.angle);
    double angleLeft = angle+fov;
    double angleRight = angle-fov;

    PartitionEnum loc = line.relativePosition(this.position);
    double alpha = Math.toDegrees(Math.atan(line.slope)); 
    double limit = 180+alpha;

    if(loc == PartitionEnum.HPLUS){
      if(line.isVertical){
        if(Math.cos(angleRadians) < 0){
          return PartitionEnum.BOTH;
        }
        return PartitionEnum.HPLUS;
      }
      if (line.slope  >= 0 && (this.angle > limit && ((angleRight)%360 >= limit || angleLeft <= alpha+360))){
        return PartitionEnum.HPLUS;
      }  
      if (line.slope < 0 && (this.angle <= limit && ((angleLeft)%360 <= limit || angleRight >= alpha))){
        return PartitionEnum.HPLUS;
      } 
    }
    if (loc == PartitionEnum.HMINUS){
      if(line.isVertical){
        if(Math.cos(angleRadians) > 0){
          return PartitionEnum.BOTH;
        }
        return PartitionEnum.HMINUS;
      }
      if(line.slope>=0 && (angle <= limit && ((angleLeft)%360 <= limit || angleRight >= alpha))){
        return PartitionEnum.HMINUS;
      } 
      if (line.slope < 0 && (angle > limit && ((angleRight)%360 >= limit || angleLeft <= alpha+360))){
        return PartitionEnum.HMINUS;
      } 
    }
    return PartitionEnum.BOTH;

  }

  /** returns the part of the segment seen by the eye
 * @param seg the segment to look at 
 * @param distance the maximum distance the segment can be from the eye
 * @return the seen segment part from the eye
   */
  public Segment seenSegmentPart(Segment seg,double distance){
  
    double angleRight = (angle-fov+360)%360;
    double angleLeft = (angle+fov)%360;
    
    double hypo = Math.sqrt(Math.pow(Math.tan(Math.toRadians(fov))*distance,2)+Math.pow(distance,2));

    Segment fovLeft = new Segment(this.position.x,this.position.y,this.position.x+hypo*Math.cos(Math.toRadians(angleLeft)),this.position.y+hypo*Math.sin(Math.toRadians(angleLeft)),seg.getColor());
    Segment fovRight =new Segment(this.position.x,this.position.y,this.position.x+hypo*Math.cos(Math.toRadians(angleRight)),this.position.y+hypo*Math.sin(Math.toRadians(angleRight)),seg.getColor());

    double angleStart = (getAnglePoint(seg.getStart())+360)%360;
    double angleEnd = (getAnglePoint(seg.getEnd())+360)%360;

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

  /** return the angle in the trigonometrical circle with the eye's position as center 
 * @param point the point to get the angle from
 * @return the angle formed by the point relative to the eye's position as the center
   */
  private double getAnglePoint(Point point){
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
