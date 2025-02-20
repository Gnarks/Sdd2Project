import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shared.SceneFinder;
import shared.generation.GenerationEnum;

import java.io.IOException;

public class GraphicalTest extends Application {

  // setting class variables because they need to be accessed through multiple 
  // functions (change the string value based on the mouse position)
  private TextField xField;
  private TextField yField;
  private TextField angleField;
  private TextField fovField;





  @Override
  public void start(Stage stage) {

    // top left parameters for the scene (file to load, view, Generation method)
    VBox sceneParam = getSceneParam();
    // bottom right parameters for the eye component (position angle fov )
    GridPane eyeParam = getEyeParam();

    // drawScene 
    AnchorPane drawScene = new AnchorPane();
    // TODO load and draw the draw scene 



    GridPane gridPane = new GridPane();

    gridPane.add(sceneParam, 0, 0);
    gridPane.add(drawScene, 0,1);
    gridPane.add(eyeParam, 1,1);


    Scene scene = new Scene(gridPane);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

  private VBox getSceneParam(){

    // getting the scenes names and paths
    ArrayList<String> sceneNames = new ArrayList<>();
    ArrayList<String> scenesFullPath = new ArrayList<>();
    SceneFinder sc = new SceneFinder();

    try {
      scenesFullPath = sc.findScenes();
      sceneNames = new ArrayList<>();
      
      for (String fullPath : scenesFullPath){
        sceneNames.add(fullPath.substring(fullPath.indexOf("scenes/") + 7));
      }
    }
    catch (IOException e){
      System.out.println(e.toString());
    }

    // the file Selector Hbox
    Label fileText = new Label("File to load : ");
    ChoiceBox<String> fileSelector = new ChoiceBox<String>(FXCollections.observableArrayList(sceneNames));
    HBox fileHbox = new HBox();
    fileHbox.getChildren().add(fileText);
    fileHbox.getChildren().add(fileSelector);

    // GenerationMethod Hbox
    Label generationText = new Label("Generation method : ");
    
    // TODO find all the methods 
    String[] enums = Arrays.stream(GenerationEnum.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    ArrayList<String> stringEnum = new ArrayList<>(); 

    for (String e : enums){
      stringEnum.add(e.toLowerCase());
    }
    ChoiceBox<String> generationSelector = new ChoiceBox<String>(FXCollections.observableArrayList(stringEnum));
    HBox generationHbox = new HBox();
    generationHbox.getChildren().add(generationText);
    generationHbox.getChildren().add(generationSelector);


    // Load button
    Button loadButton = new Button("LOAD");
    loadButton.setOnAction((ActionEvent e ) -> {
      
      // TODO : load here the scene 
    });

    // radio Button for the view
    final ToggleGroup viewGroup = new ToggleGroup();
    RadioButton topView = new RadioButton(" top view ");
    topView.setToggleGroup(viewGroup);
    topView.setSelected(true);
    RadioButton eyeView = new RadioButton(" eye view ");
    eyeView.setToggleGroup(viewGroup);
    // changing views  
    viewGroup.selectedToggleProperty().addListener(
    (ObservableValue<? extends Toggle> ov, Toggle old_toggle, 
        Toggle new_toggle) -> {
        if (viewGroup.getSelectedToggle() != null) {
          // TODO : change the drawing scene here
          }
      });

    // left panel (Scene Param)
    VBox sceneParam = new VBox();
    sceneParam.getChildren().add(fileHbox);
    sceneParam.getChildren().add(generationHbox);
    sceneParam.getChildren().add(loadButton);
    sceneParam.getChildren().add(topView);
    sceneParam.getChildren().add(eyeView);

    return sceneParam;
  }

  private GridPane getEyeParam(){

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
  }
} 
