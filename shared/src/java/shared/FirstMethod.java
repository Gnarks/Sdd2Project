package shared;

import java.util.Random;
import java.util.ArrayList;

public class FirstMethod implements GenerationMethod
{
  
  public Segment getSegment(ArrayList<Segment> data){
    return data.get(0);
  }
}
