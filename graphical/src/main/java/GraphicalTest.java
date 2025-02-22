import java.util.Arrays;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import shared.scene.SceneFinder;
import shared.scene.SceneReader;
import shared.BSP;
import shared.generation.GenerationEnum;
import shared.generation.GenerationMethod;



public class GraphicalTest extends Application {

  // all the sceneoptions in FX properties
  private SceneOptions sceneOptions;
  private shared.scene.Scene drawScene; 
  private BSP bsp;

  @Override
  public void start(Stage stage){

    // initiate the scene parameters
    sceneOptions = new SceneOptions();

    // top left parameters for the scene (file to load, view, Generation method)
    VBox sceneParam = createSceneParam();

    // bottom right parameters for the eye component (position angle fov )
    HBox eyeParam = createEyeParam();

    // drawScene 
    AnchorPane sceneAnchor = new AnchorPane();

    GridPane gridPane = new GridPane();
    gridPane.add(sceneParam, 0, 0);
    gridPane.add(sceneAnchor, 0,1);
    gridPane.add(eyeParam, 1,1);

    stage.setScene(new Scene(gridPane));
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
  
  // creates the left panel 
  private VBox createSceneParam(){

    Button loadButton = new Button("LOAD");
    Label state = new Label("");
    loadButton.setOnAction(e -> state.setText(loadScene()));
    HBox buttonHbox = new HBox(loadButton,state);

    Node fileParam = createFileParam();
    Node generationParam = createGenerationParam();
    Node radioButtons = createRadioButtons();

    return new VBox(fileParam, generationParam, buttonHbox, radioButtons);
  }

  private HBox createEyeParam(){
    Node coordonatesParam = createCoordonatesParam();
    Node viewParam = createViewParam();
    Node eyeButtons = createEyeButtons();

    return new HBox(coordonatesParam, viewParam, eyeButtons);
  }


  private Node createFileParam(){
    // getting the scenes names and paths
    SceneFinder sc = new SceneFinder();

    // the file Selector Hbox
    Label fileText = new Label("File to load : ");
    ChoiceBox<String> fileSelector = new ChoiceBox<String>(FXCollections.observableArrayList(sc.findScenes()));

    // links the fileSelector and the sceneOptions.fileLocProperty
    fileSelector.setConverter(new FileConverter());
    fileSelector.valueProperty().bindBidirectional(sceneOptions.fileLocProperty());

    return new HBox(fileText, fileSelector);
  }

  private Node createGenerationParam(){
    // GenerationMethod Hbox
    Label generationText = new Label("Generation method : ");

    ChoiceBox<GenerationEnum> generationSelector = new ChoiceBox<GenerationEnum>(FXCollections.observableArrayList(Arrays.asList(GenerationEnum.class.getEnumConstants())));

    generationSelector.setConverter(new GenerationEnumConverter());
    generationSelector.valueProperty().bindBidirectional(sceneOptions.generationMethodProperty());

    return new HBox(generationText, generationSelector);
  }


  private Node createRadioButtons(){
    // radio Button for the view
    
    final ToggleGroup viewGroup = new ToggleGroup();
    RadioButton topView = new RadioButton("top view");
    topView.setToggleGroup(viewGroup);
    topView.setSelected(true);
    RadioButton eyeView = new RadioButton("eye view");
    eyeView.setToggleGroup(viewGroup);
    
    // changing views  
    viewGroup.selectedToggleProperty().addListener(
    (ObservableValue<? extends Toggle> ov, Toggle old_toggle, 
        Toggle new_toggle) -> {
        if (viewGroup.getSelectedToggle() != null) {
          sceneOptions.setViewType(((RadioButton) viewGroup.getSelectedToggle()).getText());
          }
      });

    return new VBox(topView, eyeView);
  }

  // creates the x and y textFields
  private Node createCoordonatesParam(){
    Label xLabel = new Label("X : ");
    // textField limited by the coordonates of the borders
    DoubleProperty xLimit = sceneOptions.getEye().xLimitProperty();

    LimitedTextField xField = new LimitedTextField(xLimit);
    xField.textProperty().bindBidirectional(sceneOptions.getEye().xProperty(), new NumberStringConverter());
    HBox xBox = new HBox(xLabel, xField);

    Label yLabel = new Label("Y : ");

    DoubleProperty yLimit = sceneOptions.getEye().xLimitProperty();
    LimitedTextField yField = new LimitedTextField(yLimit);
    // textField limited by the coordonates of the borders
    yField.textProperty().bindBidirectional(sceneOptions.getEye().yProperty(), new NumberStringConverter());
    HBox yBox = new HBox(yLabel, yField);
    
    return new VBox(xBox, yBox);
  }


  // creates the angle and fov textFields
  private Node createViewParam(){
    // angle TextField
    Label angleLabel = new Label("Angle : ");
    LimitedTextField angleField = new LimitedTextField(new SimpleDoubleProperty(360d));

    // textField limited by the coordonates of the borders
    angleField.textProperty().bindBidirectional(sceneOptions.getEye().angleProperty(), new NumberStringConverter());
    HBox angleHbox= new HBox(angleLabel, angleField);

    // fov TextField
    Label fovLabel = new Label("FOV : ");
    LimitedTextField fovField = new LimitedTextField(new SimpleDoubleProperty(180d));

    fovField.textProperty().bindBidirectional(sceneOptions.getEye().fovProperty(), new CoordonateConverter(180));
    HBox fovHbox= new HBox(fovLabel, fovField);

    return new VBox(angleHbox, fovHbox);
  }

  // creates the two eye buttons
  private Node createEyeButtons(){
    // set eye info button 
    Button setEyeButton = new Button("Set"); 
    setEyeButton.setOnAction(e -> setEye());

    // unset eye info button
    Button unsetEyeButton = new Button("Unset"); 
    unsetEyeButton.setOnAction( e -> unsetEye());

    return new VBox(setEyeButton, unsetEyeButton);
  }

  private String loadScene(){

    if (sceneOptions.getFileLoc().length() == 0)
      return "please select a file to load";

    SceneReader sr = new SceneReader();
    shared.scene.Scene loadedScene = sr.read(sceneOptions.getFileLoc());

    if (loadedScene == null)
      return "Not Loaded Correctly";

    drawScene = loadedScene;
    sceneOptions.getEye().setxLimit(drawScene.getCorners()[1].x);
    sceneOptions.getEye().setyLimit(drawScene.getCorners()[2].y);

    if (!loadBsp())
      return "Failled loading the BSP";

    return "Succes !";
  }

  private boolean loadBsp(){
    BSP loadedBsp = new BSP(drawScene.getSegList(), GenerationMethod.enumToGenerationMethod(sceneOptions.getGenerationMethod()));

    bsp = loadedBsp; 
    return true;

  }


  private void setEye(){

      // TODO : create an eye component with the field infos 
  }
  private void unsetEye(){
      // TODO : delete the eye component
  }

} 
