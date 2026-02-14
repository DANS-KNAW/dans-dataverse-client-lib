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

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Optional API to perform direct database operations on the Dataverse database. Requires databaseUrl, databaseUser, and databasePassword in DataverseClientConfig.
 */
@Slf4j
public class DatabaseApi extends AbstractApi {

    DatabaseApi(@NonNull HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
    }

    /**
     * Creates a QueryContext for the provided SQL query. The QueryContext can be used to execute the query with different sets of parameters. Note that the connection used by the QueryContext is set
     * to read-only mode, so this method is intended for executing read-only queries. It is generally not recommended to change the Dataverse database directly.
     *
     * @param query the SQL query to execute, which may contain parameter placeholders (e.g., "?") for use with prepared statements
     * @return a QueryContext for executing the provided SQL query with different sets of parameters
     */
    public QueryContext<ResultSet> query(@NonNull String query) {
        try {
            return new QueryContext<>(getConnection(), query, (ResultSet rs) -> rs);
        }
        catch (SQLException e) {
            log.error("Failed to create QueryContext for query: {}", query, e);
            throw new RuntimeException("Failed to create QueryContext", e);
        }
    }

    /**
     * Creates a QueryContext for the provided SQL query, using the provided mapper function to map each ResultSet row to an object of type T. The QueryContext can be used to execute the query with
     * different sets of parameters. Note that the connection used by the QueryContext is set to read-only mode, so this method is intended for executing read-only queries. It is generally not
     * recommended to change the Dataverse database directly.
     *
     * @param query  the SQL query to execute, which may contain parameter placeholders (e.g., "?") for use with prepared statements
     * @param mapper a function that maps a ResultSet row to an object of type T, which will be used as the default mapper for the QueryContext. This mapper will be used when executing the query with
     *               sets of parameters without providing a custom mapper.
     * @param <T>    the type of objects to return in the list when executing the query with sets of parameters without providing a custom mapper
     * @return a QueryContext for executing the provided SQL query with different sets of parameters, using the provided mapper function to map ResultSet rows to objects of type T
     */
    public <T> QueryContext<T> query(@NonNull String query, @NonNull Function<ResultSet, T> mapper) {
        try {
            return new QueryContext<>(getConnection(), query, mapper);
        }
        catch (SQLException e) {
            log.error("Failed to create QueryContext for query: {}", query, e);
            throw new RuntimeException("Failed to create QueryContext", e);
        }
    }

    private Connection getConnection() throws SQLException {
        DataverseClientConfig cfg = httpClientWrapper.getConfig();
        if (cfg.getDatabaseUrl() == null || cfg.getDatabaseUser() == null || cfg.getDatabasePassword() == null) {
            throw new SQLException("Database configuration not provided. Please set databaseUrl, databaseUser, and databasePassword in DataverseClientConfig.");
        }
        return DriverManager.getConnection(cfg.getDatabaseUrl(), cfg.getDatabaseUser(), cfg.getDatabasePassword());
    }

    /**
     * Truncate notifications for a single user, keeping the newest N records.
     *
     * @param userId                the user_id in the Dataverse database (numeric)
     * @param numberOfRecordsToKeep number of latest notifications to keep (must be >= 0)
     * @return number of deleted rows
     * @throws SQLException when database access fails
     */
    public int truncateNotificationsForUser(int userId, int numberOfRecordsToKeep) throws SQLException {
        if (numberOfRecordsToKeep < 0) {
            throw new SQLException("numberOfRecordsToKeep must be >= 0");
        }
        String sql = "DELETE FROM usernotification WHERE user_id = ? AND id NOT IN (SELECT id FROM usernotification WHERE user_id = ? ORDER BY senddate DESC LIMIT ?)";
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, numberOfRecordsToKeep);
            int affected = ps.executeUpdate();
            log.info("Deleted {} record(s) for user with id {}", affected, userId);
            return affected;
        }
    }

    /**
     * Truncate notifications for all users who currently have more than N notifications. Keeps the newest N records per user.
     *
     * @param numberOfRecordsToKeep number of latest notifications to keep (must be >= 0)
     * @return total number of deleted rows across all users
     * @throws SQLException when database access fails
     */
    public int truncateNotificationsForAllUsers(int numberOfRecordsToKeep) throws SQLException {
        if (numberOfRecordsToKeep < 0) {
            throw new SQLException("numberOfRecordsToKeep must be >= 0");
        }
        List<Integer> userIds = getUserIdsExceeding(numberOfRecordsToKeep);
        int totalDeleted = 0;
        for (int userId : userIds) {
            totalDeleted += truncateNotificationsForUser(userId, numberOfRecordsToKeep);
        }
        return totalDeleted;
    }

    private List<Integer> getUserIdsExceeding(int threshold) throws SQLException {
        String sql = "SELECT user_id FROM usernotification GROUP BY user_id HAVING COUNT(user_id) > ?";
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                List<Integer> ids = new ArrayList<>();
                while (rs.next()) {
                    ids.add(rs.getInt(1));
                }
                return ids;
            }
        }
    }
}
