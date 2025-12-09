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
import nl.knaw.dans.lib.dataverse.SearchOptions;
import nl.knaw.dans.lib.dataverse.SmokeTestProperties;
import nl.knaw.dans.lib.dataverse.model.dataset.FieldList;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveSingleValueField;
import nl.knaw.dans.lib.dataverse.model.search.DatasetResultItem;
import nl.knaw.dans.lib.dataverse.model.search.SearchItemType;

import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;
import static java.util.Collections.emptyMap;

@Slf4j
public class SearchTest extends ExampleBase {
    public static void main(String[] args) throws Exception {
        var properties = new SmokeTestProperties();
        var alias = properties.getProperty("dataverseAlias");
        var dataset = properties.readJson("new-dataset.json");

        // make sure there is some data to find, regardless of the content of the Dataverse installation
        var fieldList = new FieldList();
        fieldList.add(new PrimitiveSingleValueField("title", "A dataset for smoky search queries"));
        var persistentId = client.dataverse(alias)
            .createDataset(dataset, new HashMap<>())
            .getData().getPersistentId();
        var citation = client.dataset(persistentId)
            .editMetadata(fieldList, true, emptyMap())
            .getData().getMetadataBlocks().get("citation").getFields();
        log.info("Created dataset with persistentId {}", persistentId);
        sleep(4000); // wait a bit for the search index to be updated

        var options = new SearchOptions();
        options.setTypes(List.of(SearchItemType.dataset));

        // the following tests should both show at least the dataset created above

        var smokyResult = client.search()
            .find("smoky", options)
            .getData();
        log.info("SearchFind found {} results for 'smoky'", smokyResult.getTotalCount());
        if (smokyResult.getTotalCount() == 0) {
            throw new RuntimeException("Expected at least one search result for query 'smoky'");
        }

        for (var item : smokyResult.getItems()) {
            log.info(((DatasetResultItem) item).getGlobalId());
        }

        var iterator = client.search()
            .iterator("queries", options);
        log.info("SearchFind found results {} for 'queries'", iterator.hasNext());
        if (!iterator.hasNext()) {
            throw new RuntimeException("Expected at least one search result for query 'queries'");
        }
        while (iterator.hasNext()) {
            var item = (DatasetResultItem) iterator.next();
            log.info((item).getGlobalId());
        }

        // clean up
        var message = client.dataset(persistentId)
            .deleteDraft()
            .getData().getMessage();
        log.info("Deleted dataset {} with message: {}", persistentId, message);
    }
}
