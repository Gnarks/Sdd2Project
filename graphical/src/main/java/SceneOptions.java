import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.Eye;
import shared.generation.GenerationEnum;

public class SceneOptions{

  // contains the file location to load 
  private final StringProperty fileLoc = new SimpleStringProperty("");
  // contains the lowered string of enum
  private final ObjectProperty<GenerationEnum> GenerationMethod = new SimpleObjectProperty<>(GenerationEnum.FIRST);  
  // contains "top view" or "eye view" depending on the radiobutton
  private final StringProperty viewType = new SimpleStringProperty("");
  // the eye options 
  private final ObjectProperty<Eye> eye = new SimpleObjectProperty<>(null);


  // Getter et Setter pou fileLoc
  public Eye getEye() {
    return eye.get();
  }

  public ObjectProperty<Eye> eyeProperty() {
    return eye;
  }

  public void setEye(Eye eye) {
    this.eye.set(eye);
  }

  // Getter et Setter pour fileLoc
  public String getFileLoc() {
    return fileLoc.get();
  }

  public StringProperty fileLocProperty() {
    return fileLoc;
  }

  public void setFileLoc(String fileLoc) {
    this.fileLoc.set(fileLoc);
  }

  // Getter et Setter pour GenerationMethod
  public GenerationEnum getGenerationMethod() {
    return GenerationMethod.get();
  }

  public ObjectProperty<GenerationEnum> generationMethodProperty() {
    return GenerationMethod;
  }

  public void setGenerationMethod(GenerationEnum generationMethod) {
    this.GenerationMethod.set(generationMethod);
  }

  // Getter et Setter pour viewType
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
