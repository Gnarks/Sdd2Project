package shared.geometrical;

import shared.Utils;

public class Segment {
  private Point start;
  private Point end;
  private String color;
  private Line line;
  

  public Segment(Point start,Point end,String color){
    if (start.x <= end.x){
      this.start = start;
      this.end = end;
    } else {
      this.start = end;
      this.end = start;}
    this.color = color;
    this.line = new Line(this.start, this.end);
  }

  public Segment(Point start,Point end){
    this(start,end, null);
  }

  public Segment(double x1, double y1, double x2, double y2, String color){
    Point _start = new Point(x1,y1);
    Point _end = new Point(x2,y2);
    if (_start.x < _end.x){
      this.start = _start;
      this.end = _end;
    } else {
      this.start = _end;
      this.end = _start;}
    this.color = color;
    this.line = new Line(this.start, this.end);
  }

  public Point getStart(){
    return this.start;
  }
  public Point getEnd(){
    return this.end;
  }
  public String getColor(){
    return this.color;
  }

  public Line getLine(){
    return this.line;
  }

  public boolean isVertical(){
    return this.line.isVertical;
  }

  public void setStart(Point start){
    this.start = start;
    this.line = new Line(this.start, this.end);
  }

  public void setEnd(Point end){
    this.end = end;
    this.line = new Line(this.start, this.end);
  }

  public void setColor(String color){
    this.color = color;
  }

  /**
   * returns if the point is on the segment 
 * @param point the point to check
 * @return if the point p is on the segement
   */
  public boolean onSeg(Point point){
    if (point == null)
      return false;

    if(this.isVertical())
      return (Utils.areEqual(point.x, this.line.slope)) && Math.min(start.y,end.y) <= point.y && point.y <= Math.max(start.y,end.y); 
    
    return Utils.areEqual(point.y, this.line.slope*point.x + this.line.intercept) && (start.x <= point.x && point.x <= end.x);
  }

  /** Function to define if a point is in h-, h+ or belongs to the line
  * @param Point to analyse
  * @return A PartitionEnum telling the relative position of the point relative to the line
  */
  public PartitionEnum relativePosition(Point point){
    return line.relativePosition(point);
  }
  
  /** Returns the intersection between the segment's line and the specified segment
 * @param seg the segment to intersect the line with
 * @return the point of intersection or null if there isn't any intersection
   */
  public Point lineIntersect(Segment seg){
    return line.intersect(seg);
  }

  /** Returns the intersection between two segments
 * @param seg the segment to intersect with
 * @return the point of intersection or null if there isn't any intersection
   */
  public Point SegmentIntersect(Segment seg){
    Point inter = lineIntersect(seg);
    if (onSeg(inter) && seg.onSeg(inter))
      return inter;

    return null;
  }
  public String toString(){
    return "Start: "+ start + " | End: "+ end + " | Color: " + color;
  }
}
