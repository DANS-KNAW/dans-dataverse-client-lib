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
import nl.knaw.dans.lib.dataverse.DataverseHttpResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.user.BuiltinUser;

@Slf4j
public class BuiltinUserCreate extends ExampleBase {
    // To put a test builtin user key (assuming that your unblock-key is s3kretKey)
    // curl -X PUT -d builtInS3kretKey http://localhost:8080/api/admin/settings/:BuiltinUsersKey?unblock-key=s3kretKey (v6.9 and higher, before that: BuiltinUsers.KEY, without leading colon!)
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            log.error("Usage: BuiltinUserCreate <builtinUsersKey> <password>");
            System.exit(1);
        }
        var builtinUsersKey = args[0];
        var password = args[1];

        // Please note that Dataverse performs no input validation, and if you validate database constraints, the error message will be obscure.
        // username and email MUST be unique, and email MUST be a valid email address.
        // The password may be weak, but on the first login Dataverse will prompt the user to change it.
        var user = new BuiltinUser();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("johndoe");
        user.setAffiliation("DANS");
        user.setPosition("Researcher");
        user.setEmail("johndoe@dans.knaw.nl");

        log.info(toPrettyJson(user));
        var r = client.builtinUsers(builtinUsersKey).create(user, password);
        log.info("Status Line: {} {}", r.getHttpResponse().getCode(), r.getHttpResponse().getReasonPhrase());
        if (r.getHttpResponse().getCode() < 400) {
            log.info("Created user: {}", r.getData().getUser().getUserName());
        } else {
            log.error("Failed to create user: {}", r.getHttpResponse().getReasonPhrase());
        }
        log.info("Response envelope: {}", r.getEnvelopeAsJson().toPrettyString());
    }
}

