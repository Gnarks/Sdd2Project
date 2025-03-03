package shared.generation;

import shared.*;

import java.util.ArrayList;

public class FirstMethod extends GenerationMethod
{
  public Segment getSegment(ArrayList<Segment> data){
    return data.get(0);
  }
}
