package shared.generation;

import shared.Point;
import java.util.ArrayList;

import shared.Segment;

public class HeuristicMethod extends GenerationMethod{


  public Segment getSegment(ArrayList<Segment> data ){

    int bestG =0;
    Segment bestSegment = null;

    for (Segment seg1 : data) {
      int currG = 0;
      for (Segment seg2 : data) {

        if (seg1 != seg2 && pointOnSegment(seg1.interSeg(seg2), seg1)){
          currG++;
        }
        if (currG >= bestG){
          bestG = currG;
          bestSegment = seg1;
        }
      }
    }
    return bestSegment;
  }

  private static boolean pointOnSegment(Point p, Segment seg){
    if (p == null)
    return false;
    boolean xReversed = seg.getStart().x > seg.getEnd().x;
    boolean yReversed = seg.getStart().y > seg.getEnd().y;
    boolean sameSlope = (seg.getEnd().x - seg.getStart().x)* (p.y - seg.getStart().y) == (p.x - seg.getStart().x)* (seg.getEnd().y - seg.getStart().y);

    boolean xOk = ( seg.getStart().x <= p.x && p.x <= seg.getEnd().x) || (xReversed && (( seg.getEnd().x <= p.x && p.x <= seg.getStart().x )));
    boolean yOk = ( seg.getStart().y <= p.y && p.y <= seg.getEnd().y) || (yReversed && (( seg.getEnd().y <= p.y && p.y <= seg.getStart().y )));

    return xOk && yOk && sameSlope;
  }
}
