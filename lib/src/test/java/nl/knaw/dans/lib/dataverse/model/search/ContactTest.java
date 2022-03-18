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
package nl.knaw.dans.lib.dataverse.model.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContactTest extends ModelSearchFixture{
    private static final Class<Contact> classUnderTest = Contact.class;

    @Test
    public void canDeserialize() throws Exception {
        Contact c = mapper.readValue(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(classUnderTest, c.getClass());
        assertEquals("Positor01, D", c.getName());
    }

    @Test
    public void roundTrip() throws Exception {
        Contact c = roundTrip(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(classUnderTest, c.getClass());
    }
}
