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

/**
 * Optional API to perform direct database operations on the Dataverse database.
 * Requires databaseUrl, databaseUser, and databasePassword in DataverseClientConfig.
 */
@Slf4j
public class DatabaseApi extends AbstractApi {

    DatabaseApi(@NonNull HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
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
     * Truncate notifications for all users who currently have more than N notifications.
     * Keeps the newest N records per user.
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
