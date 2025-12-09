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
import nl.knaw.dans.lib.dataverse.model.RoleAssignment;
import nl.knaw.dans.lib.dataverse.model.dataset.FieldList;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataField;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveSingleValueField;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static nl.knaw.dans.lib.dataverse.example.DatasetUpdateMetadata.getNewCitationMetadataBlock;

@Slf4j
public class DatasetTest extends ExampleBase {
    /**
     * Calls dataset API methods and some methods of other API's that require (or create) the persistent ID of a dataset or file ID's within a dataset.
     *
     * @param args none
     * @throws Exception should not happen
     */
    public static void main(String[] args) throws Exception {

        var dataset = new SmokeTestProperties().readJson("new-dataset.json");

        String persistentId;
        try {
            persistentId = client.dataverse("root")
                .createDataset(dataset, new HashMap<>())
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

        MetadataField titleField = new PrimitiveSingleValueField("title", "updated title value");
        var fieldListWithTitle = new FieldList();
        fieldListWithTitle.add(titleField);
        var citation = client.dataset(persistentId)
            .editMetadata(fieldListWithTitle, true, emptyMap())
            .getData().getMetadataBlocks().get("citation").getFields();
        var nrOfVersions = client.dataset(persistentId)
            .getAllVersions()
            .getData().size();
        log.info("nrOfVersions: {}, Citation with changed title: {}", nrOfVersions, citation);

        MetadataField notesField = new PrimitiveSingleValueField("notesText", "Not mandatory content");
        var fieldListWithNote = new FieldList();
        fieldListWithNote.add(notesField);
        var reducedCitation = client.dataset(persistentId)
            .deleteMetadata(fieldListWithNote, emptyMap())
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

        var enableMsg = client.accessRequests(persistentId)
            .enable().getData().getMessage();
        var disableMsg = client.accessRequests(persistentId)
            .disable().getData().getMessage();
        log.info("Access requests enable msg: {} -- disable msg: {}", enableMsg, disableMsg);

        var storageId = client.admin()
            .validateDatasetFiles(persistentId)
            .getBodyAsObject().getDataFiles().get(0).getStorageIdentifier();
        log.info("Storage identifier of file {}: {}", newFileId, storageId);

        var deleteMsg = client.dataset(persistentId)
            .deleteFiles(List.of(newFileId))
            .getBodyAsString();
        log.info(deleteMsg);

        var fieldObject = Map.of("citation", "description json value");
        var jsonLd = toPrettyJson(fieldObject);
        var r = client.dataset(persistentId)
            .updateMetadataFromJsonLd(jsonLd, true, emptyMap())
            .getData();
        log.info("Updated from JSON-LD, new metadata: {}", r);

        var latest = client.dataset(persistentId)
            .getVersion()
            .getData();
        latest.setTermsOfAccess("Some new terms. Pray I don't alter them any further.");
        latest.setFiles(Collections.emptyList());
        var metadataBlocks = latest.getMetadataBlocks();
        metadataBlocks.put("citation", getNewCitationMetadataBlock());
        latest.setMetadataBlocks(metadataBlocks);
        var internalVersionNumber = client.dataset(persistentId)
            .updateMetadata(latest, emptyMap())
            .getData().getInternalVersionNumber();
        log.info("Version number: {}", internalVersionNumber);
    }
}
