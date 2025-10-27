package nl.knaw.dans.lib.dataverse;

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
}
