package shared;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class BSP {
 private Node head;

  static class Node {
    public ArrayList<Segment> data;
    public Node leftSon;
    public Node rightSon;
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
      s +="\nleft" + leftSon;
      s += "\nright" + rightSon; 
      
      return s;
    }
  }

  public BSP(ArrayList<Segment> data,int method){
    if (data.size() == 0){this.head = null;}
    else if(data.size() == 1){
      this.head = new Node(data,true);
    } else{

      Segment segment = chosedSegment(data, method);
      ArrayList<ArrayList<Segment>> locSegment = segment.locationSegment(data);

      this.head = new Node(locSegment.get(0),false);
      BSP leftSon = new BSP(locSegment.get(1),method);
      BSP rightSon = new BSP(locSegment.get(2),method);
      this.head.leftSon = leftSon.head;
      this.head.rightSon = rightSon.head;
    }
  }

  public Node getHead(){
    return this.head;
  }
  public Segment chosedSegment(ArrayList<Segment> data, int method){
    switch (method) {
      case 0:
        Random rand = new Random();
        int random = rand.nextInt(data.size());
        return data.get(random);

      case 1:
        return data.get(0);
      default:
        return null;
    }
  }

}
