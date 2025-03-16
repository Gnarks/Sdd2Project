package shared.generation;

import java.util.ArrayList;
import shared.*;

/**
 * General class to qualify a generation method
 * represents the method to construct a Binary Space Partitionning tree
 * @see BSP
 */
public abstract class GenerationMethod{


  /** Converts a GenerationEnum to a new generation method instance
   * @param generationEnum the enum representing the generation method 
   * @return a new instance of the represented generation method
   */
  public static GenerationMethod enumToGenerationMethod(GenerationEnum generationEnum){

    switch(generationEnum){
      case GenerationEnum.FIRST:
        return new FirstMethod();

      case GenerationEnum.HEURISTIC:
        return new HeuristicMethod(); 

      case GenerationEnum.RANDOM:
        return new RandomMethod();

    }
    return null;
  }

  /** Returns the next segment selected by the generation method
   * @param data the list of segments to select in
   */
  public abstract Segment getSegment(ArrayList<Segment> data);
}
