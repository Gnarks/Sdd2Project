package shared.scene;

import java.util.ArrayList;
import shared.geometrical.*;

/** Object representing a 2D scene loaded from example files
 */
public class Scene{
  private ArrayList<Segment> segList;
  private Point range;

  public Scene( ArrayList<Segment> segList, Point range){
    this.segList = segList;
    this.range = range;
  }

  public Scene( ArrayList<Segment> segList){
    this.segList = segList;
    this.range = null;
  }
  
  /**
   * @return the range of the scene
   * */
  public Point getRange(){
    return range.copy();
  }
  
  public ArrayList<Segment> getSegList(){
    return segList;
  }
}

