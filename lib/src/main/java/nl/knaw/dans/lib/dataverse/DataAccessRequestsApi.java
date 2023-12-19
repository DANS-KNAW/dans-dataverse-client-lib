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

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.model.DataMessage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DataAccessRequestsApi extends AbstractTargetedApi {
    private final Path subPath = subPath("allowAccessRequest/");
    private final Map<String, List<String>> params = params(new HashMap<>());
    private final HashMap<String, String> headers = new HashMap<>();

    DataAccessRequestsApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId) {
        super(httpClientWrapper, id, isPersistentId, null, Paths.get("api/access/"));
    }

    DataAccessRequestsApi(HttpClientWrapper httpClientWrapper, String id, boolean isPersistentId, String invocationId) {
        super(httpClientWrapper, id, isPersistentId, invocationId, Paths.get("api/access/"));
    }

    public DataverseHttpResponse<DataMessage> enable() throws IOException, DataverseException {
        return toggle("true");
    }

    public DataverseHttpResponse<DataMessage> disable() throws IOException, DataverseException {
        return toggle("false");
    }

    private DataverseHttpResponse<DataMessage> toggle(String bool) throws IOException, DataverseException {
        headers.putAll(extraHeaders);
        return httpClientWrapper.putTextString(subPath, bool, params, headers, DataMessage.class);
    }
}
