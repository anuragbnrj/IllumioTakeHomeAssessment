package in.anuragbanerjee.mapper;

import in.anuragbanerjee.model.LogEntry;
import in.anuragbanerjee.model.PortProtocolKey;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TagMapper {
    private final Map<PortProtocolKey, Set<String>> tagMapping;

    public TagMapper() {
        tagMapping = new HashMap<>();
    }

    public void loadTagMappings(String lookupTableFilePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(lookupTableFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");

                    if (parts.length != 3) {
                        throw new IllegalArgumentException(
                                "Invalid number of fields. Expected " + 3 +
                                        " but got " + parts.length + " in line: " + line
                        );
                    }

                    int dstport = Integer.parseInt(parts[0]);
                    String protocol = parts[1].toLowerCase();
                    String tag = parts[2].toLowerCase();
                    PortProtocolKey portProtocolKey = new PortProtocolKey(dstport, protocol);

                    Set<String> associatedTags = tagMapping.getOrDefault(portProtocolKey, new HashSet<>());
                    associatedTags.add(tag);
                    tagMapping.put(portProtocolKey, associatedTags);
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading flow log file", e);
        }
        // System.out.println(tagMapping.toString());
    }

    public Set<String> getAssociatedTags(LogEntry logEntry) {
        PortProtocolKey key = new PortProtocolKey(logEntry.getDestinationPort(), logEntry.getProtocolAsString());
        return tagMapping.get(key);
    }
}
