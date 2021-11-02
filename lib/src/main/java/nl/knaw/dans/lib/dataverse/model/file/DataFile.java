package nl.knaw.dans.lib.dataverse.model.file;

public class DataFile {
  private int   id;
  private String persistentId;
  private String pidURL;
  private String filename;
  private String contentType;
  private long filesize;
  private String storageIdentifier;
  private int rootDataFileId;
  private Checksum checksum;
  private String creationDate;

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public Checksum getChecksum() {
    return checksum;
  }

  public void setChecksum(Checksum checksum) {
    this.checksum = checksum;
  }

  public int getRootDataFileId() {
    return rootDataFileId;
  }

  public void setRootDataFileId(int rootDataFileId) {
    this.rootDataFileId = rootDataFileId;
  }

  public String getStorageIdentifier() {
    return storageIdentifier;
  }

  public void setStorageIdentifier(String storageIdentifier) {
    this.storageIdentifier = storageIdentifier;
  }

  public long getFilesize() {
    return filesize;
  }

  public void setFilesize(long filesize) {
    this.filesize = filesize;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getPidURL() {
    return pidURL;
  }

  public void setPidURL(String pidURL) {
    this.pidURL = pidURL;
  }

  public String getPersistentId() {
    return persistentId;
  }

  public void setPersistentId(String persistentId) {
    this.persistentId = persistentId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
