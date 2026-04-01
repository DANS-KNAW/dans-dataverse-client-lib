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

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.metrics.MetricsTreeNode;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * API for Dataverse metrics endpoints.
 */
@Slf4j
@ToString
public class MetricsApi extends AbstractApi {

    private final Path targetBase;

    MetricsApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
        this.targetBase = Paths.get("api/info/metrics/");
    }

    /**
     * Returns the metrics tree of dataverses.
     *
     * @return the root node of the metrics tree
     * @throws IOException when I/O problems occur during the interaction with Dataverse
     * @throws DataverseException when Dataverse fails to perform the request
     */
    public DataverseHttpResponse<MetricsTreeNode> tree() throws IOException, DataverseException {
        Path path = buildPath(targetBase, "tree");
        return httpClientWrapper.stopPassingUnblockKey().get(path, MetricsTreeNode.class);
    }
}
