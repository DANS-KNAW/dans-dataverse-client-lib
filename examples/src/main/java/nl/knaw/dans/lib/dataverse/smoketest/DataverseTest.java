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
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.SmokeTestProperties;

import java.util.HashMap;
import java.util.UUID;

import static nl.knaw.dans.lib.dataverse.example.DataverseCreate.getDataverse;

@Slf4j
public class DataverseTest extends ExampleBase {
    public static void main(String[] args) throws Exception {
        var alias = new SmokeTestProperties().getProperty("dataverseAlias");

        var type = client.dataverse(alias)
            .getContents()
            .getData().get(0).getType();
        log.info("First item in dataverse {} is of type {}", alias, type);

        var sizeMsg = client.dataverse(alias)
            .getStorageSize()
            .getData().getMessage();
        log.info("Storage size of dataverse {}: {}", alias, sizeMsg);

        var assignee = client.dataverse("root")
            .listRoleAssignments()
            .getData().get(0).getAssignee();
        log.info("First role assignment in root dataverse is {}, assigned to {}", alias, assignee);

        var roleDescription = client.dataverse("root")
            .listRoles()
            .getData().get(0).getDescription();
        log.info("First role in root dataverse is {}", roleDescription);

        var description = client.dataverse("root")
            .view()
            .getData().getDescription();
        log.info("Root dataverse description: {}", description);

        var dataset = new SmokeTestProperties().readJson("new-dataset.json");
        var uuid = UUID.randomUUID();
        var doi = String.format("doi:10.5072/DAR/IMPORTTEST-%s", uuid);
        var id = client.dataverse(alias)
            .importDataset(dataset, doi, false, new HashMap<>())
            .getData().getId();
        log.info("Imported dataset with id {}", id);

        var isRoot = client.dataverse(alias)
            .isMetadataBlocksRoot()
            .getData();
        log.info("Dataverse {} is metadata blocks root: {}", alias, isRoot);

        var subAlias = client.dataverse(alias)
            .create(getDataverse())
            .getData().getAlias();
        log.info("Created sub-dataverse with alias {}", subAlias);

        var changedIsRootMsg = client.dataverse(subAlias)
            .setMetadataBlocksRoot(true)
            .getData().getMessage();
        log.info("Set sub-dataverse {} as metadata blocks root: {}", subAlias, changedIsRootMsg);

        var contacts = client.dataverse(subAlias)
            .publish()
            .getData().getDataverseContacts();
        log.info("Dataverse {} has {} contacts", subAlias, contacts.size());

        var deleteMsg = client.dataverse(subAlias)
            .delete()
            .getData().getMessage();
        log.info("Deleted dataverse {}: {}", subAlias, deleteMsg);
    }
}
