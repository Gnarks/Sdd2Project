package shared.generation;

import java.util.ArrayList;
import shared.*;

public interface GenerationMethod{

  public Segment getSegment(ArrayList<Segment> data);
}
