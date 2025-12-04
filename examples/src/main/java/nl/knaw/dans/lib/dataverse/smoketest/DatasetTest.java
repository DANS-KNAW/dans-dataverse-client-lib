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
package nl.knaw.dans.lib.dataverse.smoketest;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.DataverseException;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.SmokeTestProperties;
import nl.knaw.dans.lib.dataverse.Version;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateMetadata;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateMetadataFromJsonLd;
import nl.knaw.dans.lib.dataverse.example.DataverseCreateDataset;
import nl.knaw.dans.lib.dataverse.model.RoleAssignment;
import nl.knaw.dans.lib.dataverse.model.dataset.FieldList;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataField;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveSingleValueField;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.emptyMap;

@Slf4j
public class DatasetTest extends ExampleBase {
    /**
     * Calls dataset API methods and some methods of other API's that require (or create) the persistent ID of a dataset or file ID's within a dataset.
     *
     * @param args none
     * @throws Exception should not happen
     */
    public static void main(String[] args) throws Exception {

        String persistentId;
        try {
            persistentId = client.dataverse("root")
                .createDataset(DataverseCreateDataset.getDataset("Test description"), new HashMap<>())
                .getData().getPersistentId();
        } catch (DataverseException e) {
            log.error("Could not create dataset, aborting test", e);
            warnForCustomMetadataBlocks(e);
            return;
        }

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
        roleAssignment.setAssignee("@" + new SmokeTestProperties().getProperty("username"));
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

        var fileMeta1 = new FileMeta();
        fileMeta1.setLabel("some_file.md");
        var fileToAdd = ExampleBase.getExamplesRoot().resolve("../docs/getting-started.md");
        var fileForReplace = ExampleBase.getExamplesRoot().resolve("../docs/api.md");
        var dataFile = client.dataset(persistentId)
            .addFile(fileToAdd, fileMeta1)
            .getData().getFiles().get(0).getDataFile();
        var contentType = client.dataset(persistentId)
            .getFiles(Version.DRAFT.toString())
            .getData().get(0).getDataFile().getContentType();
        fileMeta1.setDirectoryLabel("some_dir");
        fileMeta1.setRestricted(true);
        fileMeta1.setDataFile(dataFile);
        var updateMsg = client.dataset(persistentId)
            .updateFileMetadatas(List.of(fileMeta1.toFileMetaUpdate()))
            .getBodyAsString();
        log.info("Added file id: {} File content type: {}, updateMsg: {}", dataFile.getId(), contentType, updateMsg);

        var firstLine = client.basicFileAccess(dataFile.getId()).getFile(r -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(r.getEntity().getContent()))) {
                return reader.readLine();
            }
        });
        var fileMeta2 = new FileMeta();
        fileMeta2.setLabel("replaced_label");
        var newFileId = client.file(dataFile.getId())
            .replaceFile(fileForReplace, fileMeta2)
            .getData().getFiles().get(0).getDataFile().getId();
        var fileMeta3 = new FileMeta();
        fileMeta3.setDirectoryLabel("replaced_dir");
        var updateMsg2 = client.file(newFileId)
            .updateMetadata(fileMeta3)
            .getBodyAsString();
        log.info("first original line of {} : {}, updateMsg: {}", dataFile.getId(), firstLine, updateMsg2);
        var validation = client.admin()
            .validateDatasetFiles(persistentId)
            .getBodyAsObject().getDataFiles().get(0);
        log.info("storage ID: {}, status: {}", validation.getStorageIdentifier(), validation.getStatus());
        var deleteMsg = client.dataset(persistentId)
            .deleteFiles(List.of(newFileId))
            .getBodyAsString();
        log.info(deleteMsg);

        // TODO too much logging: isolate direct API call from example
        DatasetUpdateMetadata.main(List.of(persistentId).toArray(new String[0]));
        DatasetUpdateMetadataFromJsonLd.main(List.of(persistentId, "citation", "description json value").toArray(new String[0]));
    }

    private static FieldList toFieldList(MetadataField... fields) {
        var fieldList = new FieldList();
        for (var field : fields) {
            fieldList.add(field);
        }
        return fieldList;
    }
}
