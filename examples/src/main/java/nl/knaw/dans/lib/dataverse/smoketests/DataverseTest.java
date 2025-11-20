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
package nl.knaw.dans.lib.dataverse.smoketests;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.example.DataverseCreate;
import nl.knaw.dans.lib.dataverse.example.DataverseCreateDataset;
import nl.knaw.dans.lib.dataverse.example.DataverseDelete;
import nl.knaw.dans.lib.dataverse.example.DataverseGetContents;
import nl.knaw.dans.lib.dataverse.example.DataverseGetStorageSize;
import nl.knaw.dans.lib.dataverse.example.DataverseImportDataset;
import nl.knaw.dans.lib.dataverse.example.DataverseIsMetadataBlocksRoot;
import nl.knaw.dans.lib.dataverse.example.DataverseListRoleAssignments;
import nl.knaw.dans.lib.dataverse.example.DataverseListRoles;
import nl.knaw.dans.lib.dataverse.example.DataversePublish;
import nl.knaw.dans.lib.dataverse.example.DataverseSetMetadataBlocksRoot;
import nl.knaw.dans.lib.dataverse.example.DataverseView;

import java.util.List;

@Slf4j
public class DataverseTest extends ExampleBase {
    public static void main(String[] args) throws Exception {
        var alias = new Properties().getProperty("dataverseAlias");
        DataverseGetContents.main(new String[0]);
        DataverseGetStorageSize.main(new String[0]);
        DataverseIsMetadataBlocksRoot.main(List.of(alias).toArray(new String[0]));
        DataverseListRoleAssignments.main(new String[0]);
        DataverseListRoles.main(new String[0]);
        DataverseView.main(List.of(alias).toArray(new String[0]));
        DataverseCreateDataset.main(new String[0]);
        DataverseImportDataset.main(new String[0]);

        DataverseIsMetadataBlocksRoot.main(List.of(alias).toArray(new String[0]));
        DataverseSetMetadataBlocksRoot.main(List.of(alias, "true").toArray(new String[0]));

        var dvAlias = client.dataverse(alias)
            .create(DataverseCreate.getDataverse())
            .getData().getAlias();
        DataversePublish.main(List.of(dvAlias).toArray(new String[0]));
        DataverseIsMetadataBlocksRoot.main(List.of(dvAlias).toArray(new String[0]));
        DataverseSetMetadataBlocksRoot.main(List.of(dvAlias).toArray(new String[0]));
        DataverseDelete.main(List.of(dvAlias).toArray(new String[0]));
    }
}
