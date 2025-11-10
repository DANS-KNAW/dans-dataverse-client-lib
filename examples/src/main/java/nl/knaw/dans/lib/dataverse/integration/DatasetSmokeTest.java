package nl.knaw.dans.lib.dataverse.integration;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.DataverseException;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.Version;
import nl.knaw.dans.lib.dataverse.example.DatasetAwaitLock;
import nl.knaw.dans.lib.dataverse.example.DatasetAwaitUnlock;
import nl.knaw.dans.lib.dataverse.example.DatasetDeleteMetadata;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateFilemetadatas;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateMetadata;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateMetadataFromJsonLd;
import nl.knaw.dans.lib.dataverse.example.DataverseCreateDataset;
import nl.knaw.dans.lib.dataverse.model.RoleAssignment;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetType;
import nl.knaw.dans.lib.dataverse.model.dataset.FieldList;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveSingleValueField;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.emptyMap;

@Slf4j
public class DatasetSmokeTest extends ExampleBase {
    public static void main(String[] args) throws Exception {

        var persistentId = client.dataverse("root")
            .createDataset(DataverseCreateDataset.getDataset(), new HashMap<String, String>())
            .getData().getPersistentId();

        var isInReview = client.dataset(persistentId)
            .submitForReview()
            .getData().getInReview();
        var datasetType = publish(persistentId);
        log.info("Dataset {} inReview: {}, datasetType: {}", persistentId, isInReview, datasetType);

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

        var metadataBlocks = client.dataset(persistentId)
            .getAllVersions()
            .getData().get(0).getMetadataBlocks().keySet();
        log.info("Metadata blocks in dataset: {}", metadataBlocks);

        var fieldList = new FieldList();
        fieldList.add(new PrimitiveSingleValueField("title", "updated title value"));
        var citation = client.dataset(persistentId)
            .editMetadata(fieldList, true, emptyMap())
            .getData().getCitation();
        log.info("Updated citation: {}", citation);

        var fileMeta = new FileMeta();
        fileMeta.setLabel("Alternative_label");
        fileMeta.setRestricted(true);
        var fileToAdd = new File("README.md").getAbsoluteFile().toPath();
        var fileId = client.dataset(persistentId)
            .addFile(fileToAdd, fileMeta)
            .getData().getFiles().get(0).getDataFile().getId();
        log.info("Added file id: {}", fileId);

        var contentType = client.dataset(persistentId).getFiles(Version.DRAFT.toString())
            .getData().get(0).getDataFile().getContentType();
        log.info("File content type: {}", contentType);
        // TODO rest of DatasetUpdateFilemetadatas

        DatasetUpdateFilemetadatas.main(List.of(persistentId, "someKey=someValue").toArray(new String[0])); // TODO updates nothing
        DatasetAwaitUnlock.main(List.of(persistentId).toArray(new String[0])); // See its own comment
        client.dataset(persistentId).getLocks().getData();

        DatasetUpdateMetadata.main(List.of(persistentId).toArray(new String[0]));
        DatasetUpdateMetadataFromJsonLd.main(List.of(persistentId, "citation", "description json value").toArray(new String[0]));
        DatasetDeleteMetadata.main(List.of(persistentId, "My new description value (2022-01-01)").toArray(new String[0])); // TODO not found
        DatasetAwaitLock.main(List.of(persistentId).toArray(new String[0])); // TODO throws wait expired
    }

    private static DatasetType publish(String persistentId) throws IOException, DataverseException, InterruptedException {
        var datasetType = client.dataset(persistentId)
            .publish()
            .getData().getDatasetType();
        while (true) {
            // wait until no longer DRAFT
            Thread.sleep(200);
            var versionState = client.dataset(persistentId)
                .getVersion().getData().getVersionState();
            log.info("Current version state: {}", versionState);
            if (!"DRAFT".equals(versionState)) {
                break;
            }
        }
        return datasetType;
    }
}
