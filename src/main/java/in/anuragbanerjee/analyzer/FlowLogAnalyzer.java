package in.anuragbanerjee.analyzer;

import in.anuragbanerjee.mapper.TagMapper;
import in.anuragbanerjee.model.LogEntry;
import in.anuragbanerjee.model.PortProtocolKey;
import in.anuragbanerjee.output.OutputGenerator;
import in.anuragbanerjee.parser.LogParser;
import in.anuragbanerjee.parser.LogParsingStrategyFactory;
import in.anuragbanerjee.parser.strategy.LogParsingStrategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FlowLogAnalyzer {
    private final LogParser logParser;
    private final TagMapper tagMapper;
    private final OutputGenerator outputGenerator;

    public FlowLogAnalyzer(String logFormat, TagMapper tagMapper, OutputGenerator outputGenerator) {
        LogParsingStrategy strategy = LogParsingStrategyFactory.createStrategy(logFormat);
        this.logParser = new LogParser(strategy);
        this.tagMapper = tagMapper;
        this.outputGenerator = outputGenerator;
    }

    public void analyze(String flowLogFilePath, String lookupTableFilePath, String outputFilePath) {
        tagMapper.loadTagMappings(lookupTableFilePath);
        List<LogEntry> logEntries = parseFlowLogFile(flowLogFilePath);
        Map<String, Integer> tagCounts = mapTagCounts(logEntries);
        Map<PortProtocolKey, Integer> portProtocolCounts = mapPortProtocolCounts(logEntries);

        outputGenerator.writeOutput(tagCounts, portProtocolCounts, outputFilePath);
    }

    private List<LogEntry> parseFlowLogFile(String flowLogFilePath) {
        List<LogEntry> logEntries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(flowLogFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    LogEntry logEntry = logParser.parseLogEntry(line);
                    logEntries.add(logEntry);
                } catch (IllegalArgumentException e) {
                    // Log error and continue with next line
                    // System.err.println("Error parsing line: " + line);
                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading flow log file", e);
        }
        return logEntries;
    }

    private Map<String, Integer> mapTagCounts(List<LogEntry> logEntries) {
        Map<String, Integer> tagCounts = new HashMap<>();
        for (LogEntry logEntry : logEntries) {
            Set<String> associatedTags = tagMapper.getAssociatedTags(logEntry);
            if (associatedTags != null) {
                for (String tag : associatedTags) {
                    tagCounts.merge(tag, 1, Integer::sum);
                }
            } else {
                tagCounts.merge("untagged", 1, Integer::sum);
            }
        }
        return tagCounts;
    }

    private Map<PortProtocolKey, Integer> mapPortProtocolCounts(List<LogEntry> logEntries) {
        Map<PortProtocolKey, Integer> portProtocolCounts = new HashMap<>();
        for (LogEntry logEntry : logEntries) {
            PortProtocolKey key = new PortProtocolKey(logEntry.getDestinationPort(), logEntry.getProtocolAsString());
            portProtocolCounts.merge(key, 1, Integer::sum);
        }
        return portProtocolCounts;
    }
}
