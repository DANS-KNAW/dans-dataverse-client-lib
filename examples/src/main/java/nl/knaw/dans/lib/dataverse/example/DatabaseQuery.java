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

import lombok.Value;
import nl.knaw.dans.lib.dataverse.ExampleBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseQuery extends ExampleBase {

    @Value
    public static class VersionInfo {
        String doi;
        Long versionNumber;
        Long minorVersionNumber;
        long storageSize;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: DatabaseQuery <doi1> <doi2> ...");
            System.exit(1);
        }

        String query = """
            SELECT
                dvo.protocol || ':' || dvo.authority || '/' || dvo.identifier AS doi,
                dv.versionnumber,
                dv.minorversionnumber,
                SUM(df.filesize) AS storage_size
            FROM
                dvobject dvo
                LEFT JOIN datasetversion dv ON dvo.id = dv.dataset_id
                LEFT JOIN filemetadata fm ON dv.id = fm.datasetversion_id
                LEFT JOIN datafile df ON fm.datafile_id = df.id
            WHERE
                dvo.dtype = 'Dataset'
                AND dvo.protocol || ':' || dvo.authority || '/' || dvo.identifier = ?
            GROUP BY
                doi,
                dv.versionnumber,
                dv.minorversionnumber
            ORDER BY
                doi,
                dv.versionnumber ASC,
                dv.minorversionnumber ASC
            """;

        List<Object[]> parameterSets = Arrays.stream(args)
            .map(doi -> new Object[] { doi })
            .collect(Collectors.toList());

        try (var context = client.database().query(query, rs -> {
            try {
                return new VersionInfo(
                    rs.getString("doi"),
                    rs.getObject("versionnumber", Long.class),
                    rs.getObject("minorversionnumber", Long.class),
                    rs.getLong("storage_size")
                );
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to map ResultSet row to VersionInfo", e);
            }
        })) {
            List<VersionInfo> results = context.executeFor(parameterSets);

            System.out.printf("%-30s %-10s %-15s%n", "DOI", "Version", "Storage Size");
            System.out.println("-".repeat(55));
            for (VersionInfo info : results) {
                String version = info.getVersionNumber() + "." + info.getMinorVersionNumber();
                System.out.printf("%-30s %-10s %-15d%n", info.getDoi(), version, info.getStorageSize());
            }
        }
    }
}
