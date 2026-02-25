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
import nl.knaw.dans.lib.dataverse.model.user.BuiltinUser;
import nl.knaw.dans.lib.dataverse.model.user.UserCreatedResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@ToString
public class BuiltinUserApi extends AbstractApi {
    private final String builtinUsersKey;

    BuiltinUserApi(HttpClientWrapper httpClientWrapper, String builtinUsersKey) {
        super(httpClientWrapper);
        this.builtinUsersKey = builtinUsersKey;
    }

    /**
     * Creates a built-in user from a JSON string with account data.
     *
     * @param accountData JSON string with account data
     * @param password    password for the new user
     * @return the created user
     * @throws DataverseException if the request fails
     * @throws IOException        if there is an I/O error
     */
    public DataverseHttpResponse<UserCreatedResult> create(String accountData, String password) throws DataverseException, IOException {
        var params = new HashMap<String, List<String>>();
        params.put("key", List.of(builtinUsersKey));
        params.put("password", List.of(password));
        return httpClientWrapper.postJsonString(Path.of("api/builtin-users"), accountData, params, Collections.emptyMap(), UserCreatedResult.class);
    }

    /**
     * Creates a built-in user from a model object.
     *
     * @param accountData model object with account data
     * @param password    password for the new user
     * @return the created user
     * @throws DataverseException if the request fails
     * @throws IOException        if there is an I/O error
     */
    public DataverseHttpResponse<UserCreatedResult> create(BuiltinUser accountData, String password) throws DataverseException, IOException {
        var params = new HashMap<String, List<String>>();
        params.put("key", List.of(builtinUsersKey));
        params.put("password", List.of(password));
        return httpClientWrapper.postModelObjectAsJson(Path.of("api/builtin-users"), accountData, params, Collections.emptyMap(), UserCreatedResult.class);
    }

}
