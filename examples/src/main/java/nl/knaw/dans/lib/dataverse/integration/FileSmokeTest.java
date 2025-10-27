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
import nl.knaw.dans.lib.dataverse.example.BasicFileAccessGetFile;
import nl.knaw.dans.lib.dataverse.example.DatasetAddFile;
import nl.knaw.dans.lib.dataverse.example.DatasetDeleteFiles;
import nl.knaw.dans.lib.dataverse.example.DatasetGetFiles;
import nl.knaw.dans.lib.dataverse.example.DatasetUpdateFilemetadatas;
import nl.knaw.dans.lib.dataverse.example.FileReplace;
import nl.knaw.dans.lib.dataverse.example.FileUpdateMetadata;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class FileSmokeTest {
    public static void main(String[] args) throws Exception {
        if (args.length < 6) {
            log.error("Usage: {} <dataset-DOI> <destination> <file-id> ...", FileSmokeTest.class.getSimpleName());
            log.error("<destination> for BasicFileAccessGetFile");
            log.error("first <file-id> should be a text file to be overridden with resources/test.txt");
            System.exit(1);
        }
        var persistentId = args[0];
        var destination = args[1];
        var fileIds = Arrays.copyOfRange(args, 3, args.length);
        var fileToUpload1 = new File("README.md").getAbsolutePath();
        var fileToUpload2 = new File("examples/src/main/resources/test.txt").getAbsolutePath();

        DatasetGetFiles.main(List.of(persistentId, "1").toArray(new String[0]));
        BasicFileAccessGetFile.main(List.of(fileIds[0], destination).toArray(new String[0])); // TODO range
        DatasetAddFile.main(List.of(persistentId, fileToUpload1).toArray(new String[0]));
        FileReplace.main(List.of(fileIds[0], fileToUpload2).toArray(new String[0]));

        DatasetDeleteFiles.main(List.of(persistentId, fileIds[2]).toArray(new String[0]));
        FileUpdateMetadata.main(List.of(fileIds[1], "newName", "newFolder").toArray(new String[0]));
        DatasetUpdateFilemetadatas.main(List.of(persistentId, "someKey=someValue").toArray(new String[0]));
    }
}
