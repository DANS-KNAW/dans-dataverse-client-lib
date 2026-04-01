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
import nl.knaw.dans.lib.dataverse.DataverseException;
import nl.knaw.dans.lib.dataverse.DataverseHttpResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.metrics.MetricsTreeNode;

/**
 * Example program that retrieves and prints the Dataverse metrics tree using the new metrics endpoint.
 *
 * <p>Usage:
 * <pre>
 *   mvn -pl modules/dans-dataverse-client-lib/examples -am -DskipTests exec:java \
 *     -Dexec.mainClass="nl.knaw.dans.lib.dataverse.example.MetricsTree"
 * </pre>
 * Make sure to configure examples/dataverse.properties first (see README in the examples module).
 */
@Slf4j
public class MetricsTree extends ExampleBase {

    public static void main(String[] args) throws Exception {
        try {
            DataverseHttpResponse<MetricsTreeNode> response = client.metrics().tree();
            log.info("Envelope: {}", response.getEnvelopeAsJson().toPrettyString());
            MetricsTreeNode root = response.getData();

            log.info("Root: id={} alias={} name={}", root.getId(), root.getAlias(), root.getName());
            printTree(root, "");
        }
        catch (DataverseException e) {
            log.error("Error calling metrics tree endpoint: {}", e.getMessage(), e);
            throw e;
        }
    }

    private static void printTree(MetricsTreeNode node, String indent) {
        if (node == null) {
            return;
        }
        if (node.getChildren() != null) {
            for (MetricsTreeNode child : node.getChildren()) {
                log.info("{}- id={} alias={} depth={} name={}", indent, child.getId(), child.getAlias(), child.getDepth(), child.getName());
                printTree(child, indent + "  ");
            }
        }
    }
}
