import javafx.util.StringConverter;
import shared.generation.GenerationEnum;

// convertes fullPath of files to their path from the /scenes directory
public class GenerationEnumConverter extends StringConverter<GenerationEnum>{

  @Override
  public String toString(GenerationEnum e) {
    // Affiche uniquement le dernier caractère de la chaîne
    return e.toString().toLowerCase();
  }
  @Override
  public GenerationEnum fromString(String string) {
    return null;
  }

}
