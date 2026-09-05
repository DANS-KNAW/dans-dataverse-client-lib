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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API for Dataverse metadata export endpoints.
 *
 * @see <a href="https://guides.dataverse.org/en/latest/admin/metadataexport.html#batch-exports-through-the-api" target="_blank">Dataverse documentation</a>
 */
@Slf4j
@ToString
public class MetadataExportApi extends AbstractApi {
    private final Path targetBase;

    MetadataExportApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
        this.targetBase = Paths.get("api/admin/metadata/");
    }

    /**
     * Attempts to export all published local datasets that have not been exported yet.
     *
     * @return the response envelope
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Object> exportAll() throws IOException, DataverseException {
        Path path = buildPath(targetBase, "exportAll");
        return httpClientWrapper.get(path, Object.class);
    }

    /**
     * Forces re-export of all published local datasets.
     *
     * @return the response envelope
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Object> reExportAll() throws IOException, DataverseException {
        return reExportAll(null, (String[]) null);
    }

    /**
     * Forces re-export of all published local datasets exported before the supplied date.
     *
     * @param olderThan re-export datasets exported before this date (YYYY-MM-DD)
     * @return the response envelope
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Object> reExportAllOlderThan(String olderThan) throws IOException, DataverseException {
        return reExportAll(olderThan, (String[]) null);
    }

    /**
     * Forces re-export of all published local datasets for the selected metadata formats.
     *
     * @param formats metadata formats to export (for example "Datacite", "croissant")
     * @return the response envelope
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Object> reExportAllFormats(String... formats) throws IOException, DataverseException {
        return reExportAll(null, formats);
    }

    /**
     * Forces re-export of all published local datasets with optional filters.
     *
     * @param olderThan re-export datasets exported before this date (YYYY-MM-DD), or null
     * @param formats   metadata formats to export, or null/empty
     * @return the response envelope
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Object> reExportAll(String olderThan, String... formats) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "reExportAll");
        return httpClientWrapper.get(path, getOptionalFilters(olderThan, formats), Object.class);
    }

    /**
     * Clears metadata export timestamps on published local datasets.
     *
     * @return the response message from Dataverse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<DataMessage> clearExportTimestamps() throws IOException, DataverseException {
        Path path = buildPath(targetBase, "clearExportTimestamps");
        return httpClientWrapper.get(path, DataMessage.class);
    }

    /**
     * Forces re-export of a specific dataset identified by its database id.
     *
     * @param id the database id of the dataset
     * @return the response envelope
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Object> reExportDataset(int id) throws IOException, DataverseException {
        return reExportDataset(Integer.toString(id), false);
    }

    /**
     * Forces re-export of a specific dataset identified by its persistent identifier.
     *
     * @param pid the dataset persistent identifier
     * @return the response envelope
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Object> reExportDataset(String pid) throws IOException, DataverseException {
        return reExportDataset(pid, true);
    }

    /**
     * Forces re-export of a specific dataset identified by its database id for selected formats.
     *
     * @param id      the database id of the dataset
     * @param formats metadata formats to export
     * @return the response envelope
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Object> reExportDataset(int id, String... formats) throws IOException, DataverseException {
        return reExportDataset(Integer.toString(id), false, formats);
    }

    /**
     * Forces re-export of a specific dataset identified by its persistent identifier for selected formats.
     *
     * @param pid     the dataset persistent identifier
     * @param formats metadata formats to export
     * @return the response envelope
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<Object> reExportDataset(String pid, String... formats) throws IOException, DataverseException {
        return reExportDataset(pid, true, formats);
    }

    private DataverseHttpResponse<Object> reExportDataset(String id, boolean isPersistentId, String... formats) throws IOException, DataverseException {
        Path path;
        var queryParameters = new HashMap<String, List<String>>();
        if (isPersistentId) {
            path = buildPath(targetBase, ":persistentId", "reExportDataset");
            queryParameters.put("persistentId", List.of(id));
        }
        else {
            path = buildPath(targetBase, id, "reExportDataset");
        }
        if (formats != null && formats.length > 0) {
            queryParameters.put("formats", List.of(String.join(",", formats)));
        }
        return httpClientWrapper.get(path, queryParameters, Object.class);
    }

    private DataverseHttpResponse<Object> reExportDataset(String id, boolean isPersistentId) throws IOException, DataverseException {
        return reExportDataset(id, isPersistentId, (String[]) null);
    }

    private Map<String, List<String>> getOptionalFilters(String olderThan, String... formats) {
        var parameters = new HashMap<String, List<String>>();
        if (olderThan != null && !olderThan.isBlank()) {
            parameters.put("olderThan", List.of(olderThan));
        }
        if (formats != null && formats.length > 0) {
            parameters.put("formats", List.of(String.join(",", formats)));
        }
        return parameters;
    }
}
