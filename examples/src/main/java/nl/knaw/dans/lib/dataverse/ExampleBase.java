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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public abstract class ExampleBase {

    protected static DataverseClient client;
    protected static ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);

    static {
        try {
            String propsFiles = getExamplesRoot().resolve("dataverse.properties").toString();
            PropertiesConfiguration props = new PropertiesConfiguration(propsFiles);
            DataverseClientConfig config = new DataverseClientConfig(new URI(props.getString("baseUrl")), props.getString("apiToken"), props.getString("unblockKey", null));
            client = new DataverseClient(config);
        }
        catch (ConfigurationException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    protected static String toPrettyJson(Object value) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
    }

    protected static Path getExamplesRoot() {
        var workingDir = Path.of("").toAbsolutePath();
        var candidates = List.of(Path.of("modules/dans-dataverse-client-lib/examples"),
            Path.of("dans-dataverse-client-lib/examples"),
            Path.of("examples"),
            Path.of("."));
        for (var candidate : candidates) {
            var fullPath = workingDir.resolve(candidate);
            if (Files.exists(fullPath)) {
                return fullPath;
            }
        }
        throw new IllegalStateException("Could not find examples root from working dir: " + workingDir);
    }

    protected static void warnForCustomMetadataBlocks(DataverseException e) {
        if (e.getMessage().contains("Validation Failed") && e.getMessage().contains("is required")) {
            log.warn("The test assumes only the citation block is mandatory.");
            log.warn("See https://guides.dataverse.org/en/latest/api/native-api.html#define-metadata-blocks-for-a-dataverse-collection");
            log.warn("or login as admin and edit the 'metadata fields' in the 'general information' of the dataverse");
        }
    }

}
