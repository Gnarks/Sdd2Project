package shared;

import java.util.ArrayList;
import shared.generation.*;
import shared.geometrical.*;
import java.lang.Math;

public class BSP {

    public ArrayList<Segment> data;
    public BSP leftSon = null;
    public BSP rightSon = null;
    public int height;
    public int size;


  public BSP(ArrayList<Segment> data, GenerationMethod generationMethod){
    if (data.size() == 0 || data.get(0)==null){
      return;
    } 
    Segment selectedSegment = generationMethod.getSegment(data);

    PartitionedSpace partitionedSpace = partitionWith(data, selectedSegment.getLine());
    this.data = partitionedSpace.align;

    //left = minus
    if (partitionedSpace.dMinus.size() != 0)
      leftSon = new BSP(partitionedSpace.dMinus, generationMethod);

    if (partitionedSpace.dPlus.size() != 0)
      rightSon = new BSP(partitionedSpace.dPlus, generationMethod);


    int maxHeight = 0;
    size = 1;
    if (leftSon != null){
      maxHeight = leftSon.height; 
      size += leftSon.size;
    }
    if (rightSon != null){
      maxHeight = Math.max(rightSon.height,maxHeight); 
      size += rightSon.size;
    }
    height = maxHeight + 1;
  }


  /** 
   * @return if the BSP tree only contains data and has no sons
   */
  public boolean isLeaf(){
    return leftSon==null && rightSon==null;
  }

  /** apply the painter's algorithm to get a projection of the eye
 * @param eye the eye looking at the BSP tree
 * @param range the scene range
 * @return the Projection of the BSP tree by the Eye
   */
  public Projection painterAlgorithm(Eye eye, Point range){
    
    Projection lineDataProjection = getDataProjection(eye, range);
    if(isLeaf()){
      return lineDataProjection;
    }

    PartitionEnum eyePos = data.get(0).relativePosition(eye.getPos());
    PartitionEnum vision = eye.getSeenPartition(data.get(0).getLine());

    // init the left and right projections as empty
    Projection rightProjection = (rightSon != null && (vision == PartitionEnum.HPLUS||vision == PartitionEnum.BOTH)) ? rightSon.painterAlgorithm(eye, range) : new Projection(new ArrayList<Segment>());
    Projection leftProjection = (leftSon != null && (vision == PartitionEnum.HMINUS||vision == PartitionEnum.BOTH)) ? leftSon.painterAlgorithm(eye, range) : new Projection(new ArrayList<Segment>());

    if(eyePos == PartitionEnum.HMINUS){
      if (vision == PartitionEnum.BOTH){
        rightProjection.mergePartsFrom(lineDataProjection);
        rightProjection.mergePartsFrom(leftProjection);
        return rightProjection;}
      leftProjection.mergePartsFrom(lineDataProjection);
      return leftProjection;
    }
    else if(eyePos == PartitionEnum.HPLUS){
      if (vision == PartitionEnum.BOTH){
        leftProjection.mergePartsFrom(lineDataProjection);
        leftProjection.mergePartsFrom(rightProjection);
        return leftProjection;
      }
      rightProjection.mergePartsFrom(lineDataProjection);
      return rightProjection;
    }
    else{
      if (vision == PartitionEnum.BOTH){
        rightProjection.mergePartsFrom(leftProjection);
        return rightProjection;
      }
      if(vision == PartitionEnum.HPLUS){
        return rightProjection;
      }
      return leftProjection;
    }
  }
  
  /** Returns the eye view (Projection) of the data contained in the current BSP
 * @param eye the eye to do the projection with
 * @param range the range of the scene 
 * @return the projection of the data stored in the current bsp seen by the eye
   */
  private Projection getDataProjection(Eye eye, Point range){
    ArrayList<Segment> proj = new ArrayList<>();  

    double eyeAngle = Math.toRadians(eye.getAngle());
    double distance = Math.sqrt(Math.pow(range.y *2,2)+Math.pow(range.x*2,2));

    double x = distance*Math.cos(eyeAngle);
    double y = distance*Math.sin(eyeAngle);
    Point point1 = new Point(x,y);
    Point point2;

    if(DoubleUtils.areEqual(Math.cos(eyeAngle),0)){
      point2  = new Point(x+1,y);
    } else if (DoubleUtils.areEqual(Math.sin(eyeAngle), 0)) {
        point2 = new Point(x,y+1);
    } else {
      point2 = new Point(x+1,y-(1/Math.tan(eyeAngle)));
    }

    Segment line = new Segment(point1,point2);  

    for (int i = 0; i < data.size(); i++){
      Segment segment = data.get(i);
      Segment seg = eye.seenSegmentPart(segment,distance);
      if(seg != null){
        Segment seg1 = new Segment(eye.getPos(),seg.getStart());
        Segment seg2 = new Segment(eye.getPos(),seg.getEnd());
        Point inter1 = seg1.lineIntersect(line);
        Point inter2 = seg2.lineIntersect(line);
        if (inter1 != null && inter2 != null && !inter1.equals(inter2)){
          Segment inter = new Segment(inter1,inter2,seg.getColor());
          proj.add(inter); 
        }
      }
    }
    return new Projection(proj);
    }



  /** Partitions the space with the specified Line
 * @param data the space represented by the list of segments to separate
 * @param divider the line to partition the space with
 * @return a PartitionedSpace refering to the partition made by the line
   */
  private PartitionedSpace partitionWith(ArrayList<Segment> data, Line divider){
    ArrayList<Segment> align = new ArrayList<>();
    ArrayList<Segment> d_minus = new ArrayList<>();
    ArrayList<Segment> d_plus = new ArrayList<>();

    data.forEach(seg-> {
      if (seg.getLine() == divider){
        align.add(seg);
      }
      else {
        Point inter = divider.intersect(seg); 
        PartitionEnum locationStart= divider.relativePosition(seg.getStart());
        PartitionEnum locationEnd = divider.relativePosition(seg.getEnd());
        if (locationStart == locationEnd || locationStart == PartitionEnum.BOTH || locationEnd == PartitionEnum.BOTH){
          switch (locationStart) {
            case PartitionEnum.HMINUS:
            d_minus.add(seg);
            break;
            case PartitionEnum.HPLUS:
            d_plus.add(seg);
            break;
            default:
            if (locationEnd == PartitionEnum.BOTH){
              align.add(seg);
            }
            else if (locationEnd == PartitionEnum.HPLUS){
              d_plus.add(seg);
            }
            else{
              d_minus.add(seg);
            }
            break;
          }
        }

        else{
          Segment startSeg = new Segment(seg.getStart(),inter,seg.getColor());
          Segment endSeg = new Segment(inter,seg.getEnd(),seg.getColor());

          if (locationStart == PartitionEnum.HMINUS){ d_minus.add(startSeg);} else { d_plus.add(startSeg);}
          if (locationEnd == PartitionEnum.HMINUS){ d_minus.add(endSeg);} else { d_plus.add(endSeg);}
        }
      }
    });
    return new PartitionedSpace(align,d_minus,d_plus);
  }

  public String toString(){
    String s = data.toString();
    s +="\nleft";
    if(leftSon != null){s += leftSon;}
    s += "\nright"; 
    if(rightSon != null){s += rightSon; }
    return s;
  }
}
