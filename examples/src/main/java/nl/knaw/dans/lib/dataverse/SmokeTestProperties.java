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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class SmokeTestProperties {
    private final Properties properties = new Properties();
    private final Path examplesRoot = ExampleBase.getExamplesRoot();

    public SmokeTestProperties() throws IOException {
        var propsFile = examplesRoot.resolve("smoketest.properties");
        try (var fis = Files.newInputStream(propsFile)) {
            properties.load(fis);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String readJson(String p) throws IOException {
        var jsonResourcesDir = properties.getProperty("jsonResourcesDir");
        var path = examplesRoot
            .resolve(jsonResourcesDir)
            .resolve(p);
        if (!path.toFile().exists()) {
            path = Path.of(jsonResourcesDir);
        }
        return Files.readString(path);
    }
}
