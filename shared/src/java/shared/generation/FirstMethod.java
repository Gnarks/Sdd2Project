package shared.generation;

import shared.*;
import java.util.ArrayList;

/** 
 * Method selecting the first segment in the list
 */
public class FirstMethod extends GenerationMethod
{
  /**
   * @return the first segment in the list
   */
  public Segment getSegment(ArrayList<Segment> data){
    return data.get(0);
  }
}
