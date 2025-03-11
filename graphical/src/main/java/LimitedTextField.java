
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.*;

public class LimitedTextField extends TextField {

  private DoubleProperty limit;
  private boolean fullCycle;

  public LimitedTextField(DoubleProperty limit, boolean fullCycle){
    this.limit = limit;
    this.fullCycle = fullCycle;
  }

  @Override public void replaceText(int start, int end, String text) {
    if (!(text.matches("[^-\\d.]")) &&
  (text.length() == 0 || (text.charAt(0) !='.'||
  (text.charAt(0) == '.' && !super.getText().contains("."))))){

      super.replaceText(start, end, text);
      tryParse(text);
    }
  }

  @Override public void replaceSelection(String text) {

    if (!(text.matches("[^-\\d.]")) &&
  (text.length() == 0 || (text.charAt(0) !='.'||
  (text.charAt(0) == '.' && !super.getText().contains("."))))){

      super.replaceSelection(text);
      tryParse(text);
    }
  }

  private void tryParse(String text){
    if (text.length() == 0)
    return;

    try {
      double parsed = Double.parseDouble(super.getText());
      if (parsed > limit.get() ){
        super.setText(String.valueOf(limit.get()));
      }
      if (!fullCycle && parsed < -limit.get()){
        super.setText(String.valueOf(-limit.get()));
      }
    } catch (Exception e) {
      super.setText("0");
    }
  }

}
