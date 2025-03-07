package shared;

import java.util.ArrayList;
import java.util.Arrays;
import shared.generation.*;
import java.lang.Math;

public class BSP {
 private Node head;

  static class Node {
    public ArrayList<Segment> data;
    public BSP leftSon;
    public BSP rightSon;
    public boolean isLeaf;

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
        if(leftSon != null){s += leftSon.getHead();}
      s += "\nright"; 
        if(rightSon != null){s += rightSon.getHead();}
      return s;
    }
  }

  public BSP(ArrayList<Segment> data, GenerationMethod generationMethod){
    if (data.size() == 0 || data.get(0)==null){
      this.head = null;
    } else if(data.size() == 1){
      this.head = new Node(data,true);
    } else{
      Segment segment = generationMethod.getSegment(data);
      ArrayList<ArrayList<Segment>> locSegment = segment.generateNode(data);

      this.head = new Node(locSegment.get(0),true);
      BSP leftSon = new BSP(locSegment.get(1),generationMethod);
      BSP rightSon = new BSP(locSegment.get(2),generationMethod);

      if(!(leftSon.equals(null)) || !(rightSon.equals(null))){
        this.head.isLeaf = false;
      }

      this.head.leftSon = leftSon;
      this.head.rightSon = rightSon;
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
    int eyePos = this.head.data.get(0).locationPoint(p.getPos());
    LocationEnum vision = p.seeNode(this.head.data.get(0));

    EyeSegment eyeSegRight = (vision == LocationEnum.HPLUS || vision == LocationEnum.ALIGNED) ? this.head.rightSon.painterAlgorithm(p,range) : null;
    EyeSegment eyeSegLeft = (vision == LocationEnum.HMINUS || vision == LocationEnum.ALIGNED) ? this.head.leftSon.painterAlgorithm(p,range) : null;


    if(eyePos == -1){
      EyeSegment eyeSegHead = new EyeSegment(eyeSeg);
      if(vision == LocationEnum.HPLUS || vision == LocationEnum.ALIGNED){
        eyeSegRight.mergeParts(eyeSegHead);
        eyeSegRight.mergeParts(eyeSegLeft);
        return eyeSegRight;}

      eyeSegHead.mergeParts(eyeSegLeft);
      return eyeSegHead;
    } 
    else if(eyePos == 1){
      EyeSegment eyeSegHead = new EyeSegment(eyeSeg);

      if(vision == LocationEnum.HMINUS || vision == LocationEnum.ALIGNED){
        eyeSegLeft.mergeParts(eyeSegHead);
        eyeSegLeft.mergeParts(eyeSegRight);
        return eyeSegLeft;}

      eyeSegHead.mergeParts(eyeSegRight);
      return eyeSegHead;
    }
    else{
      if (vision == LocationEnum.ALIGNED){
        eyeSegRight.mergeParts(eyeSegLeft);
        return eyeSegRight;
      }
      if(vision == LocationEnum.HPLUS){
        return eyeSegRight;
      }
      return eyeSegLeft;
    }
  }
  
  public ArrayList<Segment> drawNode(Eye p,ArrayList<Segment> data, double[] range){
    ArrayList<Segment> proj = new ArrayList<>();
    double pAngle = Math.toRadians(p.getAngle());
    double distance = Math.sqrt(Math.pow(range[1],2)+Math.pow(range[0],2));
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

    Segment line = new Segment(point1,point2,"red");  

    for (int i = 0; i < data.size(); i++){
      Segment segment = data.get(i);
      Segment seg = p.seenSegment(segment);
      if(seg != null){
        Segment seg1 = new Segment(p.getPos(),seg.getStart(),"red");
        Segment seg2 = new Segment(p.getPos(),seg.getEnd(),"red");
        Point inter1 = seg1.interSeg(line);
        Point inter2 = seg2.interSeg(line);

        if (inter1 != null && inter2 != null && !Utils.areEqual(inter1,inter2)){
          Segment inter = new Segment(inter1,inter2,seg.getColor());
          proj.add(inter); 
        }
      }
    };
    return proj;
    }
}
