package shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;
import java.awt.Color;

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
      s += isLeaf;

      s +="\nleft";
        if(leftSon != null){s += leftSon.getHead();}
      s += "\nright"; 
        if(rightSon != null){s += rightSon.getHead();}
      return s;
    }
  }

  public BSP(ArrayList<Segment> data, GenerationMethod generationMethod){
    if (data.size() == 0){this.head = null;}
    else if(data.size() == 1){
      this.head = new Node(data,true);
    } else{

      Segment segment = generationMethod.getSegment(data);
      // pourquoi arrayList ? c'est obli taille 2 ?
      // le nom generateNode est pas clair
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
    if(this.head.isLeaf){
      ArrayList<Segment> eyeSeg = new ArrayList<Segment>(Arrays.asList(drawNode(p,this.head.data,range)));
      sortSegments(eyeSeg);
      return new EyeSegment(eyeSeg);
    }
    int eyePos = this.head.data.get(0).locationPoint(p.getPosition());
    int vision = p.seeNode(this.head.data.get(0));
    EyeSegment eyeSegRight = null;
    EyeSegment eyeSegLeft = null;
    if(vision == -1 || vision == 0){
    eyeSegRight = this.head.rightSon.painterAlgorithm(p,range);}
    
  
    if(vision == 1 || vision == 0){
    eyeSegLeft = this.head.leftSon.painterAlgorithm(p,range);}

    ArrayList<Segment> eyeSeg = new ArrayList<Segment>(Arrays.asList(drawNode(p,this.head.data,range)));
    sortSegments(eyeSeg);
    
    if(eyePos == -1){
      EyeSegment eyeSegHead = new EyeSegment(eyeSeg);
      if(vision == 1 || vision == 0){
      eyeSegRight.mergeParts(eyeSegHead);
      sortSegments(eyeSegLeft.getParts());
      eyeSegRight.mergeParts(eyeSegLeft);
      
      return eyeSegRight;}

      eyeSegHead.mergeParts(eyeSegLeft);
      return eyeSegHead;
    }
    else if(eyePos == 1){
      EyeSegment eyeSegHead = new EyeSegment(eyeSeg);
      if(vision == -1 || vision == 0){
        eyeSegLeft.mergeParts(eyeSegHead);
        sortSegments(eyeSegLeft.getParts());
        eyeSegLeft.mergeParts(eyeSegRight);
      
      return eyeSegLeft;}

      eyeSegHead.mergeParts(eyeSegRight);
      return eyeSegHead;

    }

    else{
      if (vision == 0){
        eyeSegRight.mergeParts(eyeSegLeft);
        return eyeSegRight;
      }
      if(vision == 1){
        return eyeSegRight;
      }
      return eyeSegLeft;
    }
  }
  
  public Segment[] drawNode(Eye p,ArrayList<Segment> data, double[] range){
    Segment[] proj = new Segment[data.size()];
    double distance = Math.sqrt(Math.pow(range[1],2)+Math.pow(range[0],2));
    double x = distance*Math.cos(Math.toRadians(p.getAngle()));
    double y = distance*Math.sin(Math.toRadians(p.getAngle()));
    Point point1 = new Point(x,y);
    Point point2;

    if(p.getAngle()==270 || p.getAngle()==90){
      point2  = new Point(x+1,y);
    } else {
      point2 = new Point(x+1,y-(1/Math.tan(Math.toRadians(p.getAngle()))));
    }

    Segment line = new Segment(point1,point2,Color.RED);  

    for (int i = 0; i < data.size(); i++){
      Segment segment = data.get(i);
      Segment seg = p.seeSegment(segment);
      if(seg != null){
      Segment seg1 = new Segment(p.getPosition(),seg.getStart(),Color.RED);
      Segment seg2 = new Segment(p.getPosition(),seg.getEnd(),Color.RED);
      Point inter1 = seg1.interSeg(line);
      Point inter2 = seg2.interSeg(line);

      proj[i] = new Segment(inter1,inter2,seg.getColor());
      }
    };
    return proj;
    }

  public void sortSegments(ArrayList<Segment> seg){ 
      seg.sort((a,b)->{
        if(a.getStart().x < b.getStart().x){
          return -1;
        } else {
          return 1;
        }
      });
  }



}
