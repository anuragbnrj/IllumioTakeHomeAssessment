package in.anuragbanerjee.model;

public class LogEntry {
    private final int version;
    private final long accountId;
    private final String interfaceId;
    private final String sourceIp;
    private final String destinationIp;
    private final int sourcePort;
    private final int destinationPort;
    private final int protocol;
    private final int packets;
    private final long bytes;
    private final long startTime;
    private final long endTime;
    private final String action;
    private final String logStatus;

    private LogEntry(Builder builder) {
        this.version = builder.version;
        this.accountId = builder.accountId;
        this.interfaceId = builder.interfaceId;
        this.sourceIp = builder.sourceIp;
        this.destinationIp = builder.destinationIp;
        this.sourcePort = builder.sourcePort;
        this.destinationPort = builder.destinationPort;
        this.protocol = builder.protocol;
        this.packets = builder.packets;
        this.bytes = builder.bytes;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.action = builder.action;
        this.logStatus = builder.logStatus;
    }

    // Getters remain the same
    public int getVersion() { return version; }
    public long getAccountId() { return accountId; }
    public String getInterfaceId() { return interfaceId; }
    public String getSourceIp() { return sourceIp; }
    public String getDestinationIp() { return destinationIp; }
    public int getDestinationPort() { return destinationPort; }
    public int getSourcePort() { return sourcePort; }
    public int getProtocol() { return protocol; }
    public int getPackets() { return packets; }
    public long getBytes() { return bytes; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
    public String getAction() { return action; }
    public String getLogStatus() { return logStatus; }

    public String getProtocolAsString() {
        return (switch (protocol) {
            case 0 -> "HOPOPT";
            case 1 -> "ICMP";
            case 2 -> "IGMP";
            case 3 -> "GGP";
            case 4 -> "IPv4";
            case 5 -> "ST";
            case 6 -> "TCP";
            case 17 -> "UDP";
            case 18 -> "MUX";
            case 27 -> "RDP";
            case 28 -> "IRTP";
            case 29 -> "ISO-TP4";
            case 30 -> "NETBLT";
            case 31 -> "MFE-NSP";
            case 32 -> "MERIT-INP";
            case 33 -> "DCCP";
            case 34 -> "3PC";
            case 35 -> "IDPR";
            case 36 -> "XTP";
            case 37 -> "DDP";
            case 38 -> "IDPR-CMTP";
            case 39 -> "TP++";
            case 40 -> "IL";
            case 41 -> "IPv6";
            case 42 -> "SDRP";
            case 43 -> "IPv6-Route";
            case 44 -> "IPv6-Frag";
            case 45 -> "IDRP";
            case 46 -> "RSVP";
            case 47 -> "GRE";
            case 48 -> "DSR";
            case 49 -> "BNA";
            case 50 -> "ESP";
            case 51 -> "AH";
            case 88 -> "EIGRP";
            case 89 -> "OSPF";
            case 103 -> "PIM";
            case 108 -> "IPComp";
            case 112 -> "VRRP";
            case 115 -> "L2TP";
            case 124 -> "ISIS";
            case 132 -> "SCTP";
            case 133 -> "FC";
            case 135 -> "Mobility-Header";
            case 136 -> "UDPLite";
            case 137 -> "MPLS-in-IP";
            case 138 -> "manet";
            case 139 -> "HIP";
            case 140 -> "Shim6";
            case 141 -> "WESP";
            case 142 -> "ROHC";
            default -> String.valueOf(protocol);
        }).toLowerCase();
    }

    public boolean hasValidBytes() {
        return bytes != -1;
    }

    public boolean hasValidStartTime() {
        return startTime != -1;
    }

    @Override
    public String toString() {
        return String.format(
                "LogEntry{version=%d, accountId=%d, interfaceId='%s', sourceIp='%s', destinationIp='%s', " +
                        "sourcePort=%d, destinationPort=%d, protocol=%d, packets=%d, bytes=%d, startTime=%d, " +
                        "endTime=%d, action='%s', logStatus='%s'}",
                version, accountId, interfaceId, sourceIp, destinationIp, sourcePort, destinationPort,
                protocol, packets, bytes, startTime, endTime, action, logStatus
        );
    }

    // Builder class
    public static class Builder {
        private int version;
        private long accountId;
        private String interfaceId;
        private String sourceIp;
        private String destinationIp;
        private int sourcePort;
        private int destinationPort;
        private int protocol;
        private int packets;
        private long bytes = -1;  // Default value
        private long startTime = -1;  // Default value
        private long endTime;
        private String action;
        private String logStatus;

        public Builder() {}

        public Builder version(int version) {
            this.version = version;
            return this;
        }

        public Builder accountId(long accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder interfaceId(String interfaceId) {
            this.interfaceId = interfaceId;
            return this;
        }

        public Builder sourceIp(String sourceIp) {
            this.sourceIp = sourceIp;
            return this;
        }

        public Builder destinationIp(String destinationIp) {
            this.destinationIp = destinationIp;
            return this;
        }

        public Builder sourcePort(int sourcePort) {
            this.sourcePort = sourcePort;
            return this;
        }

        public Builder destinationPort(int destinationPort) {
            this.destinationPort = destinationPort;
            return this;
        }

        public Builder protocol(int protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder packets(int packets) {
            this.packets = packets;
            return this;
        }

        public Builder bytes(long bytes) {
            this.bytes = bytes;
            return this;
        }

        public Builder startTime(long startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(long endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder logStatus(String logStatus) {
            this.logStatus = logStatus;
            return this;
        }

        public LogEntry build() {
            return new LogEntry(this);
        }
    }
}