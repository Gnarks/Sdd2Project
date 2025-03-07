import java.util.ArrayList;

import shared.BSP;
import shared.Segment;
import shared.generation.GenerationEnum;
import shared.generation.GenerationMethod;
import shared.scene.Scene;
import shared.scene.SceneFinder;
import shared.scene.SceneReader;

public class TestConsole{

  public static void main(String[] args){
    
    SceneFinder sf = new SceneFinder();
    ArrayList<String> allScenes =  sf.findScenes();
    //for (String var : allScenes) {
    //  System.out.println(var);
    //
    //}
    int indexPicked = 3;

    SceneReader sr = new SceneReader();
    Scene scene = sr.read(allScenes.get(indexPicked));
    ArrayList<Segment> segList = scene.getSegList();

    double meanTime = getMeanCpuTime(segList, GenerationEnum.FIRST, 100);

    System.out.printf("the mean time in milliseconds is : %s \n", meanTime / 1e6);

  }
  
  /** Returns the mean cpu time of BSP creation 
   *
   *
   *
   */
  private static double getMeanCpuTime(ArrayList<Segment> segList, GenerationEnum generationEnum, int iterations){
    
    int sum = 0;
    for (int i = 0; i < iterations; i++) {
      long start = System.nanoTime();
      // call
      new BSP(segList, GenerationMethod.enumToGenerationMethod(generationEnum));
      sum += System.nanoTime() -start;
    }
    return sum/iterations;

  }
}

