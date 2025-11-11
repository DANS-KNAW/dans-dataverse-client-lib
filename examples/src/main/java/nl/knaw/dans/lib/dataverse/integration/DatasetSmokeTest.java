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
package nl.knaw.dans.lib.dataverse.integration;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.Version;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateMetadata;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateMetadataFromJsonLd;
import nl.knaw.dans.lib.dataverse.example.DataverseCreateDataset;
import nl.knaw.dans.lib.dataverse.model.RoleAssignment;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveSingleValueField;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.emptyMap;
import static nl.knaw.dans.lib.dataverse.MetadataUtil.toFieldList;

@Slf4j
public class DatasetSmokeTest extends ExampleBase {
    public static void main(String[] args) throws Exception {

        var persistentId = client.dataverse("root")
            .createDataset(DataverseCreateDataset.getDataset(), new HashMap<String, String>())
            .getData().getPersistentId();

        var isInReview = client.dataset(persistentId)
            .submitForReview()
            .getData().getInReview();
        var lockType = client.dataset(persistentId)
            .getLocks()
            .getData().get(0).getLockType();
        var datasetType = client.dataset(persistentId)
            .publish()
            .getData().getDatasetType();

        while (true) {
            // wait until no longer DRAFT
            Thread.sleep(200);
            var versionState = client.dataset(persistentId)
                .getVersion()
                .getData().getVersionState();
            log.info("Current version state: {}", versionState);
            if (!"DRAFT".equals(versionState)) {
                break;
            }
        }
        log.info("Dataset {}, inReview was: {}, datasetType: {}, lockType was: {}", persistentId, isInReview, datasetType, lockType);

        RoleAssignment roleAssignment = new RoleAssignment();
        roleAssignment.setAssignee("@user001");
        roleAssignment.setRole("fileDownloader");
        var roleAssignmentId = client.dataset(persistentId)
            .assignRole(roleAssignment)
            .getData().getId();
        var firstAssignee = client.dataset(persistentId)
            .listRoleAssignments()
            .getData().get(0).getAssignee();// might be another than just created
        var deleteMessage = client.dataset(persistentId)
            .deleteRoleAssignment(roleAssignmentId)
            .getData().getMessage();
        log.info("Deleted role assignment id {} fistAssignee {}: {}", roleAssignmentId, firstAssignee, deleteMessage);

        var newTitle = toFieldList(new PrimitiveSingleValueField("title", "updated title value"));
        var citation = client.dataset(persistentId)
            .editMetadata(newTitle, true, emptyMap())
            .getData().getMetadataBlocks().get("citation").getFields();
        var nrOfVersions = client.dataset(persistentId)
            .getAllVersions()
            .getData().size();
        log.info("nrOfVersions: {}, Citation with changed title: {}", nrOfVersions, citation);

        var noteField = toFieldList(new PrimitiveSingleValueField("notesText", "Not mandatory content"));
        var reducedCitation = client.dataset(persistentId)
            .deleteMetadata(noteField, emptyMap())
            .getData().getMetadataBlocks().get("citation").getFields();
        log.info("Citation without note: {}", reducedCitation);

        var fileMeta = new FileMeta();
        fileMeta.setLabel("some_file.md");
        var fileToAdd = new File("README.md").getAbsoluteFile().toPath();
        var dataFile = client.dataset(persistentId)
            .addFile(fileToAdd, fileMeta)
            .getData().getFiles().get(0).getDataFile();
        var contentType = client.dataset(persistentId)
            .getFiles(Version.DRAFT.toString())
            .getData().get(0).getDataFile().getContentType();
        fileMeta.setDirectoryLabel("some_dir");
        fileMeta.setRestricted(true);
        fileMeta.setDataFile(dataFile);
        var updateMsg = client.dataset(persistentId)
            .updateFileMetadatas(List.of(fileMeta.toFileMetaUpdate()))
            .getBodyAsString();
        // TODO what is left in FileSmokeTest
        log.info("Added file id: {} File content type: {}, updateMsg: {}", dataFile.getId(), contentType, updateMsg);

        // TODO too much logging: isolate direct API call from example
        DatasetUpdateMetadata.main(List.of(persistentId).toArray(new String[0]));
        DatasetUpdateMetadataFromJsonLd.main(List.of(persistentId, "citation", "description json value").toArray(new String[0]));
    }
}
