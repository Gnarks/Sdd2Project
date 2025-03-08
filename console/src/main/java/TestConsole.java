import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import shared.BSP;
import shared.Eye;
import shared.Point;
import shared.Segment;
import shared.generation.GenerationEnum;
import shared.generation.GenerationMethod;
import shared.scene.Scene;
import shared.scene.SceneFinder;
import shared.scene.SceneReader;

public class TestConsole{

  public static void main(String[] args){
    
    
    Scene scene = selectScene();
    ArrayList<Eye> eyeList = generateEyes(scene, 100);

    for (GenerationEnum heuristic : Arrays.asList(GenerationEnum.class.getEnumConstants())) {
      System.out.println("\n-----------------------------------------");
      displayHeursiticStats(heuristic, scene, eyeList);
    }
  }
  


  /** Returns the mean cpu time of BSP creation and Painter algorithm
   * @param scene the scene to create the bsp on 
   * @param generationEnum the generationMethod 
   * @param iterations the number of BSP to calculate the mean
   *
   * @return two Double: first is mean Bsp creation time, second is mean Painter's algorithm time 
   */
  private static double[] getMeanCpuTime(Scene scene, ArrayList<Eye> eyeList, GenerationEnum generationEnum, int iterations){
    
    ArrayList<Segment> segList = scene.getSegList();
    long sumBsp = 0;
    ArrayList<BSP> bspList = new ArrayList<>();
    for (int i = 0; i < iterations; i++) {
      long start = System.nanoTime();
      // call
      BSP createdBsp =new BSP(segList, GenerationMethod.enumToGenerationMethod(generationEnum)); 
      sumBsp += System.nanoTime() -start;
      bspList.add(createdBsp);
    }

    long sumPainter = 0;
    for (int i = 0; i < iterations; i++) {
      BSP bsp = bspList.get(i); 
      double[] range = {scene.getRange().x, scene.getRange().y};
      long eyeSum = 0;
      for (int j = 0; j < eyeList.size(); j++) {
        Eye eye = eyeList.get(j);

        long start = System.nanoTime();
        bsp.painterAlgorithm(eye, range);
        eyeSum += System.nanoTime() -start;
      }
      sumPainter += eyeSum/eyeList.size();
    }

    double[] means = {sumBsp/iterations, sumPainter/iterations};
    return means;
  }

  private static Scene selectScene(){
    SceneFinder sf = new SceneFinder();
    ArrayList<String> allScenes =  sf.findScenes();
    System.out.println("Select a scene to test :");
    for (int i = 0; i < allScenes.size(); i++) {
      System.out.printf("%s) %s\n", i,allScenes.get(i));
    }

    Scanner scanner = new Scanner(System.in);
    int indexPicked = scanner.nextInt();
    scanner.close();

    SceneReader sr = new SceneReader();
    Scene scene = sr.read(allScenes.get(indexPicked));
    return scene;
  }

  private static  ArrayList<Eye> generateEyes(Scene scene, int amount){
    
    ArrayList<Eye> eyes = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      Random rnd = new Random();
      Point pos = new Point(rnd.nextDouble(-scene.getRange().x, scene.getRange().x), rnd.nextDouble(-scene.getRange().y, scene.getRange().y));
      double angle = rnd.nextDouble(360);
      double fov = rnd.nextDouble(180);
      eyes.add(new Eye(pos,angle,fov));
    }
    return eyes;
  }

  private static void displayHeursiticStats(GenerationEnum heuristic, Scene scene, ArrayList<Eye> eyeList){

    double[] meanTime = getMeanCpuTime(scene, eyeList, heuristic, 10000);
    System.out.printf("%s Got : \n", heuristic);
    System.out.printf("BSP tree height : %s\n", Double.NaN);
    System.out.printf("BSP tree size : %s\n", Double.NaN);
    System.out.printf("Mean BSP tree creation time : %s \n", meanTime[0] / 1e6);
    System.out.printf("Mean Painter's algorithm time: %s \n", meanTime[1] / 1e6);

  }
}

