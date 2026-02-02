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
import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.DataMessageWithId;
import nl.knaw.dans.lib.dataverse.model.banner.MessageText;
import nl.knaw.dans.lib.dataverse.model.banner.Messages;

import java.util.List;

@Slf4j
public class AdminAddBannerMessage extends ExampleBase {

    public static void main(String[] args) throws Exception {
        // Usage: AdminAddBannerMessage "message text" [lang] [dismissible]
        String text = args.length > 0 ? args[0] : "Scheduled maintenance at 22:00 UTC.";
        String lang = args.length > 1 ? args[1] : "en";
        boolean dismissible = args.length > 2 && Boolean.parseBoolean(args[2]);
        if (args.length <= 2) {
            // default to true if not provided
            dismissible = true;
        }

        var mt = new MessageText();
        mt.setLang(lang);
        mt.setMessage(text);

        var messages = new Messages();
        messages.setDismissibleByUser(Boolean.toString(dismissible));
        messages.setMessageTexts(List.of(mt));

        DataverseResponse<DataMessageWithId> r = client.admin().addBannerMessage(messages);
        log.info(r.getEnvelopeAsJson().toPrettyString());
        log.info("Result id: {}, message: {}", r.getData().getId(), r.getData().getMessage());
    }
}
