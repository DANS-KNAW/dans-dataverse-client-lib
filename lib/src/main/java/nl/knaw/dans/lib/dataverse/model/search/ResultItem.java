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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.net.URI;

// Mix between snake_case and camelCase, so we need to specify per field what name conversion strategy to use for (de)serialization
@Data
public abstract class ResultItem {
    private final SearchItemType type;
    private String name;
    private URI url;
    private String description;
    @JsonProperty("published_at")
    private String publishedAt;

    protected ResultItem() {
        this.type = SearchItemType.dataset;
    }

    protected ResultItem(SearchItemType type) {
        this.type = type;
    }
}
