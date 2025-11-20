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
package nl.knaw.dans.lib.dataverse.smoketests;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.example.DataverseCreateDataset;
import nl.knaw.dans.lib.dataverse.example.DataverseDelete;
import nl.knaw.dans.lib.dataverse.example.SearchFind;
import nl.knaw.dans.lib.dataverse.example.SearchIterator;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class SearchTest extends ExampleBase {
    public static void main(String[] args) throws Exception {

        // make sure there is some data to find, regardless of the content of the Dataverse installation
        var dataset = DataverseCreateDataset.getDataset("Description for smoky search queries.");
        var persistentId = client.dataverse(new Properties().getProperty("dataverseAlias")).createDataset(dataset, new HashMap<>())
            .getData().getPersistentId();

        // the following test should both show the dataset above
        SearchFind.main(List.of("smoky").toArray(new String[0]));
        SearchIterator.main(List.of("queries").toArray(new String[0]));

        // See example classes for search options.
        // They may not return other data structures
        // and thus have no added value in this smoke test.

        // clean up
        var message = client.dataset(persistentId).deleteDraft().getData().getMessage();
        log.info("Deleted dataset {} with message: {}", persistentId, message);
    }
}
