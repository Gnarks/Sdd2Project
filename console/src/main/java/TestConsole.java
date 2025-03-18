import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import shared.geometrical.*;
import shared.scene.*;
import shared.*;
import shared.generation.*;

public class TestConsole{

  private static Scanner sc = new Scanner(System.in);

  public static void main(String[] args){
    
    
    Scene scene = selectScene();
    int eyeNumber = askInt("Select the number of random eyes for the painter's algorithm");
    int iterations = askInt("Select the number of iterations for the mean");

    ArrayList<Eye> eyeList = generateEyes(scene, eyeNumber);


    for (GenerationEnum heuristic : Arrays.asList(GenerationEnum.class.getEnumConstants())) {
      System.out.println("\n-----------------------------------------");
      displayHeursiticStats(heuristic, scene, eyeList, iterations);
    }
    sc.close();
  }
  


  /** Returns all there is to know (BspStat) for the stats of an heuristic
   * @param scene the scene to create the bsp on 
   * @param generationEnum the generationMethod 
   * @param iterations the number of BSP to calculate the mean
   *
   * @return A BspStat object
   */
  private static BspStat getBspStat(Scene scene, ArrayList<Eye> eyeList, GenerationEnum generationEnum, int iterations){
    
    ArrayList<Segment> segList = scene.getSegList();
    long sumBsp = 0;
    double sumHeight = 0;
    double sumSize = 0;

    ArrayList<BSP> bspList = new ArrayList<>();
    for (int i = 0; i < iterations; i++) {
      long start = System.nanoTime();
      // call
      BSP createdBsp =new BSP(segList, GenerationMethod.enumToGenerationMethod(generationEnum)); 
      sumBsp += System.nanoTime() -start;
      
      sumHeight += createdBsp.getHead().height;
      sumSize += createdBsp.getHead().size;
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

    return new BspStat(sumBsp/iterations, sumPainter/iterations, sumHeight/iterations, sumSize/iterations);
  }

  private static Scene selectScene(){
    SceneFinder sf = new SceneFinder();
    ArrayList<String> allScenes =  sf.findScenes();
    for (int i = 0; i < allScenes.size(); i++) {
      System.out.printf("%s) %s\n", i,allScenes.get(i));
    }
    int indexPicked = askInt("Select a scene to test");

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

  private static void displayHeursiticStats(GenerationEnum heuristic, Scene scene, ArrayList<Eye> eyeList, int iterations){

    BspStat bspStat = getBspStat(scene, eyeList, heuristic, iterations);
    System.out.printf("%s Got : \n", heuristic);
    System.out.printf("BSP tree height : %s\n", bspStat.meanHeight);
    System.out.printf("BSP tree size : %s\n", bspStat.meanSize);
    System.out.printf("Mean BSP tree creation time : %s \n", bspStat.meanBspCpuTime /1e6);
    System.out.printf("Mean Painter's algorithm time: %s \n", bspStat.meanPainterCpuTime/ 1e6);
  }

  private static int askInt(String text) {
    System.out.printf("\n%s : ", text);
    while(true){
      try {
        int a = Integer.parseInt(sc.next());
        return a;
      } catch(NumberFormatException ne) {
        System.out.printf("\n %s :", text);
      }
    }
  }
}

