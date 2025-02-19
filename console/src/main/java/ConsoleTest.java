import java.io.IOException;
import java.util.ArrayList;

import shared.SceneReader;
import shared.Segment;

public class ConsoleTest{

  public static void main(String[] args) throws IOException{
    System.out.println("yipie");

    SceneReader sr = new SceneReader();

     ArrayList<Segment> list = sr.read(ClassLoader.getSystemClassLoader().getResource("scenes/first/octogone.txt").getPath());
    
    for (Segment seg : list){
      System.out.println(seg);
    }
  }
}

