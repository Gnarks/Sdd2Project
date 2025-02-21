import java.util.Arrays;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import shared.scene.SceneFinder;
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
    //GridPane eyeParam = getEyeParam();

    // drawScene 
    AnchorPane drawScene = new AnchorPane();

    GridPane gridPane = new GridPane();
    gridPane.add(sceneParam, 0, 0);
    gridPane.add(drawScene, 0,1);
    //gridPane.add(eyeParam, 1,1);

    stage.setScene(new Scene(gridPane));
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

  
  private VBox createSceneParam(){

    Button loadButton = new Button("LOAD");
    loadButton.setOnAction(e -> loadScene());

    // left panel (Scene Param)
    VBox sceneParam = new VBox();
    sceneParam.getChildren().add(createFileParam());
    sceneParam.getChildren().add(createGenerationParam());
    sceneParam.getChildren().add(loadButton);
    sceneParam.getChildren().add(createRadioButtons());

    return sceneParam;
  }

  /**private GridPane getEyeParam(){

    // X textField
    // TODO make the textField change if the cursor is on the draw pane
    Label xLabel = new Label("X : ");
    xField = new TextField();
    HBox xHBox = new HBox();
    xHBox.getChildren().add(xLabel);
    xHBox.getChildren().add(xField);


    // Y textField 
    // TODO make the textField change if the cursor is on the draw pane
    Label yLabel = new Label("Y : ");
    yField = new TextField();
    HBox yHBox= new HBox();
    yHBox.getChildren().add(yLabel);
    yHBox.getChildren().add(yField);


    // angle TextField
    // TODO make the textField change if the cursor is on the draw pane
    Label angleLabel = new Label("Angle : ");
    angleField = new TextField();
    HBox angleHbox= new HBox();
    angleHbox.getChildren().add(angleLabel);
    angleHbox.getChildren().add(angleField);

    // fov TextField
    // TODO make the textField change if the cursor is on the draw pane
    Label fovLabel = new Label("FOV : ");
    fovField = new TextField();
    HBox fovHbox= new HBox();
    fovHbox.getChildren().add(fovLabel);
    fovHbox.getChildren().add(fovField);


    // set eye info button 
    Button setEyeInfo = new Button("Set"); 
    setEyeInfo.setOnAction((ActionEvent e ) -> {
      
      // TODO : create an eye component with the field infos 
    });


    // unset eye info button
    Button unsetEyeInfo = new Button("Unset"); 
    unsetEyeInfo.setOnAction((ActionEvent e ) -> {
      
      // TODO : delete the eye component
    });




    //setting the grid pane 
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(20);
    grid.setHgap(20);

    GridPane.setConstraints(xHBox, 0, 0);
    grid.getChildren().add(xHBox);

    GridPane.setConstraints(yHBox, 0, 1);
    grid.getChildren().add(yHBox);

    GridPane.setConstraints(angleHbox, 1, 0);
    grid.getChildren().add(angleHbox);

    GridPane.setConstraints(fovHbox, 1, 1);
    grid.getChildren().add(fovHbox);

    GridPane.setConstraints(setEyeInfo, 2, 0);
    grid.getChildren().add(setEyeInfo);

    GridPane.setConstraints(unsetEyeInfo, 2, 1);
    grid.getChildren().add(unsetEyeInfo);
    return grid;
  } **/


  private Node createFileParam(){

    // getting the scenes names and paths
    SceneFinder sc = new SceneFinder();

    // the file Selector Hbox
    Label fileText = new Label("File to load : ");
    ChoiceBox<String> fileSelector = new ChoiceBox<String>(FXCollections.observableArrayList(sc.findScenes()));

    // links the fileSelector and the sceneOptions.fileLocProperty
    fileSelector.setConverter(new FileConverter());
    fileSelector.valueProperty().bindBidirectional(sceneOptions.fileLocProperty());

    HBox fileHbox = new HBox();
    fileHbox.getChildren().add(fileText);
    fileHbox.getChildren().add(fileSelector);
    return fileHbox;
  }

  private Node createGenerationParam(){

    // GenerationMethod Hbox
    Label generationText = new Label("Generation method : ");

    ChoiceBox<GenerationEnum> generationSelector = new ChoiceBox<GenerationEnum>(FXCollections.observableArrayList(Arrays.asList(GenerationEnum.class.getEnumConstants())));

    generationSelector.setConverter(new GenerationEnumConverter());
    generationSelector.valueProperty().bindBidirectional(sceneOptions.generationMethodProperty());

    HBox generationHbox = new HBox();
    generationHbox.getChildren().add(generationText);
    generationHbox.getChildren().add(generationSelector);

    return generationHbox;
  }

  private void loadScene(){
    // TODO load the scene here with param generation and file 
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

    VBox vb = new VBox();
    vb.getChildren().add(topView);
    vb.getChildren().add(eyeView);

    return vb;

  }
} 
