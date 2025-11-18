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
import nl.knaw.dans.lib.dataverse.example.LicensesCreate;
import nl.knaw.dans.lib.dataverse.example.LicensesDelete;
import nl.knaw.dans.lib.dataverse.example.LicensesGetDefault;
import nl.knaw.dans.lib.dataverse.example.LicensesGetSingle;
import nl.knaw.dans.lib.dataverse.example.LicensesList;
import nl.knaw.dans.lib.dataverse.model.license.License;

import java.util.List;
import java.util.UUID;

@Slf4j
public class LicenseSmokeTest extends ExampleBase {
    public static void main(String[] args) throws Exception {
        var msg1 = client.license().getDefaultLicense()
                .getData().getMessage();
        log.info(msg1);

        var name = client.license().getLicenseById(1)
                .getData().getName();
        log.info(name);

        var id = UUID.randomUUID().toString();
        var license = new License();
        license.setName("some name - " + id);
        license.setUri("https://dans.knaw.nl/license/" + id);
        license.setShortDescription("Dans license");
        var msg2 = client.license().addLicense(license)
                .getData().getMessage();
        log.info(msg2);

        var data = client.license().getLicenses()
                .getData();
        int largestId = data.stream()
            .mapToInt(License::getId)
            .max()
            .orElseThrow(() -> new RuntimeException("No licenses found"));
        LicensesDelete.main(List.of(""+largestId).toArray(new String[0]));
    }
}
