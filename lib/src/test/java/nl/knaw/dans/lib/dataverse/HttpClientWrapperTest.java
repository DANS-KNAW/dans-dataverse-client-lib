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

import org.apache.hc.client5.http.classic.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpClientWrapperTest extends MapperFixture {

    private HttpClient httpClient;
    private DataverseClientConfig config;
    private HttpClientWrapper httpClientWrapper;

    protected HttpClientWrapperTest() {
        super("");
    }

    @BeforeEach
    public void setUp() throws Exception {
        super.beforeEach();
        httpClient = Mockito.mock(HttpClient.class);
        config = new DataverseClientConfig(new URI("http://localhost:8080"), "api-token", "unblock-key");
        httpClientWrapper = new HttpClientWrapper(config, httpClient, mapper);
    }

    @Test
    public void buildUri_includesUnblockKeyByDefault() throws Exception {
        Method buildURi = HttpClientWrapper.class.getDeclaredMethod("buildURi", Path.class, java.util.Map.class);
        buildURi.setAccessible(true);
        URI uri = (URI) buildURi.invoke(httpClientWrapper, Paths.get("test"), Collections.emptyMap());
        assertTrue(uri.getQuery().contains("unblock-key=unblock-key"));
    }

    @Test
    public void buildUri_omitsUnblockKeyWhenStopPassingUnblockKeyIsCalled() throws Exception {
        Method buildURi = HttpClientWrapper.class.getDeclaredMethod("buildURi", Path.class, java.util.Map.class);
        buildURi.setAccessible(true);
        HttpClientWrapper wrapperWithoutUnblockKey = httpClientWrapper.stopPassingUnblockKey();
        URI uri = (URI) buildURi.invoke(wrapperWithoutUnblockKey, Paths.get("test"), Collections.emptyMap());
        assertFalse(uri.getQuery() != null && uri.getQuery().contains("unblock-key="));
    }
}
