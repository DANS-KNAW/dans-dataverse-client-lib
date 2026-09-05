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
import nl.knaw.dans.lib.dataverse.model.workflow.ResumeMessage;
import nl.knaw.dans.lib.dataverse.model.workflow.Workflow;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class WorkflowsApi extends AbstractApi {

    private static final Path callbackSubPath = Paths.get("api/workflows/");
    private static final Path managementSubPath = Paths.get("api/admin/workflows/");

    WorkflowsApi(HttpClientWrapper httpClientWrapper) {
        super(httpClientWrapper);
    }

    public DataverseHttpResponse<Object> resume(String invocationId, ResumeMessage resumeMessage) throws IOException, DataverseException {
        return httpClientWrapper.postModelObjectAsJson(callbackSubPath.resolve(invocationId), resumeMessage, Object.class);
    }

    public DataverseHttpResponse<Workflow> addWorkflow(Workflow workflow) throws IOException, DataverseException {
        return httpClientWrapper.postModelObjectAsJson(managementSubPath, workflow, Workflow.class);
    }

    public DataverseHttpResponse<List<Workflow>> listWorkflows() throws IOException, DataverseException {
        return httpClientWrapper.get(managementSubPath, List.class, Workflow.class);
    }

    public DataverseHttpResponse<DataMessage> setDefault(String triggerType, long workflowId) throws IOException, DataverseException {
        return httpClientWrapper.putTextString(
            managementSubPath.resolve("default/").resolve(triggerType),
            Long.toString(workflowId),
            Collections.emptyMap(),
            Collections.emptyMap(),
            DataMessage.class
        );
    }

    public DataverseHttpResponse<Map> listDefaults() throws IOException, DataverseException {
        return httpClientWrapper.get(managementSubPath.resolve("default/"), Map.class);
    }

    public DataverseHttpResponse<Workflow> getDefault(String triggerType) throws IOException, DataverseException {
        return httpClientWrapper.get(managementSubPath.resolve("default/").resolve(triggerType), Workflow.class);
    }

    public DataverseHttpResponse<DataMessage> deleteDefault(String triggerType) throws IOException, DataverseException {
        return httpClientWrapper.delete(managementSubPath.resolve("default/").resolve(triggerType), DataMessage.class);
    }

    public DataverseHttpResponse<Workflow> getWorkflow(long workflowId) throws IOException, DataverseException {
        return httpClientWrapper.get(managementSubPath.resolve(Long.toString(workflowId)), Workflow.class);
    }

    public DataverseHttpResponse<DataMessage> deleteWorkflow(long workflowId) throws IOException, DataverseException {
        return httpClientWrapper.delete(managementSubPath.resolve(Long.toString(workflowId)), DataMessage.class);
    }

    public DataverseHttpResponse<String> getIpWhitelist() throws IOException, DataverseException {
        return httpClientWrapper.get(managementSubPath.resolve("ip-whitelist"), String.class);
    }

    public DataverseHttpResponse<String> setIpWhitelist(String ipWhitelist) throws IOException, DataverseException {
        return httpClientWrapper.putTextString(
            managementSubPath.resolve("ip-whitelist"),
            ipWhitelist,
            Collections.emptyMap(),
            Collections.emptyMap(),
            String.class
        );
    }

    public DataverseHttpResponse<DataMessage> deleteIpWhitelist() throws IOException, DataverseException {
        return httpClientWrapper.delete(managementSubPath.resolve("ip-whitelist"), DataMessage.class);
    }

}
