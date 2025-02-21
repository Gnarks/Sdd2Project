import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class EyeProperty{


  private final DoubleProperty x = new SimpleDoubleProperty(0);

  private final DoubleProperty y = new SimpleDoubleProperty(0);

  private final DoubleProperty fov = new SimpleDoubleProperty(0);

  private final DoubleProperty angle = new SimpleDoubleProperty(0); 


  public DoubleProperty xProperty(){
    return x;
  }
  
  public double getX(){
    return x.get();
  }

  public void setX(double x){
    this.x.set(x);
  }

  public DoubleProperty yProperty(){
    return y;
  }
  
  public double getY(){
    return y.get();
  }

  public void setY(double y){
    this.y.set(y);
  }

  public double getFov() {
    return fov.get();
  }

  public DoubleProperty fovProperty() {
    return fov;
  }

  public void setFov(double fov) {
    this.fov.set(fov);
  }

  public double getAngle() {
    return angle.get();
  }

  public DoubleProperty angleProperty() {
    return angle;
  }

  public void setAngle(double angle) {
    this.angle.set(angle);
  }
}
