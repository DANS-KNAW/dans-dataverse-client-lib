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
package nl.knaw.dans.lib.dataverse.model.dataset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatasetMetadataTest extends ModelDatasetMapperFixture {
    private static final Class<DatasetMetadata> classUnderTest = DatasetMetadata.class;

    @Test
    public void canDeserialize() throws Exception {
        DatasetMetadata de = mapper.readValue(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(classUnderTest, de.getClass());
        assertEquals("file://10.5072/DAR/LDIU4X", de.getStorageIdentifier());
        assertEquals("DANS Archaeology Data Station (dev)", de.getPublisher());
        assertEquals("2022-03-24", de.getPublicationDate());
        assertEquals(1, de.getLatestVersion().getFiles().size());
    }

    @Test
    public void roundTrip() throws Exception {
        DatasetMetadata mb = roundTrip(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(classUnderTest, mb.getClass());
    }

}