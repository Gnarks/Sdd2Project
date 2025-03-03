import javafx.util.StringConverter;

// convertes fullPath of files to their path from the /scenes directory
public class FileConverter extends StringConverter<String>{

  @Override
  public String toString(String fullPath) {
    // Affiche uniquement le dernier caractère de la chaîne
    if (fullPath.length() < 1) {return "";}
    return fullPath.substring(fullPath.indexOf("scenes/") + 7);
  }
  @Override
  public String fromString(String string) {
    return null;
  }

}
