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

import lombok.Data;
import nl.knaw.dans.lib.dataverse.model.file.prestaged.Checksum;
import nl.knaw.dans.lib.dataverse.model.file.prestaged.PrestagedFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class FileMeta {

    private String label;
    private String description;
    private String directoryLabel;
    private int version;
    private int datasetVersionId;
    private Boolean restricted;
    private List<String> categories = new ArrayList<>();
    private DataFile dataFile;
    private Boolean forceReplace;

    public PrestagedFile toPrestagedFile() {
        if (dataFile == null)
            throw new IllegalArgumentException("Cannot convert to PrestagedFile if dataFile is null");
        PrestagedFile p = new PrestagedFile();
        p.setStorageIdentifier(dataFile.getStorageIdentifier());
        p.setFileName(label);
        p.setMimeType(dataFile.getContentType());
        p.setChecksum(new Checksum(dataFile.getChecksum().getType(), dataFile.getChecksum().getValue()));
        p.setDescription(description);
        p.setDirectoryLabel(directoryLabel);
        p.setCategories(categories);
        p.setRestrict(restricted);
        p.setForceReplace(forceReplace);
        return p;
    }

    public FileMetaUpdate toFileMetaUpdate() {
        return toFileMetaUpdate(true);
    }

    /**
     * Convert this object to a FileMetaUpdate object, which can be used in the {{@link nl.knaw.dans.lib.dataverse.DatasetApi#updateFileMetadatas(List)} method. Note, that the 'restrict' property is
     * only included in the FileMetaUpdate object if the 'sendRestrict' parameter is true and the 'restricted' property is not null. You should only send the 'restrict' property if you want to change
     * the restricted status of the file. Dataverse will return an error if you set 'restrict' to the same value as the current restricted status of the file.
     *
     * @param sendRestrict whether to include the 'restrict' property in the FileMetaUpdate object
     * @return the FileMetaUpdate object
     */
    public FileMetaUpdate toFileMetaUpdate(boolean sendRestrict) {
        FileMetaUpdate f = restricted != null && sendRestrict ? new FileMetaUpdateWithRestrict(restricted) : new FileMetaUpdate();
        f.setDataFileId(dataFile.getId());
        f.setLabel(label);
        f.setDescription(description);
        if (directoryLabel != null) {
            f.setDirectoryLabel(directoryLabel);
        }
        if (categories != null) {
            f.setCategories(categories);
        }
        return f;
    }

    /**
     * Dataverse uses "restrict" in the <a href="https://guides.dataverse.org/en/latest/api/native-api.html#add-a-file-to-a-dataset">Add File To Dataset</a> API, but returns "restricted" in the
     * response message; "restricted" is ignored by Add File To Dataset. This library works around the confusion by supporting both the "restrict" and "restricted" property, storing them in the same
     * private field.
     *
     * @return whether the file is or should be restricted
     * @see #getRestricted()
     */
    public Boolean getRestrict() {
        return restricted;
    }

    /**
     * See comments in the {@link #getRestrict()} method docs.
     *
     * @param restrict whether to restrict the file or not
     */
    public void setRestrict(Boolean restrict) {
        this.restricted = restrict;
    }

    /**
     * See comments in the {@link #getRestrict()} method docs. *
     *
     * @return whether the file is or should be restricted
     * @see #getRestrict()
     */
    public Boolean getRestricted() {
        return restricted;
    }

    /**
     * See comments in the {@link #getRestrict()} method docs.
     *
     * @param restricted whether to restrict the file or not
     */
    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }
}

