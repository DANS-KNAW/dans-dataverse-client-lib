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

import nl.knaw.dans.lib.dataverse.CompoundFieldBuilder;
import nl.knaw.dans.lib.dataverse.DataverseException;
import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.dataset.CompoundField;
import nl.knaw.dans.lib.dataverse.model.dataset.ControlledMultiValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import nl.knaw.dans.lib.dataverse.model.dataset.FieldList;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataBlock;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveMultiValueField;
import nl.knaw.dans.lib.dataverse.model.dataset.PrimitiveSingleValueField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DatasetUpdateMetadata extends ExampleBase {

    private static final Logger log = LoggerFactory.getLogger(DatasetUpdateMetadata.class);

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];

        MetadataBlock citation = new MetadataBlock();
        citation.setDisplayName("Citation Metadata");
        citation.setName("citation");
        citation.setFields(Arrays.asList(
            new PrimitiveSingleValueField("title", "My New Title"),
            new CompoundFieldBuilder("author", true)
                .addSubfield("authorName", "A. Thor")
                .addSubfield("authorAffiliation", "Walhalla")
                .addSubfield("authorEmail", "thor@asgard.no")
                .build(),
            new CompoundFieldBuilder("dsDescription", true)
                .addSubfield("dsDescriptionValue", "My new description value")
                .addSubfield("dsDescriptionDate", "2022-01-01")
                .build(),
            new CompoundFieldBuilder("datasetContact", true)
                .addSubfield("datasetContactName", "NEW CONTACT NAME")
                .addSubfield("datasetContactEmail", "NEW_CONTACT@example.com")
                .build(),
            new ControlledMultiValueField("subject", Collections.singletonList("Chemistry"))
        ));

        // Note that the dataset must be in draft state, otherwise it cannot be updated through this API.
        // You may initiate a draft for a new version by making a trivial change to the metadata using the editMetadata API
        try {

            DataverseResponse<DatasetVersion> r = client.dataset(persistentId).updateMetadata(Collections.singletonMap("citation", citation));
            log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());
            log.info("Version number: {}", r.getData().getVersionNumber());
        } catch (DataverseException e) {
            System.out.println(e.getMessage());
        }
    }
}
