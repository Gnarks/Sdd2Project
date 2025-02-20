package shared;

import java.util.Random;
import java.util.ArrayList;

public class RandomMethod implements GenerationMethod
{
  
  public Segment getSegment(ArrayList<Segment> data){
    Random rand = new Random();
    int random = rand.nextInt(data.size());
    return data.get(random);
  }
}
