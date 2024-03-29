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
package nl.knaw.dans.lib.dataverse;

import nl.knaw.dans.lib.dataverse.model.DataverseEnvelope;
import nl.knaw.dans.lib.dataverse.model.ModelFixture;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetLatestVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnvelopeWithSeriesTest extends ModelFixture {

    private static class EnvelopeWithSeries extends DataverseEnvelope<DatasetLatestVersion> {
    }

    private static final Class<?> classUnderTest = EnvelopeWithSeries.class;

    @Test
    public void canDeserialize() throws Exception {
        EnvelopeWithSeries e = mapper.readValue(getTestJsonFileFor(classUnderTest), EnvelopeWithSeries.class);
        assertEquals("DRAFT",e.getData().getLatestVersion().getVersionState());

        var field = e.getData().getLatestVersion().getMetadataBlocks()
            .get("citation").getFields().stream()
            .filter(f -> "seriesInformation".equals(f.getTypeName()));
    }

    @Test
    public void roundTrip() throws Exception {
        EnvelopeWithSeries e = roundTrip(getTestJsonFileFor(classUnderTest), EnvelopeWithSeries.class);
        assertEquals(classUnderTest, e.getClass());
    }
}