package shared.generation;

import java.util.ArrayList;
import shared.*;

public abstract class GenerationMethod{


  public static GenerationMethod enumToGenerationMethod(GenerationEnum generationEnum){

    switch(generationEnum){
      case GenerationEnum.FIRST:
        return new FirstMethod();

      case GenerationEnum.HEURISTIC:
        //TODO changer ici lorsqu'on aura impl HEURISTIC
        return new FirstMethod(); 

      case GenerationEnum.RANDOM:
        return new RandomMethod();

    }
    return null;
  }

  public abstract Segment getSegment(ArrayList<Segment> data);
}
