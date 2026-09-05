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
import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;

@Slf4j
public class MetadataExportReExportAll extends ExampleBase {
    public static void main(String[] args) throws Exception {
        DataverseResponse<Object> r;
        if (args.length == 0) {
            r = client.metadataExport().reExportAll();
        }
        else if (args.length == 1) {
            r = client.metadataExport().reExportAllOlderThan(args[0]);
        }
        else {
            r = client.metadataExport().reExportAll(args[0], args[1].split(","));
        }
        log.info("Response envelope: {}", r.getEnvelopeAsJson().toPrettyString());
        log.info("Status: {}", r.getEnvelope().getStatus());
    }
}
