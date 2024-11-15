package in.anuragbanerjee.model;

import java.util.Objects;

public class PortProtocolKey {
    private final int port;
    private final String protocol;

    public PortProtocolKey(int port, String protocol) {
        this.port = port;
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortProtocolKey that = (PortProtocolKey) o;
        return getPort() == that.getPort() && Objects.equals(getProtocol(), that.getProtocol());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPort(), getProtocol());
    }

    @Override
    public String toString() {
        return "PortProtocolKey{" +
                "port=" + port +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
