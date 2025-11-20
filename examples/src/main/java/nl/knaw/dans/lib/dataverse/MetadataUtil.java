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

import nl.knaw.dans.lib.dataverse.model.dataset.FieldList;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataBlock;
import nl.knaw.dans.lib.dataverse.model.dataset.MetadataField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataUtil {
    public static Map<String, MetadataBlock> toMetadataBlockMap(MetadataBlock... blocks) {
        var map = new HashMap<String, MetadataBlock>();
        for (var block : blocks) {
            map.put(block.getName(), block);
        }
        return map;
    }

    public static MetadataBlock toMetadataBlock(String name, String displayName, MetadataField... fields) {
        MetadataBlock metadataBlock = new MetadataBlock();
        metadataBlock.setName(name);
        metadataBlock.setDisplayName(displayName);
        metadataBlock.setFields(List.of(fields));
        return metadataBlock;
    }

    public static FieldList toFieldList(MetadataField... fields) {
        var fieldList = new FieldList();
        for (var field : fields) {
            fieldList.add(field);
        }
        return fieldList;
    }
}
