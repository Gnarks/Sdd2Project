import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import shared.generation.GenerationEnum;

public class SceneOptions{

  // contains the file location to load 
  private final StringProperty fileLoc = new SimpleStringProperty("");
  // contains the lowered string of enum
  private final ObjectProperty<GenerationEnum> GenerationMethod = new SimpleObjectProperty<>(GenerationEnum.FIRST);  
  // contains "top view" or "eye view" depending on the radiobutton
  private final StringProperty viewType = new SimpleStringProperty("");

  private final EyeProperty eye = new EyeProperty(); 

  private ObjectProperty<Node> drawScene = new SimpleObjectProperty<Node>();

  public EyeProperty getEye() {
    return eye;
  }



  public Node getDrawSceneNode(){
    return drawScene.get();
  }

  public ObjectProperty<Node> DrawSceneNodeProperty() {
    return drawScene;
  }

  public void setDrawSceneNode(Node drawScene) {
    this.drawScene.set(drawScene);
  }

  public String getFileLoc(){
    return fileLoc.get();
  }

  public StringProperty fileLocProperty() {
    return fileLoc;
  }

  public void setFileLoc(String fileLoc) {
    this.fileLoc.set(fileLoc);
  }

  public GenerationEnum getGenerationMethod() {
    return GenerationMethod.get();
  }

  public ObjectProperty<GenerationEnum> generationMethodProperty() {
    return GenerationMethod;
  }

  public void setGenerationMethod(GenerationEnum generationMethod) {
    this.GenerationMethod.set(generationMethod);
  }

  public String getViewType() {
    return viewType.get();
  }

  public StringProperty viewTypeProperty() {
    return viewType;
  }

  public void setViewType(String viewType) {
    this.viewType.set(viewType);
  }
}
