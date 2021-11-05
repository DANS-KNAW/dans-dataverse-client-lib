/*
 * Copyright (C) 2021 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.lib.dataverse.model.file;

import java.util.List;

public class FileMeta {

  private String label;
  private String description;
  private String directoryLabel;
  private int version;
  private int datasetVersionId;
  private Boolean restricted;
  private List<String> categories; // TODO Enum or vocab? https://guides.dataverse.org/en/latest/user/dataset-management.html?highlight=category#file-tags
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

  public Boolean getRestricted() {
    return restricted;
  }

  public void setRestricted(Boolean restricted) {
    this.restricted = restricted;
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

  public int getDatasetVersionId() {
    return datasetVersionId;
  }

  public void setDatasetVersionId(int datasetVersionId) {
    this.datasetVersionId = datasetVersionId;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  // TODO public ... toPrestagedFile = ???
}

