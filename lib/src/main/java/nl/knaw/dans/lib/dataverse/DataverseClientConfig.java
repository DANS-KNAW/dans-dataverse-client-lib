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

import lombok.AccessLevel;
import lombok.Getter;
import nl.knaw.dans.lib.dataverse.model.dataset.UpdateType;

import java.net.URI;

public class DataverseClientConfig {
    public static final int DEFAULT_AWAIT_LOCK_STATE_MAX_NUMBER_OF_RETRIES = 30;
    public static final int DEFAULT_AWAIT_LOCK_STATE_MILLISECONDS_BETWEEN_RETRIES = 500;
    public static final int DEFAULT_AWAIT_INDEXING_MAX_NUMBER_OF_RETRIES = 15;
    public static final int DEFAULT_AWAIT_INDEXING_MILLISECONDS_BETWEEN_RETRIES = 1000;

    @Getter(AccessLevel.PACKAGE)
    private final URI baseUrl;
    @Getter(AccessLevel.PACKAGE)
    private final String apiToken;
    @Getter(AccessLevel.PACKAGE)
    private final int awaitLockStateMaxNumberOfRetries;
    @Getter(AccessLevel.PACKAGE)
    private final int awaitLockStateMillisecondsBetweenRetries;
    @Getter(AccessLevel.PACKAGE)
    private final int awaitIndexingMaxNumberOfRetries;
    @Getter(AccessLevel.PACKAGE)
    private final int awaitIndexingMillisecondsBetweenRetries;

    @Getter(AccessLevel.PACKAGE)
    private final String unblockKey;

    // Optional database connectivity
    @Getter(AccessLevel.PACKAGE)
    private final String databaseUrl;
    @Getter(AccessLevel.PACKAGE)
    private final String databaseUser;
    @Getter(AccessLevel.PACKAGE)
    private final String databasePassword;

    /**
     * Configuration settings for the {@link DataverseClient}.
     *
     * @param baseUrl                                  the base URL of the Dataverse server to communicate with
     * @param apiToken                                 the API token used for authorization
     * @param awaitLockStateMaxNumberOfRetries         the maximum number of tries for {@link DatasetApi#awaitLock(String)} API (default {@value #DEFAULT_AWAIT_LOCK_STATE_MAX_NUMBER_OF_RETRIES})
     * @param awaitLockStateMillisecondsBetweenRetries the number of milliseconds to wait between tries for {@link DatasetApi#awaitLock(String)} API (default
     *                                                 {@value #DEFAULT_AWAIT_LOCK_STATE_MILLISECONDS_BETWEEN_RETRIES})
     * @param awaitIndexingMaxNumberOfRetries          the maximum number of tries for {@link DatasetApi#publish(UpdateType, boolean)} API (default
     *                                                 {@value #DEFAULT_AWAIT_INDEXING_MAX_NUMBER_OF_RETRIES})
     * @param awaitIndexingMillisecondsBetweenRetries  the number of milliseconds to wait between tries for {@link DatasetApi#publish(UpdateType, boolean)} API (default
     *                                                 {@value #DEFAULT_AWAIT_INDEXING_MILLISECONDS_BETWEEN_RETRIES})
     * @param unblockKey                               a key required for admin tasks when not running on localhost
     * @param databaseUrl                              JDBC URL for direct database access (optional)
     * @param databaseUser                             database username (optional)
     * @param databasePassword                         database password (optional)
     */
    public DataverseClientConfig(URI baseUrl, String apiToken,
        int awaitLockStateMaxNumberOfRetries, int awaitLockStateMillisecondsBetweenRetries,
        int awaitIndexingMaxNumberOfRetries, int awaitIndexingMillisecondsBetweenRetries,
        String unblockKey,
        String databaseUrl, String databaseUser, String databasePassword) {
        this.baseUrl = baseUrl;
        this.apiToken = apiToken;
        this.awaitLockStateMaxNumberOfRetries = awaitLockStateMaxNumberOfRetries;
        this.awaitLockStateMillisecondsBetweenRetries = awaitLockStateMillisecondsBetweenRetries;
        this.awaitIndexingMaxNumberOfRetries = awaitIndexingMaxNumberOfRetries;
        this.awaitIndexingMillisecondsBetweenRetries = awaitIndexingMillisecondsBetweenRetries;
        this.unblockKey = unblockKey;
        this.databaseUrl = databaseUrl;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
    }

    /**
     * Configuration settings for the {@link DataverseClient}.
     *
     * @param baseUrl    the base URL of the Dataverse server to communicate with
     * @param apiToken   the API token used for authorization
     * @param unblockKey a key required for admin tasks when not running on localhost
     * @param databaseUrl      JDBC URL for direct database access (optional)
     * @param databaseUser     database username (optional)
     * @param databasePassword database password (optional)
     */
    public DataverseClientConfig(URI baseUrl, String apiToken, String unblockKey,
        String databaseUrl, String databaseUser, String databasePassword) {
        this(baseUrl, apiToken,
            DEFAULT_AWAIT_LOCK_STATE_MAX_NUMBER_OF_RETRIES, DEFAULT_AWAIT_LOCK_STATE_MILLISECONDS_BETWEEN_RETRIES,
            DEFAULT_AWAIT_INDEXING_MAX_NUMBER_OF_RETRIES, DEFAULT_AWAIT_INDEXING_MILLISECONDS_BETWEEN_RETRIES,
            unblockKey,
            databaseUrl, databaseUser, databasePassword);
    }

    /**
     * Configuration settings for the {@link DataverseClient}.
     *
     * @param baseUrl  the base URL of the Dataverse server to communicate with
     * @param apiToken the API token used for authorization
     */
    public DataverseClientConfig(URI baseUrl, String apiToken) {
        this(baseUrl, apiToken,
            DEFAULT_AWAIT_LOCK_STATE_MAX_NUMBER_OF_RETRIES, DEFAULT_AWAIT_LOCK_STATE_MILLISECONDS_BETWEEN_RETRIES,
            DEFAULT_AWAIT_INDEXING_MAX_NUMBER_OF_RETRIES, DEFAULT_AWAIT_INDEXING_MILLISECONDS_BETWEEN_RETRIES,
            null,
            null, null, null);
    }

    /**
     * Configuration settings for the {@link DataverseClient}. No API token is specified, so the client will only be able to access endpoints that require no account.
     *
     * @param baseUrl the base URL of the Dataverse server to communicate with
     */
    public DataverseClientConfig(URI baseUrl) {
        this(baseUrl, null);
    }

    /**
     * Configuration settings for the {@link DataverseClient}.
     *
     * @param baseUrl    the base URL of the Dataverse server to communicate with
     * @param apiToken   the API token used for authorization
     * @param unblockKey a key required for admin tasks when not running on localhost
     */
    public DataverseClientConfig(URI baseUrl, String apiToken, String unblockKey) {
        this(baseUrl, apiToken,
            DEFAULT_AWAIT_LOCK_STATE_MAX_NUMBER_OF_RETRIES, DEFAULT_AWAIT_LOCK_STATE_MILLISECONDS_BETWEEN_RETRIES,
            DEFAULT_AWAIT_INDEXING_MAX_NUMBER_OF_RETRIES, DEFAULT_AWAIT_INDEXING_MILLISECONDS_BETWEEN_RETRIES,
            unblockKey,
            null, null, null);
    }
}
