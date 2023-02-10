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

import nl.knaw.dans.lib.dataverse.DataverseHttpResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataverseIsMetadataBlocksRoot extends ExampleBase {

    private static final Logger log = LoggerFactory.getLogger(DataverseIsMetadataBlocksRoot.class);

    public static void main(String[] args) throws Exception {
        String alias = args[0];
        log.info("Getting metadata blocks isRoot for dataverse with alias: {}",  alias);
        DataverseHttpResponse<Boolean> r = client.dataverse(alias).isMetadataBlocksRoot();
        log.info("Status Line: {}", r.getHttpResponse().getStatusLine());
        log.info("Is root: {}", r.getData());
    }
}
