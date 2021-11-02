package nl.knaw.dans.lib.dataverse.model.file;

public class Checksum {
  private String type;
  private String Value;

  public String getValue() {
    return Value;
  }

  public void setValue(String value) {
    Value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
