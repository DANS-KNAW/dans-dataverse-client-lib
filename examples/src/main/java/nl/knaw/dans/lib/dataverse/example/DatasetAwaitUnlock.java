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
import nl.knaw.dans.lib.dataverse.DatasetApi;
import nl.knaw.dans.lib.dataverse.ExampleBase;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class DatasetAwaitUnlock extends ExampleBase {
    /*
     * The easiest way to test this manually is to create an InReview lock. Other locks will be released too quickly.
     *
     * 1.  Login as depositor001 and create a dataset.
     * 2.  Submit the dataset for review.
     * 3a. Log in as a curator or admin.
     * 4a. Start the program.
     * 5a. Publish the dataset.
     *
     * To check for multiple types of locks also
     *
     * 3b. Use small values for retries and milliseconds between retries and lock types "InReview" and "Ingest".
     * 4b. Upload https://github.com/user-attachments/files/19648533/50.zip
     *     or 50.zip attached to  https://drivenbydata.atlassian.net/browse/DD-1818
     * 5b. Save changes to the dataset.
     * 6b. Quickly run this test repeatedly.
     *     Debug logging will show both locks at the first attempt, and only the InReview lock at a later attempt.
     * 7b. Continue with 3a.
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            log.error("Usage: DatasetAwaitUnlock <persistentId> [<maxNumberOfRetries> [<millisecondsBetweenRetries> [<lockType1> <lockType2> ...]]]");
            System.exit(1);
        }
        var persistentId = args[0];
        var dataset = client.dataset(persistentId);
        if (args.length == 1) {
            dataset.awaitUnlock();
        }
        int maxNumberOfRetries = args.length > 1 ? Integer.parseInt(args[1]) : 10;
        int millisecondsBetweenRetries = args.length > 2 ? Integer.parseInt(args[2]) : 2000;
        if (args.length < 4) {
            dataset.awaitUnlock(maxNumberOfRetries, millisecondsBetweenRetries);
        } else {
            var lockTypes = List.of(Arrays.copyOfRange(args, 3, args.length));
            dataset.awaitUnlock(lockTypes, maxNumberOfRetries, millisecondsBetweenRetries);
        }
        log.info("All locks on dataset {} were cleared", persistentId);
    }
}
