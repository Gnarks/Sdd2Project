package shared;

public class BSP {
 Node head;

  static class Node {
    public Segment[] data;
    public Node leftSon;
    public Node rightSon;
    public boolean isLeaf;

    public Node(Segment[] data,boolean isLeaf){
      this.data = data;
      this.leftSon = null;
      this.rightSon = null;
      this.isLeaf = isLeaf;
    }
  }
}
