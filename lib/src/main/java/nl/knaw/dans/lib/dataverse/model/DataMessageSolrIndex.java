package nl.knaw.dans.lib.dataverse.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataMessageSolrIndex extends DataMessage {
    private Long numRowsClearedByClearAllIndexTimes;
    private List<Integer> availablePartitionIds;
    private Map<String, Integer> args;
    private Long id;
    private String persistentId;
    private List<Version> versions;

    @Data
    public static class Version {
        private String semanticVersion;
        private Long id;
    }
}
