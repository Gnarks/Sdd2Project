package shared.geometrical;

import shared.DoubleUtils;

public class Line {
  public double intercept;
  public double slope;
  public boolean isVertical;

  public Line(Point start, Point end) {
    if (start.x == end.x){
      this.isVertical = true;
      slope = end.x;
      intercept = 0;
    }
    else{
      slope = (end.y - start.y)/(end.x-start.x);
      intercept = start.y - (slope*start.x);
      this.isVertical = false;
    }
  }

  /** Returns the intersection between the line and the specified segment
 * @param seg the segment to intersect the line with
 * @return the point of intersection or null if there isn't any intersection
   */
  public Point intersect(Segment seg){
    if ((isVertical && seg.isVertical()) || (!(isVertical || seg.isVertical()) && DoubleUtils.areEqual(seg.getLine().slope, this.slope))){
      return null;
    }
    if (this.isVertical){
      double x = slope;
      return new Point(x, seg.getLine().slope*x+seg.getLine().intercept);
    }
    if(seg.isVertical()){
      double x = seg.getLine().slope;
      return new Point(x, this.slope*x+this.intercept);
    }
    double x = (seg.getLine().intercept - this.intercept)/(this.slope-seg.getLine().slope);
    double y = this.slope*x + this.intercept;
    return new Point(x,y);
  }

  /** Function to define if a point is in h-, h+ or belongs to the line
  * @param Point to analyse
  * @return A PartitionEnum telling the relative position of the point relative to the line
  */
  public PartitionEnum relativePosition(Point point){

    if(isVertical){
      if(Double.compare(point.x, slope) == 0){
        return PartitionEnum.BOTH;
      } else if(point.x > slope){
        return PartitionEnum.HPLUS;
      } else {
      return PartitionEnum.HMINUS;
      }
    }
    if(slope == 0){
      if(DoubleUtils.areEqual(point.y, intercept)){
        return PartitionEnum.BOTH;
      } else if(point.y < intercept){
        return PartitionEnum.HPLUS;
      } else {
        return PartitionEnum.HMINUS;
      }
    }
    double x = (point.y - intercept)/slope;
    if (DoubleUtils.areEqual(point.x,x)){
      return PartitionEnum.BOTH;
    } else if (point.x < x){
      return PartitionEnum.HMINUS;
    } else {
      return PartitionEnum.HPLUS;
    }

  }
}
