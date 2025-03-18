package shared.generation;

import java.util.ArrayList;
import shared.geometrical.*;

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
