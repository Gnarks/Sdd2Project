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
    double angleLeft = (angle+fov)%360;
    double angleRight = (angle-fov+360)%360;

    PartitionEnum loc = line.relativePosition(this.position);
    double alpha = (Math.toDegrees(Math.atan(line.slope))+360)%360; 
    double limit = (180+alpha)%360;

    if(loc == PartitionEnum.HPLUS){
      if(line.isVertical){
        if(Math.cos(angleRadians) < 0){
          return PartitionEnum.BOTH;
        }
        return PartitionEnum.HPLUS;
      }
      if (line.slope  >= 0 && (this.angle > limit || this.angle < alpha) && (angleRight >= limit || angleRight <= alpha) && (angleLeft >= limit || angleLeft <= alpha)){
        return PartitionEnum.HPLUS;
      }  
      if (line.slope < 0&& (this.angle < limit || this.angle > alpha) && (angleRight <= limit || angleRight >= alpha) && (angleLeft < limit || angleLeft >= alpha)){
        return PartitionEnum.HPLUS;
      } 
    }
    else if (loc == PartitionEnum.HMINUS){
      if(line.isVertical){
        if(Math.cos(angleRadians) > 0){
          return PartitionEnum.BOTH;
        }
        return PartitionEnum.HMINUS;
      }
      if(line.slope>=0 && (angle > alpha && angle < limit && (angleLeft <= limit && angleRight >= alpha))){
        return PartitionEnum.HMINUS;
      } 
      if (line.slope < 0 && (angle > limit && angle < alpha && (angleRight >= limit && angleLeft <= alpha))){
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

    double angleStart = newGetAnglePoint(seg.getStart());
    double angleEnd = newGetAnglePoint(seg.getEnd());

    boolean seeStart = (angleRight < angleLeft && angleStart > angleRight && angleStart < angleLeft) || (angleRight > angleLeft && (angleStart > angleRight || angleStart < angleLeft));

    boolean seeEnd = (angleRight < angleLeft && angleEnd > angleRight && angleEnd < angleLeft) || (angleRight > angleLeft && (angleEnd > angleRight || angleEnd < angleLeft));

    if(seeStart && seeEnd){
      if (seg.getStart().equals(seg.getEnd()))
        return null;
      
      return seg;
    }

    Point interRight = fovRight.segmentIntersect(seg);
    Point interLeft = fovLeft.segmentIntersect(seg);

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
      return ((180 + degree) +360)%360;
    }
    return (degree +360) %360;
  }

  /** return the angle in the trigonometrical circle with the eye's position as center 
 * @param point the point to get the angle from
 * @return the angle formed by the point relative to the eye's position as the center
   */
  private double newGetAnglePoint(Point point){
    return (Math.toDegrees(Math.atan2(point.y, point.x)) +360 )%360;
  }


}
