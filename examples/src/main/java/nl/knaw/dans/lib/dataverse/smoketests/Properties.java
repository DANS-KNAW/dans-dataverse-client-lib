package nl.knaw.dans.lib.dataverse.smoketests;

import java.io.FileInputStream;
import java.io.IOException;

public class Properties {
    private java.util.Properties properties = new java.util.Properties();

    public Properties() throws IOException {
        try (FileInputStream fis = new FileInputStream("dataverse.properties")) {
            properties.load(fis);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
