package shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import shared.generation.*;
import shared.geometrical.*;
import java.lang.Math;

public class BSP {
 private Node head;

  public static class Node {
    public ArrayList<Segment> data;
    public BSP leftSon;
    public BSP rightSon;
    public boolean isLeaf;
    public int height;
    public int size;

    public Node(Segment[] data,boolean isLeaf){
      this.data = new ArrayList<Segment>(Arrays.asList(data));
      this.leftSon = null;
      this.rightSon = null;
      this.isLeaf = isLeaf;
    }

    public Node(ArrayList<Segment> data, boolean isLeaf){
      this.data = data;
      this.leftSon = null;
      this.rightSon = null;
      this.isLeaf = isLeaf;
    }

    public String toString(){
      String s = data.toString();
      s +="\nleft";
        if(leftSon != null){s += leftSon.getHead(); }
      s += "\nright"; 
        if(rightSon != null){s += rightSon.getHead(); }
      return s;
    }
  }

  public BSP(ArrayList<Segment> data, GenerationMethod generationMethod){
    if (data.size() == 0 || data.get(0)==null){
      this.head = null;
    } else if(data.size() == 1){
      this.head = new Node(data,true);
      this.head.height = 1;
      this.head.size = 1;
    } else{
      Segment segment = generationMethod.getSegment(data);
      ArrayList<ArrayList<Segment>> locSegment = generateNode(data, segment.getLine());

      this.head = new Node(locSegment.get(0),true);
      BSP leftSon = new BSP(locSegment.get(1),generationMethod);
      BSP rightSon = new BSP(locSegment.get(2),generationMethod);

      if(!(leftSon.equals(null)) || !(rightSon.equals(null))){
        this.head.isLeaf = false;
      }

      this.head.leftSon = leftSon;
      this.head.rightSon = rightSon;
      int maxHeight = 0;
      this.head.size = 1;
      if (leftSon != null && leftSon.getHead() != null){
        maxHeight = leftSon.getHead().height; 
        this.head.size += leftSon.getHead().size;
      }
      if (rightSon != null && rightSon.getHead() != null){
        maxHeight = Math.max(rightSon.getHead().height,maxHeight); 
        this.head.size += rightSon.getHead().size;
      }
      this.head.height = maxHeight + 1;

    }
  }

  public Node getHead(){
    return this.head;
  }

  public EyeSegment painterAlgorithm(Eye p,double[] range){
    
    if(this.head == null){
      return new EyeSegment(new ArrayList<Segment>());
    }

    ArrayList<Segment> eyeSeg = drawNode(p,this.head.data,range);
    Utils.sortSegments(eyeSeg);

    if(this.head.isLeaf){
      return new EyeSegment(eyeSeg);
    }

    PartitionEnum eyePos = this.head.data.get(0).relativePosition(p.getPos());
    VisionEnum vision = p.seeNode(this.head.data.get(0));
    
    EyeSegment eyeSegHead = new EyeSegment(eyeSeg);
    EyeSegment eyeSegRight =  this.head.rightSon.painterAlgorithm(p,range);
    EyeSegment eyeSegLeft =  this.head.leftSon.painterAlgorithm(p,range);
    

    if(eyePos == PartitionEnum.HMINUS){
        eyeSegRight.mergeParts(eyeSegHead);
        eyeSegRight.mergeParts(eyeSegLeft);
        return eyeSegRight;}

     
    else if(eyePos == PartitionEnum.HPLUS){
      eyeSegLeft.mergeParts(eyeSegHead);
      eyeSegLeft.mergeParts(eyeSegRight);
      return eyeSegLeft;
    }
    else{
      if (vision == VisionEnum.BOTH){
        eyeSegRight.mergeParts(eyeSegLeft);
        return eyeSegRight;
      }
      if(vision == VisionEnum.HPLUS){
        return eyeSegRight;
      }
      return eyeSegLeft;
    }
  }
  
  public ArrayList<Segment> drawNode(Eye p,ArrayList<Segment> data, double[] range){
    ArrayList<Segment> proj = new ArrayList<>();  

    double pAngle = Math.toRadians(p.getAngle());
    double distance = 2* Math.sqrt(Math.pow(range[1],2)+Math.pow(range[0],2));

    double x = distance*Math.cos(Math.toRadians(pAngle));
    double y = distance*Math.sin(Math.toRadians(pAngle));
    Point point1 = new Point(x,y);
    Point point2;

    if(Utils.areEqual(Math.cos(pAngle),0)){
      point2  = new Point(x+1,y);
    } else if (Utils.areEqual(Math.sin(pAngle), 0)) {
        point2 = new Point(x,y+1);
    } else {
      point2 = new Point(x+1,y-(1/Math.tan(pAngle)));
    }

    Segment line = new Segment(point1,point2);  

    for (int i = 0; i < data.size(); i++){
      Segment segment = data.get(i);
      Segment seg = p.seenSegment(segment,distance,line);
      if(seg != null){
        Segment seg1 = new Segment(p.getPos(),seg.getStart());
        Segment seg2 = new Segment(p.getPos(),seg.getEnd());
        Point inter1 = seg1.lineIntersect(line);
        Point inter2 = seg2.lineIntersect(line);
        if (inter1 != null && inter2 != null && !Utils.areEqual(inter1,inter2)){
          Segment inter = new Segment(inter1,inter2,seg.getColor());
          proj.add(inter); 
        }
      }
    }
    return proj;
    }



  /**
   * align :  the segments that are aligned with the current segment
   * d_minus : the segments that are on the d- part of the segment
   * d_plus : the segments that are on the d+ part of the segment
   */
  private ArrayList<ArrayList<Segment>> generateNode(ArrayList<Segment> data, Line divider){
    ArrayList<Segment> align = new ArrayList<>();
    ArrayList<Segment> d_minus = new ArrayList<>();
    ArrayList<Segment> d_plus = new ArrayList<>();

    data.forEach(seg-> {
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
    });
    return new ArrayList<>(List.of(align,d_minus,d_plus));
  }

}
