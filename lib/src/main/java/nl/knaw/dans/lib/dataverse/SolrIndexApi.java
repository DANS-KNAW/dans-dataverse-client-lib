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

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.DataMessage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API for Dataverse Solr index endpoints.
 *
 * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#solr-index" target="_blank">Dataverse documentation</a>
 */
@Slf4j
@ToString
public class SolrIndexApi extends AbstractApi {

    private final Path targetBase;

    SolrIndexApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
        this.targetBase = Paths.get("api/admin/index/");
    }

    /**
     * Triggers a full reindex of all content in the Dataverse installation.
     *
     * @return the response message from Dataverse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#solr-index" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> indexAll() throws IOException, DataverseException {
        return httpClientWrapper.get(targetBase, DataMessage.class);
    }

    /**
     * Returns the current status of the Solr index.
     *
     * @return the index status message
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#solr-index" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> status() throws IOException, DataverseException {
        Path path = buildPath(targetBase, "status");
        return httpClientWrapper.get(path, DataMessage.class);
    }

    /**
     * Indexes a specific dataset identified by its persistent identifier.
     *
     * @param persistentId the persistent identifier of the dataset (e.g. doi:10.5072/FK2/ABCDEF)
     * @return the response message from Dataverse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#solr-index" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> indexDataset(String persistentId) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "dataset");
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("persistentId", Collections.singletonList(persistentId));
        return httpClientWrapper.get(path, parameters, DataMessage.class);
    }

    /**
     * Indexes a specific dataset identified by its database id.
     *
     * @param id the database id of the dataset
     * @return the response message from Dataverse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#solr-index" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> indexDataset(int id) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "dataset", Integer.toString(id));
        return httpClientWrapper.get(path, DataMessage.class);
    }

    /**
     * Indexes a specific dataverse identified by its database id.
     *
     * @param id the database id of the dataverse
     * @return the response message from Dataverse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#solr-index" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> indexDataverse(int id) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "dataverse", Integer.toString(id));
        return httpClientWrapper.get(path, DataMessage.class);
    }

    /**
     * Clears (deletes) the Solr index.
     *
     * @return the response message from Dataverse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#solr-index" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> clear() throws IOException, DataverseException {
        return httpClientWrapper.delete(targetBase, new HashMap<>(), DataMessage.class);
    }

    /**
     * Clears the index timestamps for all objects, causing them to be re-indexed on the next indexing run.
     *
     * @return the response message from Dataverse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#solr-index" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> clearTimestamps() throws IOException, DataverseException {
        Path path = buildPath(targetBase, "timestamps");
        return httpClientWrapper.delete(path, new HashMap<>(), DataMessage.class);
    }
}
