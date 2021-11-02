package nl.knaw.dans.lib.dataverse.model.file;

import java.util.List;

public class FileMeta {
  private String label;
  private String description;
  private String directoryLabel;
  private Boolean restrict;
  private List<String> categories; // TODO Enum? https://guides.dataverse.org/en/latest/user/dataset-management.html?highlight=category#file-tags
  private DataFile dataFile;
  private Boolean forceReplace;

  public FileMeta() {
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDirectoryLabel() {
    return directoryLabel;
  }

  public void setDirectoryLabel(String directoryLabel) {
    this.directoryLabel = directoryLabel;
  }

  public Boolean getRestrict() {
    return restrict;
  }

  public void setRestrict(Boolean restrict) {
    this.restrict = restrict;
  }

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public Boolean getForceReplace() {
    return forceReplace;
  }

  public void setForceReplace(Boolean forceReplace) {
    this.forceReplace = forceReplace;
  }

  public DataFile getDataFile() {
    return dataFile;
  }

  public void setDataFile(DataFile dataFile) {
    this.dataFile = dataFile;
  }

  // TODO public ... toPrestagedFile = ???
}
