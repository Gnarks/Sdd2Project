import java.util.Arrays;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import shared.scene.SceneFinder;
import shared.Point;
import shared.generation.GenerationEnum;



public class GraphicalTest extends Application {

  // all the sceneoptions in FX properties
  private SceneOptions sceneOptions;

  @Override
  public void start(Stage stage){

    // the scene parameters
    sceneOptions = new SceneOptions();

    // top left parameters for the scene (file to load, view, Generation method)
    VBox sceneParam = createSceneParam();

    // bottom right parameters for the eye component (position angle fov )
    HBox eyeParam = createEyeParam();

    // drawScene 
    AnchorPane drawScene = new AnchorPane();

    GridPane gridPane = new GridPane();
    gridPane.add(sceneParam, 0, 0);
    gridPane.add(drawScene, 0,1);
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
    loadButton.setOnAction(e -> loadScene());

    Node fileParam = createFileParam();
    Node generationParam = createGenerationParam();
    Node radioButtons = createRadioButtons();

    return new VBox(fileParam, generationParam, loadButton, radioButtons);
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
    //TODO set scene coordonate limits  
    Point limits = new Point(500,500);

    Label xLabel = new Label("X : ");
    // textField limited by the coordonates of the borders
    LimitedTextField xField = new LimitedTextField(limits.x);

    xField.textProperty().bindBidirectional(sceneOptions.getEye().xProperty(), new NumberStringConverter());
    HBox xBox = new HBox(xLabel, xField);

    Label yLabel = new Label("Y : ");
    LimitedTextField yField = new LimitedTextField(limits.y);
    // textField limited by the coordonates of the borders
    yField.textProperty().bindBidirectional(sceneOptions.getEye().yProperty(), new NumberStringConverter());
    HBox yBox = new HBox(yLabel, yField);
    
    return new VBox(xBox, yBox);
  }


  // creates the angle and fov textFields
  private Node createViewParam(){

    // angle TextField
    Label angleLabel = new Label("Angle : ");
    LimitedTextField angleField = new LimitedTextField(360d);

    // textField limited by the coordonates of the borders
    angleField.textProperty().bindBidirectional(sceneOptions.getEye().angleProperty(), new NumberStringConverter());
    HBox angleHbox= new HBox(angleLabel, angleField);

    // fov TextField
    Label fovLabel = new Label("FOV : ");
    LimitedTextField fovField = new LimitedTextField(180d);

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

  private void loadScene(){
    System.out.println(sceneOptions.getEye().getX() + "x and y" + sceneOptions.getEye().getY());
    // TODO load the scene here with param generation and file 
  }


  private void setEye(){

      // TODO : create an eye component with the field infos 
  }
  private void unsetEye(){
      // TODO : delete the eye component
  }

} 
