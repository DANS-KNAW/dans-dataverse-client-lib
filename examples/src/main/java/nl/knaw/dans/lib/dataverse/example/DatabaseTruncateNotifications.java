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

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.ExampleBase;

@Slf4j
public class DatabaseTruncateNotifications extends ExampleBase {
    /**
     * Usage:
     *  - DatabaseTruncateNotifications <numberOfRecordsToKeep>
     *    Truncate notifications for all users, keeping the given number of latest records per user.
     *  - DatabaseTruncateNotifications <userId> <numberOfRecordsToKeep>
     *    Truncate notifications for a specific user.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1 && args.length != 2) {
            log.error("Usage: DatabaseTruncateNotifications <numberOfRecordsToKeep> | <userName> <numberOfRecordsToKeep>");
            return;
        }

        if (args.length == 1) {
            int keep = Integer.parseInt(args[0]);
            int deleted = client.database().truncateNotificationsForAllUsers(keep);
            log.info("Truncated notifications for all users; kept {} per user; deleted {} records in total", keep, deleted);
        }
        else {
            int userId = Integer.parseInt(args[0]);
            int keep = Integer.parseInt(args[1]);
            int deleted = client.database().truncateNotificationsForUser(userId, keep);
            log.info("Truncated notifications for user {}; kept {}; deleted {} records", userId, keep, deleted);
        }
    }
}
