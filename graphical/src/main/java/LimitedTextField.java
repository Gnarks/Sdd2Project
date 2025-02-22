
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.*;

public class LimitedTextField extends TextField {

  private DoubleProperty limit;

  public LimitedTextField(DoubleProperty limit){
    this.limit = limit;
  }

  @Override public void replaceText(int start, int end, String text) {
    if (!(text.matches("[^-\\d.]")) &&
  (text.length() == 0 || (text.charAt(0) !='.'||
  (text.charAt(0) == '.' && !super.getText().contains(".")))))
  {
      super.replaceText(start, end, text);

      if (text.length() != 0 && Double.parseDouble(super.getText()) > limit.get()){
        super.setText(String.valueOf(limit.get()));
      }
    }
  }

  @Override public void replaceSelection(String text) {

    if (!(text.matches("[^-\\d.]")) &&
  (text.length() == 0 || (text.charAt(0) !='.'||
  (text.charAt(0) == '.' && !super.getText().contains(".")))))
  {
      super.replaceSelection(text);
      if (text.length() != 0 && Double.parseDouble(super.getText()) > limit.get()){
        super.setText(String.valueOf(limit.get()));
      }
    }
  }
}
