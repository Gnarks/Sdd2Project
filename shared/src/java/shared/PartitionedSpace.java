package shared;

import java.util.ArrayList;
import shared.geometrical.Segment;

public class PartitionedSpace {
  public ArrayList<Segment> align;
  public ArrayList<Segment> dMinus;
  public ArrayList<Segment> dPlus;
  
  public PartitionedSpace(ArrayList<Segment> align,ArrayList<Segment> dMinus, ArrayList<Segment> dPlus ) {
    this.align = align;
    this.dMinus = dMinus;
    this.dPlus = dPlus;
  }
}
