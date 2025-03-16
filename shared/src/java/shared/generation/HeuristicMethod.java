package shared.generation;

import shared.Point;
import java.util.ArrayList;
import shared.Segment;

/**
 * Heuristique H1
 * Auteurs: * Détermination de la partition: on choisit un segment s qui maximise g_s
 *
 * Heuristic H1 (see Heuristique.pdf)
 * Authors:  Krishnaswamy, Alijani, Su 
 * Method selecting the segment beeing intersected the most by all other segment's lines
 */
public class HeuristicMethod extends GenerationMethod{

  /**
   * @return the most intersected segment in the list
   */
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
  /**
   * returns if the point p is on the segment seg
 * @param p the point to check
 * @param seg the segment on which the test is issued
 * @return if the point p is on the segement seg
   */
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
