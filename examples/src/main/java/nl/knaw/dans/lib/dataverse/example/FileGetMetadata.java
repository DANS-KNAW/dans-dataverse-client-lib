package nl.knaw.dans.lib.dataverse.example;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.ExampleBase;

@Slf4j
public class FileGetMetadata extends ExampleBase {
    public static void main(String[] args) throws Exception {
        var r = client.file(Long.parseLong(args[0])).getMetadata();
        log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());

        var checksum = r.getData().getDataFile().getChecksum();
        log.info("Checksum type = {}, value = {}", checksum.getType(), checksum.getValue());
    }
}
