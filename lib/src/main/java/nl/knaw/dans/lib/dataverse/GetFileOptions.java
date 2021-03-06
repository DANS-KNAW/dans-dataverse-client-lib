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

public class GetFileOptions {
    private String format;
    private boolean noVarHeader;
    private boolean imageThumb;
    private int imageThumbPixels = 64;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isNoVarHeader() {
        return noVarHeader;
    }

    public void setNoVarHeader(boolean noVarHeader) {
        this.noVarHeader = noVarHeader;
    }

    public boolean isImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(boolean imageThumb) {
        this.imageThumb = imageThumb;
    }

    public int getImageThumbPixels() {
        return imageThumbPixels;
    }

    public void setImageThumbPixels(int imageThumbPixels) {
        this.imageThumbPixels = imageThumbPixels;
    }
}
