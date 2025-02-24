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
package nl.knaw.dans.lib.dataverse.example;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.Version;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DatasetUpdateFilemetadatas extends ExampleBase {
    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        // Parse the rest of the arguments as key-value pairs, formatted key=value.
        // key is the label, and value the description to set for the file.
        Map<String, String> fileDescriptions = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            String[] parts = args[i].split("=");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid key-value pair: " + args[i]);
            }
            fileDescriptions.put(parts[0], parts[1]);
        }
        // Look up the filemetas pointed to be the filepaths.
        var result1 = client.dataset(persistentId).getFiles(Version.LATEST.toString());
        List<FileMeta> fileMetas = result1.getData();
        List<FileMeta> fileMetasToUpdate = new ArrayList<>();
        for (FileMeta fileMeta : fileMetas) {
            if (fileDescriptions.containsKey(fileMeta.getLabel())) {
                fileMeta.setDescription(fileDescriptions.get(fileMeta.getLabel()));
                fileMetasToUpdate.add(fileMeta);
            }
        }
        var result2 = client.dataset(persistentId).updateFileMetadatas(fileMetasToUpdate.stream().map(FileMeta::toFileMetaUpdate).toList());
        log.info("Response message: {}", result2.getEnvelopeAsJson().toPrettyString());

    }
}
