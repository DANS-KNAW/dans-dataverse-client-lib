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

import nl.knaw.dans.lib.dataverse.model.dataset.MetadataBlockDefinition;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DataverseApiTest {

    @Test
    void listMetadataBlocks_withoutOptions_usesCollectionScopedEndpoint() throws Exception {
        HttpClientWrapper wrapper = mock(HttpClientWrapper.class);
        @SuppressWarnings("unchecked")
        DataverseHttpResponse<List<MetadataBlockDefinition>> response = mock(DataverseHttpResponse.class);
        doReturn(response).when(wrapper).get(any(), anyMap(), eq(List.class), eq(MetadataBlockDefinition.class));

        DataverseApi api = new DataverseApi(wrapper, "collectionAlias");

        DataverseHttpResponse<List<MetadataBlockDefinition>> result = api.listMetadataBlocks();

        assertSame(response, result);
        verify(wrapper).get(Paths.get("api/dataverses/").resolve("collectionAlias/").resolve("metadatablocks"),
            Collections.emptyMap(), List.class, MetadataBlockDefinition.class);
    }

    @Test
    void listMetadataBlocks_onlyDisplayedOnCreate_addsSingleQueryParameter() throws Exception {
        HttpClientWrapper wrapper = mock(HttpClientWrapper.class);
        @SuppressWarnings("unchecked")
        DataverseHttpResponse<List<MetadataBlockDefinition>> response = mock(DataverseHttpResponse.class);
        doReturn(response).when(wrapper).get(any(), anyMap(), eq(List.class), eq(MetadataBlockDefinition.class));

        DataverseApi api = new DataverseApi(wrapper, "collectionAlias");

        DataverseHttpResponse<List<MetadataBlockDefinition>> result = api.listMetadataBlocks(true);

        Map<String, List<String>> expectedParameters = new HashMap<>();
        expectedParameters.put("onlyDisplayedOnCreate", Collections.singletonList("true"));

        assertSame(response, result);
        verify(wrapper).get(Paths.get("api/dataverses/").resolve("collectionAlias/").resolve("metadatablocks"),
            expectedParameters, List.class, MetadataBlockDefinition.class);
    }

    @Test
    void listMetadataBlocks_withAllOptions_addsAllQueryParameters() throws Exception {
        HttpClientWrapper wrapper = mock(HttpClientWrapper.class);
        @SuppressWarnings("unchecked")
        DataverseHttpResponse<List<MetadataBlockDefinition>> response = mock(DataverseHttpResponse.class);
        doReturn(response).when(wrapper).get(any(), anyMap(), eq(List.class), eq(MetadataBlockDefinition.class));

        DataverseApi api = new DataverseApi(wrapper, "collectionAlias");

        DataverseHttpResponse<List<MetadataBlockDefinition>> result = api.listMetadataBlocks(true, true);

        Map<String, List<String>> expectedParameters = new HashMap<>();
        expectedParameters.put("onlyDisplayedOnCreate", Collections.singletonList("true"));
        expectedParameters.put("returnDatasetFieldTypes", Collections.singletonList("true"));

        assertSame(response, result);
        verify(wrapper).get(Paths.get("api/dataverses/").resolve("collectionAlias/").resolve("metadatablocks"),
            expectedParameters, List.class, MetadataBlockDefinition.class);
    }
}
