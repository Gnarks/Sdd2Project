package shared.scene;

import java.util.ArrayList;

import shared.*;

public class Scene{
  private ArrayList<Segment> segList;
  private Point topLeft;
  private Point topRight;
  private Point bottomLeft;
  private Point bottomRight;

  public Scene( ArrayList<Segment> segList, Point topLeft, Point topRight, Point bottomLeft, Point bottomRight){
    this.segList = segList;
    this.topRight = topRight;
    this.topLeft = topLeft;
    this.bottomLeft = bottomLeft;
    this.bottomRight = bottomRight;
  }

  public Scene( ArrayList<Segment> segList){
    this.segList = segList;
    this.topRight = null;
    this.topLeft = null;
    this.bottomLeft = null;
    this.bottomRight = null;
  }
  
  /**
   * @return the corners of the scene in order : {topLeft, topRight, bottomLeft, bottomRight}
   */
  public Point[] getCorners(){
    Point[] corners = {topLeft.copy(), topRight.copy(), bottomLeft.copy(), bottomRight.copy()};
    return corners;
  }

  public Point getRange(){
    return bottomRight.copy();
  }
  
  public ArrayList<Segment> getSegList(){
    return segList;
  }
}

