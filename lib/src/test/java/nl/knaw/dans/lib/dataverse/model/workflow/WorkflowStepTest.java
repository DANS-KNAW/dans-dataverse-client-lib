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
package nl.knaw.dans.lib.dataverse.model.workflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkflowStepTest extends ModelWorkflowFixture {
    private static final Class<WorkflowStep> classUnderTest = WorkflowStep.class;

    @Test
    public void canDeserialize() throws Exception {
        WorkflowStep workflowStep = mapper.readValue(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(classUnderTest, workflowStep.getClass());
        assertEquals("api", workflowStep.getStepType());
        assertEquals("http://example.com/workflow/step", workflowStep.getProvider());
        assertEquals("POST", workflowStep.getParameters().get("method"));
        assertEquals("auth.example.token", workflowStep.getRequiredSettings().get("authToken"));
    }

    @Test
    public void roundTrip() throws Exception {
        WorkflowStep workflowStep = roundTrip(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(classUnderTest, workflowStep.getClass());
    }
}
