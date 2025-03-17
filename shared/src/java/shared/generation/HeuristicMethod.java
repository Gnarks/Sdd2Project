package shared.generation;

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

        if (seg1 != seg2 && seg1.onSeg(seg1.interSeg(seg2))){
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
}
