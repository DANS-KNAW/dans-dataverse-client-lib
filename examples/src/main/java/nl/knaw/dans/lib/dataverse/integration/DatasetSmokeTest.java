package nl.knaw.dans.lib.dataverse.integration;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.example.DatasetAddFile;
import nl.knaw.dans.lib.dataverse.example.DatasetAssignRole;
import nl.knaw.dans.lib.dataverse.example.DatasetAwaitLock;
import nl.knaw.dans.lib.dataverse.example.DatasetAwaitUnlock;
import nl.knaw.dans.lib.dataverse.example.DatasetDeleteFiles;
import nl.knaw.dans.lib.dataverse.example.DatasetDeleteMetadata;
import nl.knaw.dans.lib.dataverse.example.DatasetDeleteRoleAssignment;
import nl.knaw.dans.lib.dataverse.example.DatasetEditMetadata;
import nl.knaw.dans.lib.dataverse.example.DatasetGetAllVersions;
import nl.knaw.dans.lib.dataverse.example.DatasetGetFiles;
import nl.knaw.dans.lib.dataverse.example.DatasetGetLocks;
import nl.knaw.dans.lib.dataverse.example.DatasetGetVersion;
import nl.knaw.dans.lib.dataverse.example.DatasetListRoleAssignments;
import nl.knaw.dans.lib.dataverse.example.DatasetPublish;
import nl.knaw.dans.lib.dataverse.example.DatasetSubmitForReview;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateFilemetadatas;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateMetadata;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateMetadataFromJsonLd;

import java.util.List;

@Slf4j
public class DatasetSmokeTest {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            log.error("Usage: {} <dataset-DOI> >", FileSmokeTest.class.getSimpleName());
            System.exit(1);
        }
        var persistentId = args[0];
        DatasetGetAllVersions.main(List.of(persistentId).toArray(new String[0]));
        DatasetGetFiles.main(List.of(persistentId, "1").toArray(new String[0]));
        DatasetGetVersion.main(List.of(persistentId, "1").toArray(new String[0]));
        DatasetListRoleAssignments.main(List.of(persistentId).toArray(new String[0]));
        DatasetEditMetadata.main(List.of(persistentId).toArray(new String[0]));
        DatasetUpdateMetadata.main(List.of(persistentId).toArray(new String[0]));
        DatasetUpdateFilemetadatas.main(List.of(args[0], "someKey=someValue").toArray(new String[0]));
        DatasetUpdateMetadataFromJsonLd.main(List.of(args[0], "citation","description json value").toArray(new String[0]));
        DatasetAwaitUnlock.main(List.of(persistentId).toArray(new String[0])); // See its own comment
        DatasetSubmitForReview.main(args);
        DatasetPublish.main(args);
        DatasetGetLocks.getLocks(persistentId); // main wraps a loop sleeping 300 times 0.5 second.
        if (true) {
            // condition outsmarts compiler errors:
            // keep imports
            // do not execute until figured out a scenario
            return;
        }
        DatasetDeleteRoleAssignment.main(List.of(args[0], "9").toArray(new String[0])); // sword publish rights
        DatasetAssignRole.main(List.of(args[0], "user001", "datamanager").toArray(new String[0]));
        DatasetAwaitLock.main(List.of(persistentId).toArray(new String[0])); // TODO throws wait expired
        DatasetDeleteMetadata.main(List.of(persistentId, "This dataset makes use of the ddm:available field to put all files under embargo until a specified date.").toArray(new String[0]));
    }
}
