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
package nl.knaw.dans.lib.dataverse.smoketest;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.SmokeTestProperties;
import nl.knaw.dans.lib.dataverse.example.AdminGetDatabaseSetting;
import nl.knaw.dans.lib.dataverse.example.AdminListSingleUser;
import nl.knaw.dans.lib.dataverse.example.AdminPutDatabaseSetting;
import nl.knaw.dans.lib.dataverse.example.AdminValidateDatasetFiles;

import java.util.List;

@Slf4j
public class AdminTest {
    public static void main(String[] args) throws Exception {
        AdminListSingleUser.main(List.of(new SmokeTestProperties().getProperty("username")).toArray(new String[0]));
        AdminValidateDatasetFiles.main(List.of("2").toArray(new String[0]));
        AdminValidateDatasetFiles.main(List.of("2", "True").toArray(new String[0]));
        AdminValidateDatasetFiles.main(List.of("2", "False").toArray(new String[0]));
        AdminGetDatabaseSetting.main(List.of(":AllowSignUp").toArray(new String[0]));
        AdminPutDatabaseSetting.main(List.of(":AllowSignUp", "False").toArray(new String[0]));
    }
}
