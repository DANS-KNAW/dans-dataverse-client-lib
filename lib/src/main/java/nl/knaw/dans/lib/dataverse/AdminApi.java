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
import nl.knaw.dans.lib.dataverse.model.DataMessageWithId;
import nl.knaw.dans.lib.dataverse.model.DatasetFileValidationResultList;
import nl.knaw.dans.lib.dataverse.model.banner.BannerMessage;
import nl.knaw.dans.lib.dataverse.model.banner.Messages;
import nl.knaw.dans.lib.dataverse.model.user.AuthenticatedUser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Administration API end-points.
 *
 * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#admin" target="_blank">Dataverse documentation</a>
 */
@Slf4j
@ToString
public class AdminApi extends AbstractApi {

    private final Path targetBase;

    AdminApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
        this.targetBase = Paths.get("api/admin/");
    }

    /**
     * @param id username
     * @return the user
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#list-single-user" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<AuthenticatedUser> listSingleUser(String id) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "authenticatedUsers", id);
        return httpClientWrapper.get(path, new HashMap<>(), new HashMap<>(), AuthenticatedUser.class);
    }

    /**
     * @param key   the settings key
     * @param value the new value
     * @return the result
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/installation/config.html#database-settings" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<Map<String, String>> putDatabaseSetting(String key, String value) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "settings", key);
        return httpClientWrapper.putJsonString(path, value, new HashMap<>(), new HashMap<>(), Map.class);
    }

    /**
     * @param key the settings key
     * @return the result
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/installation/config.html#database-settings" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> getDatabaseSetting(String key) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "settings", key);
        return httpClientWrapper.get(path, DataMessage.class);
    }

    /**
     * The following validates all the physical files in the dataset specified by recalculating the checksums and comparing them against the values saved in the database.
     *
     * @param dbId the dataset database id
     * @return the result
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#physical-files-validation-in-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponseWithoutEnvelope<DatasetFileValidationResultList> validateDatasetFiles(int dbId) throws IOException, DataverseException {
        return validateDatasetFiles(Integer.toString(dbId), false);
    }

    /**
     * Validates all the physical files in the dataset specified by recalculating the checksums and comparing them against the values saved in the database.
     *
     * @param pid the dataset persistent identifier
     * @return the result
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#physical-files-validation-in-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponseWithoutEnvelope<DatasetFileValidationResultList> validateDatasetFiles(String pid) throws IOException, DataverseException {
        return validateDatasetFiles(pid, true);
    }

    /**
     * Validates all the physical files in the dataset specified by recalculating the checksums and comparing them against the values saved in the database.
     *
     * @param id             the dataset id (dbId or pid)
     * @param isPersistentId indicates whether the id is a persistent identifier
     * @return the result
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#physical-files-validation-in-a-dataset" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponseWithoutEnvelope<DatasetFileValidationResultList> validateDatasetFiles(String id, boolean isPersistentId) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "validate/dataset/files");
        var queryParameters = new HashMap<String, List<String>>();
        if (isPersistentId) {
            path = path.resolve(":persistentId");
            queryParameters.put("persistentId", List.of(id));
        }
        else {
            path = path.resolve(id);
        }
        return httpClientWrapper.getWithoutEnvelope(path, queryParameters, new HashMap<>(), DatasetFileValidationResultList.class);
    }

    /**
     * Lists all banner messages.
     *
     * @return the list of banner messages
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#manage-banner-messages" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<List<BannerMessage>> listBannerMessages() throws IOException, DataverseException {
        Path path = buildPath(targetBase, "bannerMessage");
        return httpClientWrapper.get(path, List.class, BannerMessage.class);
    }

    /**
     * Adds a banner message.
     *
     * @param json the banner message in JSON format
     * @return the response
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#manage-banner-messages" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessageWithId> addBannerMessage(String json) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "bannerMessage");
        return httpClientWrapper.postJsonString(path, json, new HashMap<>(), new HashMap<>(), DataMessageWithId.class);
    }

    /**
     * Adds a banner message.
     *
     * @param messages the banner message
     * @return the response
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#manage-banner-messages" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessageWithId> addBannerMessage(Messages messages) throws IOException, DataverseException {
        return addBannerMessage(httpClientWrapper.writeValueAsString(messages));
    }

    /**
     * Deletes a banner message.
     *
     * @param id the banner message id
     * @return the response envelope from Dataverse
     * @throws IOException        when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     * @see <a href="https://guides.dataverse.org/en/latest/api/native-api.html#manage-banner-messages" target="_blank">Dataverse documentation</a>
     */
    public DataverseHttpResponse<DataMessage> deleteBannerMessage(int id) throws IOException, DataverseException {
        Path path = buildPath(targetBase, "bannerMessage", Integer.toString(id));
        return httpClientWrapper.delete(path, new HashMap<>(), DataMessage.class);
    }
}
