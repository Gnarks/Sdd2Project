
import javafx.scene.control.*;

public class LimitedTextField extends TextField {

  private Double limit;

  public LimitedTextField(Double limit){
    this.limit = limit;
  }

  @Override public void replaceText(int start, int end, String text) {
    if (!(text.matches("[^-\\d.]")) &&
  (text.length() == 0 || (text.charAt(0) !='.'||
  (text.charAt(0) == '.' && !super.getText().contains(".")))))
  {
      super.replaceText(start, end, text);

      if (text.length() != 0 && Double.parseDouble(super.getText()) > limit){
        super.setText(String.valueOf(limit));
      }
    }
  }

  @Override public void replaceSelection(String text) {

    if (!(text.matches("[^-\\d.]")) &&
  (text.length() == 0 || (text.charAt(0) !='.'||
  (text.charAt(0) == '.' && !super.getText().contains(".")))))
  {
      super.replaceSelection(text);
      if (text.length() != 0 && Double.parseDouble(super.getText()) > limit){
        super.setText(String.valueOf(limit));
      }
    }
  }
}
