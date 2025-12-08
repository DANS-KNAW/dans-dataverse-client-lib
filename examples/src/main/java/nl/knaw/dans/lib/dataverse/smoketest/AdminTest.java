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
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.SmokeTestProperties;
import nl.knaw.dans.lib.dataverse.example.AdminGetDatabaseSetting;
import nl.knaw.dans.lib.dataverse.example.AdminPutDatabaseSetting;
import nl.knaw.dans.lib.dataverse.example.AdminValidateDatasetFiles;

import java.util.List;

@Slf4j
public class AdminTest extends ExampleBase {
    public static void main(String[] args) throws Exception {
        var displayName = client.admin()
            .listSingleUser(new SmokeTestProperties().getProperty("username"))
            .getData().getDisplayName();
        log.info("Display name of user: {}", displayName);
        var msg = client.admin()
            .getDatabaseSetting(":AllowSignUp")
            .getBodyAsObject().getData().getMessage();
        log.info("AllowSignUp setting: {}", msg);
        var x =  client.admin().putDatabaseSetting(":AllowSignUp", "False")
                .getData();
        log.info("Set AllowSignUp keys: {}", x.keySet());
        log.info("Set AllowSignUp values: {}", x.values());
    }
}
