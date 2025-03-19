package shared.generation;

import java.util.Random;
import java.util.ArrayList;
import shared.geometrical.*;

/* Method selecting a random segment in the list
 */
public class RandomMethod extends GenerationMethod
{
  /** 
   * @return a random segment in the list
   */
  public Segment getSegment(ArrayList<Segment> data){
    Random rand = new Random();
    int random = rand.nextInt(data.size());
    return data.get(random);
  }
}
