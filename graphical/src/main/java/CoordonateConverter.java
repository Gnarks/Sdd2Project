import javafx.beans.property.DoubleProperty;
import javafx.util.StringConverter;

public class CoordonateConverter extends StringConverter<Number>{

  private DoubleProperty limit; 

  public CoordonateConverter(DoubleProperty limit){
    this.limit = limit;
  }

  public Number fromString(String st){
    if (st == null || st.isEmpty()){
      return 0d;
    }
    try {
      Double parsed = Double.parseDouble(st.replaceAll("[^-\\d.]", ""));
      return (parsed<=limit.get()? parsed: limit.get());
      
    }
    catch(Exception e) {
      return 0d;
    }

  }

  public String toString(Number d){
    return d.toString();
  }
}
