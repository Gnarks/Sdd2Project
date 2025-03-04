import java.util.Arrays;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import shared.scene.SceneFinder;
import shared.scene.SceneReader;
import shared.BSP;
import shared.Eye;
import shared.EyeSegment;
import shared.Point;
import shared.generation.GenerationEnum;
import shared.generation.GenerationMethod;



public class GraphicalTest extends Application {

  // all the sceneoptions in FX properties
  private SceneOptions sceneOptions;
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
    ScrollPane sceneAnchor = createDrawScene();

    GridPane gridPane = new GridPane();
    gridPane.add(sceneParam, 0, 0);
    gridPane.add(sceneAnchor, 1,0);
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
    Node eyeButtons = createEyeButton();

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
  private Node createEyeButton(){
    // set eye info button 
    Button setEyeButton = new Button("Set"); 
    setEyeButton.setOnAction(e -> setEye());
    

    return new VBox(setEyeButton, new Label("Eye parameters"));
  }

  private ScrollPane createDrawScene(){
    ScrollPane sPane = new ScrollPane();
    sPane.setStyle("-fx-border-color: black; -fx-border-width: 1px 1px 1px 0px");

    sceneOptions.DrawSceneNodeProperty().addListener(e -> {
      sPane.setContent(sceneOptions.getDrawSceneNode());
      sPane.setMinSize(sceneOptions.getEye().getxLimit(), sceneOptions.getEye().getyLimit());
    });

    sceneOptions.viewTypeProperty().addListener(e -> {
      if (sceneOptions.getViewType() == "eye view"){
        System.out.println("EYE VIEW enabled ");
        sPane.setContent(sceneOptions.getEyeSceneNode());
      }
      else{
        sPane.setContent(sceneOptions.getDrawSceneNode());
        sPane.setMinSize(sceneOptions.getEye().getxLimit(), sceneOptions.getEye().getyLimit());
      }
    });

    return sPane;
  }

  private String loadScene(){

    if (sceneOptions.getFileLoc().length() == 0)
      return "please select a file to load";

    SceneReader sr = new SceneReader();
    shared.scene.Scene loadedScene = sr.read(sceneOptions.getFileLoc());

    if (loadedScene == null)
      return "Not Loaded Correctly";


    Pane pane = new Pane();
    
    //TODO delete
    System.out.println("Segments drawn :");
    for (shared.Segment seg : loadedScene.getSegList()) {
      //adding 100 to include all segments (not on the edge of the pane)
      System.out.println(seg);
      double initX = seg.getStart().x + loadedScene.getCorners()[3].x*1.1;
      double initY = seg.getStart().y + loadedScene.getCorners()[3].y*1.1;
      double finalX = seg.getEnd().x + loadedScene.getCorners()[3].x*1.1;
      double finalY = seg.getEnd().y + loadedScene.getCorners()[3].y*1.1;

      Line line = new Line(initX, initY, finalX, finalY);
      line.setStroke(Paint.valueOf(seg.getColor()));
      pane.getChildren().add(line);
    }
    sceneOptions.getEye().eyeNodeProperty().addListener( e -> {
      if (sceneOptions.getEye().isDrawn){
        pane.getChildren().removeLast();
      }
      pane.getChildren().add(sceneOptions.getEye().getEyeNode()); 
    });

    pane.setMinSize(loadedScene.getCorners()[3].x*2.2, loadedScene.getCorners()[3].y*2.2 );
    sceneOptions.setDrawSceneNode(pane);

    sceneOptions.getEye().setxLimit(Math.round(loadedScene.getCorners()[3].x*2.2));
    sceneOptions.getEye().setyLimit(Math.round(loadedScene.getCorners()[3].y*2.2));
    //load the bsp 
    
    bsp = new BSP(loadedScene.getSegList(), GenerationMethod.enumToGenerationMethod(sceneOptions.getGenerationMethod()));

    return "Succes !";
  }

  private void setEye(){
    
    DrawEye();
    Point pos = new Point(sceneOptions.getEye().getX() - sceneOptions.getEye().getxLimit()/2,
      sceneOptions.getEye().getY() - sceneOptions.getEye().getyLimit()/2);
    double initAngle = ( sceneOptions.getEye().getAngle() +90) %360;
    double fov = sceneOptions.getEye().getFov();
    Eye eye = new Eye(pos, initAngle, fov);
    
    double[] range = {sceneOptions.getEye().getxLimit(), sceneOptions.getEye().getyLimit()};

    EyeSegment eyePov = bsp.painterAlgorithm(eye, range);
    Pane p = new Pane();

    System.out.printf("\n\nEye parameters are : \n");
    System.out.printf("Initial Pos : (%s, %s) \n", sceneOptions.getEye().getX(), sceneOptions.getEye().getY());
    System.out.printf("Pos (Transposed): %s  \n", pos);
    System.out.printf("Angle : %s \n", sceneOptions.getEye().getAngle());
    System.out.printf("Fov : %s \n\n\n", sceneOptions.getEye().getFov());

    // TODO get the drawnSegment from the eye pov
    for (shared.Segment seg : eyePov.getParts()) {
      //adding 100 to include all segments (not on the edge of the pane)
      System.out.println(seg);
      double initX = seg.getStart().x + sceneOptions.getEye().getxLimit()/2;
      double initY = seg.getStart().y + sceneOptions.getEye().getyLimit()/2;
      double finalX = seg.getEnd().x + sceneOptions.getEye().getxLimit()/2;
      double finalY = seg.getEnd().y + sceneOptions.getEye().getyLimit()/2;
      System.out.printf("Transposed to :%s \n\n", new shared.Segment(new Point(initX, initY), new Point(finalX, finalY), seg.getColor()));

      Line line = new Line(initX, initY, finalX, finalY);
      line.setStroke(Paint.valueOf(seg.getColor()));
      p.getChildren().add(line);
    }

    sceneOptions.setEyeSceneNode(p);
  }
  
  
  private void DrawEye(){
    Point pos = new Point(sceneOptions.getEye().getX(), sceneOptions.getEye().getY());
    double initAngle = ( sceneOptions.getEye().getAngle() +90) %360;
    double fov = sceneOptions.getEye().getFov();
    double[] angles = {Math.toRadians((initAngle - fov) % 360), Math.toRadians((initAngle + fov) % 360)};
    // arbitrary length of the segments showing the angles
    double length = 30;

    Line leftLine = new Line(pos.x, pos.y, pos.x + (length * Math.sin(angles[0])), pos.y + (length * Math.cos(angles[0])));
    Line rightLine = new Line(pos.x, pos.y, pos.x + (length * Math.sin(angles[1])), pos.y + (length * Math.cos(angles[1])));
    double essai = initAngle -fov -90;
    Arc arc = new Arc(pos.x,pos.y, 15, 15,essai , fov*2);
    arc.setType(ArcType.ROUND);
    sceneOptions.getEye().setEyeNode(new Pane(leftLine, rightLine, arc)); 
    sceneOptions.getEye().isDrawn = true;
  }
} 
