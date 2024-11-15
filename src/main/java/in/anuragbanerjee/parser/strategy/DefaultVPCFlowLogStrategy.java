package in.anuragbanerjee.parser.strategy;

import in.anuragbanerjee.model.LogEntry;

public class DefaultVPCFlowLogStrategy implements LogParsingStrategy {
    private static final int EXPECTED_FIELDS = 14;

    @Override
    public LogEntry parse(String logLine) {
        String[] fields = logLine.trim().split("\\s+");

        if (fields.length != EXPECTED_FIELDS) {
            throw new IllegalArgumentException(
                    "Invalid number of fields. Expected " + EXPECTED_FIELDS +
                            " but got " + fields.length + " in log line: " + logLine
            );
        }

        try {
            return new LogEntry.Builder()
                    .version(parseVersion(fields[0]))
                    .accountId(parseLong(fields[1]))
                    .interfaceId(fields[2])
                    .sourceIp(parseIpAddress(fields[3]))
                    .destinationIp(parseIpAddress(fields[4]))
                    .sourcePort(parseSrcPort(fields[5]))
                    .destinationPort(parseDesPort(fields[6]))
                    .protocol(parseProtocol(fields[7]))
                    .packets(parseInteger(fields[8]))
                    .bytes(parseLong(fields[9]))
                    .startTime(parseLong(fields[10]))
                    .endTime(parseLong(fields[11]))
                    .action(fields[12])
                    .logStatus(fields[13])
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing log line: " + logLine, e);
        }
    }

    private int parseVersion(String value) {
        int version = Integer.parseInt(value);
        if (version != 2) {
            throw new IllegalArgumentException("Unsupported version: " + version);
        }
        return version;
    }

    private String parseIpAddress(String value) {
        if (value.equals("-")) return "-1";

        String[] octets = value.split("\\.");
        if (octets.length != 4) {
            throw new IllegalArgumentException("Invalid IP address format: " + value);
        }
        return value;
    }

    private int parseSrcPort(String value) {
        if (value.equals("-")) return -1;
        int port = Integer.parseInt(value);
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port number: " + port);
        }
        return port;
    }

    private int parseDesPort(String value) {
        int port = Integer.parseInt(value);
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port number: " + port);
        }
        return port;
    }

    private int parseProtocol(String value) {
        int protocol = Integer.parseInt(value);
        if (protocol < 0) {
            throw new IllegalArgumentException("Invalid protocol number: " + protocol);
        }
        return protocol;
    }

    private int parseInteger(String value) {
        return "-".equals(value) ? -1 : Integer.parseInt(value);
    }

    private long parseLong(String value) {
        return "-".equals(value) ? -1 : Long.parseLong(value);
    }
}
