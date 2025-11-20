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
import nl.knaw.dans.lib.dataverse.example.SearchFind;
import nl.knaw.dans.lib.dataverse.example.SearchIterator;
import nl.knaw.dans.lib.dataverse.model.search.SearchItemType;

import java.util.List;

public class SearchSmokeTest {
    public static void main(String[] args) throws Exception {
        SearchFind.main(List.of("bag").toArray(new String[0]));
        SearchIterator.main(List.of("bag").toArray(new String[0]));
        // it is not very likely that other search options will return different data structures
    }
}
