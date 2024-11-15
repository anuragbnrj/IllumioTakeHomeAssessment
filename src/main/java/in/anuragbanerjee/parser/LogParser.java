package in.anuragbanerjee.parser;

import in.anuragbanerjee.model.LogEntry;
import in.anuragbanerjee.parser.strategy.LogParsingStrategy;

public class LogParser {
    private LogParsingStrategy strategy;

    public LogParser(LogParsingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(LogParsingStrategy strategy) {
        this.strategy = strategy;
    }

    public LogEntry parseLogEntry(String logLine) {
        return strategy.parse(logLine);
    }
}
