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
package nl.knaw.dans.lib.dataverse.integration;

import nl.knaw.dans.lib.dataverse.example.LicensesCreate;
import nl.knaw.dans.lib.dataverse.example.LicensesDelete;
import nl.knaw.dans.lib.dataverse.example.LicensesGetDefault;
import nl.knaw.dans.lib.dataverse.example.LicensesGetSingle;
import nl.knaw.dans.lib.dataverse.example.LicensesList;

import java.util.List;

public class LicenseSmokeTest {
    public static void main(String[] args) throws Exception {
        LicensesGetDefault.main(new String[0]);
        LicensesGetSingle.main(new String[0]);
        LicensesCreate.main(new String[0]);
        LicensesList.main(new String[0]);
        // TODO retrieve the number of the created license (with 'some name') and pass it to the delete example
        //   the default would be the one created above when running for the first time on dev_archaeology
        LicensesDelete.main(args.length > 0 ? args : List.of("24").toArray(new String[0]));
    }
}
