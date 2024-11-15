package in.anuragbanerjee.parser.strategy;

import in.anuragbanerjee.model.LogEntry;

public interface LogParsingStrategy {
    LogEntry parse(String logLine);
}
