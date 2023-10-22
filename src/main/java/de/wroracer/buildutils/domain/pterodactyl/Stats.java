package de.wroracer.buildutils.domain.pterodactyl;

import com.google.gson.annotations.SerializedName;

public class Stats extends PterodactylObject {


    @SerializedName("current_state")
    private String serverState;

    @SerializedName("is_suspended")
    private boolean suspended;

    private Resources resources;

    public String getServerState() {
        return serverState;
    }

    public void setServerState(String serverState) {
        this.serverState = serverState;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public static class Resources {
        @SerializedName("memory_bytes")
        private long memoryBytes;

        @SerializedName("cpu_absolute")
        private double cpuAbsolute;

        @SerializedName("disk_bytes")
        private long diskBytes;

        @SerializedName("network_rx_bytes")
        private long networkRxBytes;

        @SerializedName("network_tx_bytes")
        private long networkTxBytes;

        private long uptime;

        public long getMemoryBytes() {
            return memoryBytes;
        }

        public void setMemoryBytes(long memoryBytes) {
            this.memoryBytes = memoryBytes;
        }

        public double getCpuAbsolute() {
            return cpuAbsolute;
        }

        public void setCpuAbsolute(double cpuAbsolute) {
            this.cpuAbsolute = cpuAbsolute;
        }

        public long getDiskBytes() {
            return diskBytes;
        }

        public void setDiskBytes(long diskBytes) {
            this.diskBytes = diskBytes;
        }

        public long getNetworkRxBytes() {
            return networkRxBytes;
        }

        public void setNetworkRxBytes(long networkRxBytes) {
            this.networkRxBytes = networkRxBytes;
        }

        public long getNetworkTxBytes() {
            return networkTxBytes;
        }

        public void setNetworkTxBytes(long networkTxBytes) {
            this.networkTxBytes = networkTxBytes;
        }

        public long getUptime() {
            return uptime;
        }

        public void setUptime(long uptime) {
            this.uptime = uptime;
        }
    }
}
