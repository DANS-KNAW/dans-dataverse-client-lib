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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import nl.knaw.dans.lib.dataverse.model.DataverseEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Response from Dataverse.
 *
 * @param <D> the type of the data of the response message envelope, one of the classes in {@link nl.knaw.dans.lib.dataverse.model}
 */
public class DataverseResponse<D> {
    private static final Logger log = LoggerFactory.getLogger(DataverseResponse.class);
    private final ObjectMapper mapper;

    private final String bodyText;
    private final JavaType dataType;

    protected DataverseResponse(String bodyText, ObjectMapper mapper, Class<?>... dataClass) {
        log.trace("ENTER");
        log.trace(bodyText);
        this.bodyText = bodyText;
        this.mapper = mapper;
        TypeFactory typeFactory = mapper.getTypeFactory();
        if (dataClass.length == 2) {
            JavaType inner = typeFactory.constructParametricType(dataClass[0], dataClass[1]);
            this.dataType = typeFactory.constructParametricType(DataverseEnvelope.class, inner);
        }
        else {
            this.dataType = typeFactory.constructParametricType(DataverseEnvelope.class, dataClass[0]);
        }
    }

    // Must be static, otherwise compiler complains about passing result into 'this()' call
    private static JavaType constuctJavaTypeForContainerClass(Class<?> containerClass, Class<?> elementClass, ObjectMapper mapper) {
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType inner = typeFactory.constructParametricType(containerClass, elementClass);
        return typeFactory.constructParametricType(DataverseEnvelope.class, inner);
    }

    /**
     * @return A dataverse envelope
     * @throws IOException if body cannot be processed properly as JSON
     */
    public DataverseEnvelope<D> getEnvelope() throws IOException {
        return mapper.readValue(bodyText, dataType);
    }

    /**
     * @return the payload of the envelope directly
     * @throws IOException if body cannot be processed properly as JSON
     */
    public D getData() throws IOException {
        return getEnvelope().getData();
    }

    /**
     * @return the envelope as a JSON AST
     * @throws IOException if body cannot be processed properly as JSON
     */
    public JsonNode getEnvelopeAsJson() throws IOException {
        return mapper.readTree(bodyText);
    }

    /**
     * @return the body as a String
     */
    public String getEnvelopeAsString() {
        return bodyText;
    }
}
