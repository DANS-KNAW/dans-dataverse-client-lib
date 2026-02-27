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
import nl.knaw.dans.lib.dataverse.model.StorageDriver;

import java.util.List;
import java.util.Map;

@Slf4j
public class AdminGetAvailableStorageDrivers extends ExampleBase {

    public static void main(String[] args) throws Exception {
        DataverseResponse<Map<String, String>> r = client.admin().getAvailableStorageDrivers();
        log.info("Response envelope: {}", r.getEnvelopeAsJson().toPrettyString());
        for (var driver : r.getData().entrySet()) {
            log.info("- label: {}, id: {}", driver.getKey(), driver.getValue());
        }
    }
}
