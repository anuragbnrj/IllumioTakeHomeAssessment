package in.anuragbanerjee.parser;

import in.anuragbanerjee.parser.strategy.DefaultVPCFlowLogStrategy;
import in.anuragbanerjee.parser.strategy.LogParsingStrategy;

public class LogParsingStrategyFactory {
    public static LogParsingStrategy createStrategy(String logFormat) {
        return switch (logFormat.toLowerCase()) {
            case "default" -> new DefaultVPCFlowLogStrategy();
            default -> throw new IllegalArgumentException("Unsupported log format: " + logFormat);
        };
    }
}
