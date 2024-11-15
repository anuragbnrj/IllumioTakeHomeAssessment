package in.anuragbanerjee.output;

import in.anuragbanerjee.model.PortProtocolKey;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class OutputGenerator {

    public void writeOutput(Map<String, Integer> tagCounts, Map<PortProtocolKey, Integer> portProtocolCounts, String outputFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write("Tag Counts:\n");
            writer.write("Tag,Count\n");
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }

            writer.write("\nPort/Protocol Combination Counts:\n");
            writer.write("Port,Protocol,Count\n");
            for (Map.Entry<PortProtocolKey, Integer> entry : portProtocolCounts.entrySet()) {
                PortProtocolKey key = entry.getKey();
                writer.write(key.getPort() + "," + key.getProtocol() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            // Handle exception
        }
    }

}
